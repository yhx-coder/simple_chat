package com.example.handler;


import com.example.config.Config;
import com.example.dao.UserDao;
import com.example.message.*;

import com.example.pojo.User;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: ming
 * @date: 2021/8/26 22:20
 */
public class ServerChatHandler extends SimpleChannelInboundHandler<Message> {

    private final UserDao userDao;
    private final InputStream resourceAsStream;
    private final SqlSession session;

    public ServerChatHandler() throws IOException {
        resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = sessionFactoryBuilder.build(resourceAsStream);

        session = factory.openSession();

        userDao = session.getMapper(UserDao.class);
    }

    // 防止并发问题
    private static final Map<Integer, Channel> map = new ConcurrentHashMap<>();

    private static final Map<Integer, List<Integer>> userGroupMap = new ConcurrentHashMap<>();

    private static final Map<Integer, List<Integer>> groupUserMap = new ConcurrentHashMap<>();

    private final static Logger logger = LoggerFactory.getLogger(ServerChatHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Message.MessageType messageType = msg.getMessageType();
        switch (messageType) {
            // 登录请求
            case LOGIN_REQ: {

                LoginReq loginReq = msg.getLoginReq();
                String username = loginReq.getUsername();
                String password = loginReq.getPassword();
                User user = userDao.queryByUsernameAndPassword(username, password);

                // 查到用户
                if (user != null) {
                    Integer userId = user.getUserId();
                    Channel channel = map.get(userId);
                    // 用户处于离线状态
                    if (channel == null) {
                        map.put(userId, ctx.channel());
                        LoginRes loginRes = Message.newBuilder()
                                .getLoginRes().newBuilderForType()
                                .setStatus(LoginRes.LoginStatus.SUCCESS)
                                .setResponse("用户: " + username + " 登录成功")
                                .setSUserId(userId)
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.LOGIN_RES)
                                .setLoginRes(loginRes)
                                .build();
                        ctx.channel().writeAndFlush(message);

                        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(userId);
                        ctx.channel().attr(AttributeKey.<String>valueOf("username")).set(username);

                        // 推送离线时缓存的消息
                        if (hasUnreadMessage(userId)){
                            List<Message> messages = retrieveMessage(userId);
                            for (Message unreadMessage : messages) {
                                ctx.channel().writeAndFlush(unreadMessage);
                            }
                        }
                    } else {
                        // 挤掉线的操作
                        map.put(userId, ctx.channel());
                        LoginRes loginRes = Message.newBuilder().getLoginRes().newBuilderForType()
                                .setStatus(LoginRes.LoginStatus.REMOTE)
                                .setResponse("用户: " + username + " 已经在其他设备登录。先按enter，再输用户名。")
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.LOGIN_RES)
                                .setLoginRes(loginRes)
                                .build();
                        channel.writeAndFlush(message);


                        LoginRes loginRes1 = Message.newBuilder()
                                .getLoginRes().newBuilderForType()
                                .setStatus(LoginRes.LoginStatus.SUCCESS)
                                .setResponse("用户: " + username + " 登录成功")
                                .setSUserId(userId)
                                .build();
                        Message message1 = Message.newBuilder()
                                .setMessageType(Message.MessageType.LOGIN_RES)
                                .setLoginRes(loginRes1)
                                .build();
                        ctx.channel().writeAndFlush(message1);

                        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(userId);
                        ctx.channel().attr(AttributeKey.<String>valueOf("username")).set(username);
                    }
                    break;
                }

                // 未找到用户
                LoginRes loginRes = Message.newBuilder()
                        .getLoginRes().newBuilderForType()
                        .setStatus(LoginRes.LoginStatus.FAIL)
                        .setResponse("用户名或密码错误")
                        .build();

                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.LOGIN_RES)
                        .setLoginRes(loginRes)
                        .build();
                ctx.channel().writeAndFlush(message);

