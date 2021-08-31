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
    private static Map<Integer, Channel> map = new ConcurrentHashMap<>();

    private static Map<Integer, List<Integer>> userGroupMap = new ConcurrentHashMap<>();

    private static Map<Integer,List<Integer>> groupUserMap = new ConcurrentHashMap<>();

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
                }else{
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

                if (channel == null){
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
                }else{
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
                    Integer userId = msg.getGroupCreateReq().getUserId();
                    List<Integer> user = new CopyOnWriteArrayList();
                    List<Integer> group = new CopyOnWriteArrayList();
                    user.add(userId);
                    group.add(groupId);
                    groupUserMap.put(groupId, user);
                    userGroupMap.put(userId, group);

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
