package com.example.handler;

import com.example.message.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

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

    // 防止并发问题
    private static volatile Map<Integer, Channel> map = new ConcurrentHashMap<>();

    private static volatile Map<Integer, List<Integer>> userGroupMap = new ConcurrentHashMap<>();

    private static volatile Map<Integer, List<Integer>> groupUserMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        Message.MessageType messageType = msg.getMessageType();
        switch (messageType) {
            // 登录请求
            case LOGIN_REQ: {
                int userId = msg.getLoginReq().getUserId();
                Channel channel = map.get(userId);

                LoginRes loginRes = Message.newBuilder().getLoginRes();
                // 用户处于离线状态
                if (channel == null) {
                    map.put(userId, ctx.channel());
                    loginRes = loginRes.newBuilderForType()
                            .setStatus(LoginRes.LoginStatus.SUCCESS)
                            .setResponse("用户: " + userId + " 登录成功")
                            .setUserId(userId)
                            .build();
                } else {
                    loginRes = loginRes.newBuilderForType()
                            .setStatus(LoginRes.LoginStatus.SUCCESS)
                            .setResponse("用户: " + userId + " 已经登录")
                            .setUserId(userId)
                            .build();
                }
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.LOGIN_RES)
                        .setLoginRes(loginRes)
                        .build();
                ctx.channel().writeAndFlush(message);
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


                    GroupCreateRes groupCreateRes = Message.newBuilder()
                            .getGroupCreateRes().newBuilderForType()
                            .setStatus(true)
                            .setReason("群组" + groupId + "建立成功")
                            .setGroupId(groupId)
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_CREATE_RES)
                            .setGroupCreateRes(groupCreateRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                } else {
                    GroupCreateRes groupCreateRes = Message.newBuilder()
                            .getGroupCreateRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("群组" + groupId + "已经存在")
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_CREATE_RES)
                            .setGroupCreateRes(groupCreateRes)
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

                GroupJoinRes groupJoinRes;

                // 先看该组有没有被创建
                if (userIds == null) {
                    // 为什么必须在这里发送，无法利用下面的统一消息发送? 奇怪
                    groupJoinRes = Message.newBuilder()
                            .getGroupJoinRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("群组" + groupId + "不存在, 请先创建!")
                            .build();
                    Message message = Message.newBuilder()
                            .setMessageType(Message.MessageType.GROUP_JOIN_RES)
                            .setGroupJoinRes(groupJoinRes)
                            .build();
                    ctx.channel().writeAndFlush(message);
                }

                // 检查用户是否在该组内
                if (userIds.contains(userId)) {
                    groupJoinRes = Message.newBuilder()
                            .getGroupJoinRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + userId + "已经在聊天组" + groupId + "中了")
                            .setGroupId(groupId)
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
                            .getGroupJoinRes().newBuilderForType()
                            .setStatus(true)
                            .setReason("用户" + userId + "加入聊天组" + groupId + "成功")
                            .setGroupId(groupId)
                            .build();
                }

                // 构造发送的消息
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.GROUP_JOIN_RES)
                        .setGroupJoinRes(groupJoinRes)
                        .build();
                ctx.channel().writeAndFlush(message);
                break;
            }
            // 退出聊天组
            case GROUP_QUIT_REQ: {
                int userId = msg.getGroupQuitReq().getUserId();
                int groupId = msg.getGroupQuitReq().getGroupId();
                List<Integer> groupIds = userGroupMap.get(userId);

                GroupQuitRes groupQuitRes;

                if (groupIds != null && groupIds.contains(groupId)) {
                    groupIds.remove(Integer.valueOf(groupId));
                    userGroupMap.put(userId, groupIds);

                    List<Integer> userIds = groupUserMap.get(groupId);
                    userIds.remove(Integer.valueOf(userId));
                    groupUserMap.put(groupId, userIds);

                    groupQuitRes = Message.newBuilder()
                            .getGroupQuitRes().newBuilderForType()
                            .setStatus(true)
                            .setReason("用户" + userId + "退出聊天组" + groupId + "成功")
                            .setGroupId(groupId)
                            .build();
                } else {
                    groupQuitRes = Message.newBuilder()
                            .getGroupQuitRes().newBuilderForType()
                            .setStatus(false)
                            .setReason("用户" + userId + "未加入聊天组" + groupId)
                            .build();
                }

                // 发送消息
                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.GROUP_QUIT_RES)
                        .setGroupQuitRes(groupQuitRes)
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
                    ctx.channel().writeAndFlush(message);
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
        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