                break;
            }
            // 消息发送
            case MSG_REQ: {
                String dUsername = msg.getMsgReq().getDUsername();
                User user = userDao.queryByUsername(dUsername);

                // 用户存在
                if (user != null) {
                    Integer dUserId = user.getUserId();
                    // 获取的是通信对端的连接，消息要靠这个连接发送
                    Channel channel = map.get(dUserId);

                    if (channel == null) {
                        // 目标用户当前不在线，先将要发送的消息缓存起来。
                        storeMessage(ctx,msg,dUserId);

                        MsgRes msgRes = Message.newBuilder().getMsgRes()
                                .newBuilderForType()
                                .setStatus(MsgRes.Status.FAIL)
                                .setResponse("用户: " + dUsername + " 当前不在线, 消息已缓存!")
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.MSG_RES)
                                .setMsgRes(msgRes)
                                .build();
                        ctx.channel().writeAndFlush(message);

                    } else {
                        MsgRX msgRes = Message.newBuilder().getMsgRX()
                                .newBuilderForType()
                                .setSUsername(ctx.channel().attr(AttributeKey.<String>valueOf("username")).get())
                                .setContent(msg.getMsgReq().getMsg())
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.MSG_RX)
                                .setMsgRX(msgRes)
                                .build();
                        channel.writeAndFlush(message);
                    }
                    break;
                }

                MsgRes msgRes = Message.newBuilder().getMsgRes()
                        .newBuilderForType()
                        .setStatus(MsgRes.Status.FAIL)
                        .setResponse("用户: " + dUsername + " 不存在, 消息发送失败!")
                        .build();
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.MSG_RES)
                        .setMsgRes(msgRes)
                        .build();
                ctx.channel().writeAndFlush(message);
                break;
            }
            // 创建聊天组
            case GROUP_CREATE_REQ: {
                int groupId = msg.getGroupCreateReq().getGroupId();
                List<Integer> list = groupUserMap.get(groupId);
                if (list == null || list.isEmpty()) {
                    List<Integer> userIds = new CopyOnWriteArrayList<>();
                    Integer userId = msg.getGroupCreateReq().getUserId();
                    userIds.add(userId);
                    groupUserMap.put(groupId, userIds);

                    List<Integer> groupIds = userGroupMap.get(userId);

                    if (groupIds == null) {
                        groupIds = new CopyOnWriteArrayList<>();
                    }
                    groupIds.add(groupId);

                    userGroupMap.put(userId, groupIds);


                    GroupRes groupCreateRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(true)
                            .setReason("群组" + groupId + "建立成功")
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_RES)
                            .setGroupRes(groupCreateRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                } else {
                    GroupRes groupCreateRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("群组" + groupId + "已经存在")
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_RES)
                            .setGroupRes(groupCreateRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                }
                break;
            }
            // 加入聊天组
            case GROUP_JOIN_REQ: {
                int userId = msg.getGroupJoinReq().getUserId();
                int groupId = msg.getGroupJoinReq().getJoinId();
                String username = ctx.channel().attr(AttributeKey.<String>valueOf("username")).get();

                List<Integer> userIds = groupUserMap.get(groupId);

                GroupRes groupJoinRes;

                // 先看该组有没有被创建
                if (userIds == null) {
                    groupJoinRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("群组" + groupId + "不存在, 请先创建!")
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_RES)
                            .setGroupRes(groupJoinRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                    break;
                }

                // 检查用户是否在该组内
                if (userIds.contains(userId)) {
                    groupJoinRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + username + "已经在聊天组" + groupId + "中了")
                            .build();
                } else {
                    userIds.add(userId);
                    groupUserMap.put(groupId, userIds);

                    List<Integer> groupIds = userGroupMap.get(userId);
                    // 若该用户之前没有加过群组
                    if (groupIds == null) {
                        List<Integer> group = new CopyOnWriteArrayList<>();
                        group.add(groupId);
                        userGroupMap.put(userId, group);
                    } else {
                        groupIds.add(groupId);
                        userGroupMap.put(userId, groupIds);
                    }

                    groupJoinRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(true)
                            .setReason("用户" + username + "加入聊天组" + groupId + "成功")
                            .build();
                }

                // 构造发送的消息
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.GROUP_RES)
                        .setGroupRes(groupJoinRes)
                        .build();
                ctx.channel().writeAndFlush(message);
                break;
            }
            // 退出聊天组
            case GROUP_QUIT_REQ: {
                int userId = msg.getGroupQuitReq().getUserId();
                int groupId = msg.getGroupQuitReq().getGroupId();
                String username = ctx.channel().attr(AttributeKey.<String>valueOf("username")).get();
                List<Integer> groupIds = userGroupMap.get(userId);

                GroupRes groupQuitRes;

                if (groupIds != null && groupIds.contains(groupId)) {
                    groupIds.remove(Integer.valueOf(groupId));
                    userGroupMap.put(userId, groupIds);

                    List<Integer> userIds = groupUserMap.get(groupId);
                    userIds.remove(Integer.valueOf(userId));
                    groupUserMap.put(groupId, userIds);

                    groupQuitRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(true)
                            .setReason("用户" + username + "退出聊天组" + groupId + "成功")
                            .build();
                } else {
                    groupQuitRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + username + "未加入聊天组" + groupId)
                            .build();
                }

                // 发送消息
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.GROUP_RES)
                        .setGroupRes(groupQuitRes)
                        .build();
                ctx.channel().writeAndFlush(message);
                break;
            }
            // 用户加入的聊天组查询
            case GROUP_JOINED_QUERY_REQ: {
                GroupJoinedQueryReq groupQueryReq = msg.getGroupQueryReq();
                int userId = groupQueryReq.getUserId();
                String username = ctx.channel().attr(AttributeKey.<String>valueOf("username")).get();
                List<Integer> list = userGroupMap.get(userId);

                GroupJoinedQueryRes groupQueryRes;

                if (list == null || list.isEmpty()) {
                    groupQueryRes = Message.newBuilder()
                            .getGroupQueryRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + username + "未加入过聊天组")
                            .build();
                } else {
                    groupQueryRes = Message.newBuilder()
                            .getGroupQueryRes().newBuilderForType()
                            .setStatus(true)
                            .addAllGroupId(list)
                            .build();
                }
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.GROUP_JOINED_QUERY_RES)
                        .setGroupQueryRes(groupQueryRes)
                        .build();
                ctx.channel().writeAndFlush(message);
                break;
            }
            // 群组中加入的用户查询
            case GROUP_MEMBER_QUERY_REQ: {
                GroupMemberQueryReq groupMemberQueryReq = msg.getGroupMemberQueryReq();
                int groupId = groupMemberQueryReq.getGroupId();
                List<Integer> userIds = groupUserMap.get(groupId);

                GroupMemberQueryRes groupMemberQueryRes;

                if (userIds == null || userIds.isEmpty()) {
                    groupMemberQueryRes = Message.newBuilder()
                            .getGroupMemberQueryRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("聊天组" + groupId + "未创建")
                            .build();
                } else {
                    List<String> usernames = new ArrayList<>();
                    userIds.forEach(userId -> {
                        User user = userDao.queryByUserId(userId);
                        usernames.add(user.getUsername());
                    });
                    groupMemberQueryRes = Message.newBuilder()
                            .getGroupMemberQueryRes().newBuilderForType()
                            .setStatus(true)
                            .addAllUsername(usernames)
                            .build();
                }
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.GROUP_MEMBER_QUERY_RES)
                        .setGroupMemberQueryRes(groupMemberQueryRes)
                        .build();
                ctx.channel().writeAndFlush(message);
                break;
            }
            //用户的群发消息处理
            case GROUP_MSG_REQ: {
                GroupMessageReq groupMessageReq = msg.getGroupMessageReq();

                int sUserId = groupMessageReq.getSUserId();
                int groupId = groupMessageReq.getGroupId();
                List<Integer> groupIds = userGroupMap.get(sUserId);

                if (groupIds != null && groupIds.contains(groupId)) {
                    GroupMessageRX groupMessageRX = Message.newBuilder()
                            .getGroupMessageRX().newBuilderForType()
                            .setSUserId(sUserId)
                            .setMessage(groupMessageReq.getMessage())
                            .build();

                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_MSG_RX)
                            .setGroupMessageRX(groupMessageRX)
                            .build();
                    // --------------------开始发送----------------------------
                    List<Integer> userIds = groupUserMap.get(groupId);
                    for (int id : userIds) {
                        Channel channel = map.get(id);
                        channel.writeAndFlush(message);
                    }
                } else {
                    GroupRes groupRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + sUserId + "未加入群组" + groupId)
                            .build();

                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_RES)
                            .setGroupRes(groupRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                }

                break;
            }

            default: {
                break;
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


//        Channel 断开，服务端监听到连接断开事件，但是此时 Channel 所绑定的属性已经被移除掉了，因此这里无法直接获取的到 userid。
//        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get() 不行
//        感谢慕课网的 netty 教程
        Channel channel = ctx.channel();
        Integer userId = null;

        for (Map.Entry<Integer, Channel> entry : map.entrySet()) {
            Integer uid = entry.getKey();
            Channel channel1 = entry.getValue();
            if (channel == channel1) {
                userId = uid;
                break;
            }
        }
        if (userId != null) {
            map.remove(userId);

            List<Integer> groups = userGroupMap.get(userId);
            userGroupMap.remove(userId);

            for (Integer groupId : groups) {
                List<Integer> users = groupUserMap.get(groupId);
                users.remove(userId);
                groupUserMap.put(groupId, users);
            }

            ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(null);

            resourceAsStream.close();
            session.close();
            super.channelInactive(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("服务端内部出现连接异常: " + cause);
        resourceAsStream.close();
        session.close();
        ctx.close();
    }


    // 只缓存一条，一会改成消息列表
    private void storeMessage(ChannelHandlerContext ctx, Message msg, Integer dUserId) throws InvalidProtocolBufferException {
        MsgRX msgRes = Message.newBuilder().getMsgRX()
                .newBuilderForType()
                .setSUsername(ctx.channel().attr(AttributeKey.<String>valueOf("username")).get())
                .setContent(msg.getMsgReq().getMsg())
                .build();
        Message message = Message.newBuilder()
                .setMessageType(Message.MessageType.MSG_RX)
                .setMsgRX(msgRes)
                .build();

        String unreadMessageJson = JsonFormat.printer().print(message);
        Jedis jedis = new Jedis(Config.getRedisAddress(), Config.getRedisPort());
        jedis.lpush(String.valueOf(dUserId), unreadMessageJson);
        jedis.close();
    }

    private List<Message> retrieveMessage(Integer dUserId) throws InvalidProtocolBufferException {
        Jedis jedis = new Jedis(Config.getRedisAddress(), Config.getRedisPort());
        List<Message> unreadMessageList = new ArrayList<>();
        while (true) {
            String unreadMessageJson = jedis.lpop(String.valueOf(dUserId));
            if (unreadMessageJson == null) {
                break;
            }
            Message.Builder builder = Message.newBuilder();
            JsonFormat.parser().merge(unreadMessageJson, builder);
            unreadMessageList.add(builder.build());
        }
        jedis.close();
        return unreadMessageList;
    }

    private boolean hasUnreadMessage(Integer userId){
        Jedis jedis = new Jedis(Config.getRedisAddress(), Config.getRedisPort());
        return jedis.exists(String.valueOf(userId));
    }
}
