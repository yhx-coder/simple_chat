package com.example.handler;

import com.example.message.LoginRes;
import com.example.message.Message;
import com.example.message.MsgRX;
import com.example.message.MsgRes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ming
 * @date: 2021/8/26 22:20
 */
public class ServerChatHandler extends SimpleChannelInboundHandler<Message> {

    private static Map<Integer, Channel> map = new HashMap<>();

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
                            .setResponse("用户: " + userId + "登录成功")
                            .setUserId(userId)
                            .build();
                }else{
                    loginRes = loginRes.newBuilderForType()
                            .setStatus(LoginRes.LoginStatus.SUCCESS)
                            .setResponse("用户: " + userId + "已经登录")
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
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
