package com.example.handler;

import com.example.dao.UserDao;
import com.example.message.*;
import com.example.pojo.User;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: ming
 * @date: 2021/8/26 22:20
 */
public class ServerChatHandler extends SimpleChannelInboundHandler<Message> {

    public ServerChatHandler() {
    }

    // 防止并发问题
    private static volatile Map<Integer, Channel> map = new ConcurrentHashMap<>();

    private static volatile Map<Integer, List<Integer>> userGroupMap = new ConcurrentHashMap<>();

    private static volatile Map<Integer, List<Integer>> groupUserMap = new ConcurrentHashMap<>();

    private final static Logger logger = LoggerFactory.getLogger(ServerChatHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Message.MessageType messageType = msg.getMessageType();
        switch (messageType) {
            // 登录请求
            case LOGIN_REQ: {

                // 数据库的连接怎么关啊？放在 finally 块中就显示未初始化。求教。
                LoginReq loginReq = msg.getLoginReq();
                String username = loginReq.getUsername();
                String password = loginReq.getPassword();

                InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
                SqlSessionFactoryBuilder sessionFactoryBuilder = new SqlSessionFactoryBuilder();
                SqlSessionFactory factory = sessionFactoryBuilder.build(resourceAsStream);

                SqlSession session = factory.openSession();
                UserDao userDao = session.getMapper(UserDao.class);

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
                                .setResponse("用户: " + userId + " 登录成功")
                                .setUserId(userId)
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.LOGIN_RES)
                                .setLoginRes(loginRes)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        resourceAsStream.close();
                        session.close();
                        break;
                    }
                    LoginRes loginRes = Message.newBuilder().getLoginRes().newBuilderForType()
                            .setStatus(LoginRes.LoginStatus.SUCCESS)
                            .setResponse("用户: " + userId + " 已经登录")
                            .setUserId(userId)
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.LOGIN_RES)
                            .setLoginRes(loginRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                    resourceAsStream.close();
                    session.close();
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

                resourceAsStream.close();
                session.close();
                break;
            }
            // 消息发送
            case MSG_REQ: {
                int dUserId = msg.getMsgReq().getDUserId();

                // 获取的是通信对端的连接，消息要靠这个连接发送
                Channel channel = map.get(dUserId);

                if (channel == null) {
                    MsgRes msgRes = Message.newBuilder().getMsgRes()
                            .newBuilderForType()
                            .setStatus(MsgRes.Status.FAIL)
                            .setResponse("用户: " + dUserId + " 当前不在线, 消息发送失败!")
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.MSG_RES)
                            .setMsgRes(msgRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                } else {
                    int sUserId = msg.getMsgReq().getSUserId();
                    MsgRX msgRes = Message.newBuilder().getMsgRX()
                            .newBuilderForType()
                            .setSUserId(sUserId)
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
                    if (groupIds != null) {
                        groupIds.add(groupId);
                    } else {
                        groupIds = new CopyOnWriteArrayList<>();
                        groupIds.add(groupId);
                    }
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
                            .setReason("用户" + userId + "已经在聊天组" + groupId + "中了")
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
                            .setReason("用户" + userId + "加入聊天组" + groupId + "成功")
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
                            .setReason("用户" + userId + "退出聊天组" + groupId + "成功")
                            .build();
                } else {
                    groupQuitRes = Message.newBuilder()
                            .getGroupRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + userId + "未加入聊天组" + groupId)
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
                List<Integer> list = userGroupMap.get(userId);

                GroupJoinedQueryRes groupQueryRes;

                if (list == null || list.isEmpty()) {
                    groupQueryRes = Message.newBuilder()
                            .getGroupQueryRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + userId + "未加入过聊天组")
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
                    groupMemberQueryRes = Message.newBuilder()
                            .getGroupMemberQueryRes().newBuilderForType()
                            .setStatus(true)
                            .addAllUserId(userIds)
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
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


//        Channel 断开，服务端监听到连接断开事件，但是此时 Channel 所绑定的属性已经被移除掉了，因此这里无法直接获取的到 userid。
//        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get() 不行
//        感谢慕课网的 netty 教程
        Channel channel = ctx.channel();
        Integer userId = null;

        for (Map.Entry<Integer,Channel> entry : map.entrySet()){
            Integer uid = entry.getKey();
            Channel channel1 = entry.getValue();
            if (channel == channel1){
                userId=uid;
                break;
            }
        }
        if (userId!=null){
            map.remove(userId);

            List<Integer> groups = userGroupMap.get(userId);
            userGroupMap.remove(userId);

            for (Integer groupId : groups){
                List<Integer> users = groupUserMap.get(groupId);
                users.remove(userId);
                groupUserMap.put(groupId,users);
            }

            ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(null);
            super.channelInactive(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("服务端内部出现连接异常: " + cause);
        ctx.close();
    }
}
