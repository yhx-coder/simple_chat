package com.example.handler;

import com.example.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.util.Scanner;

/**
 * @author: ming
 * @date: 2021/8/27 18:16
 */
public class ClientChatHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        Message.MessageType messageType = msg.getMessageType();

        if (messageType == Message.MessageType.MSG_RES) {

            System.out.println(msg.getMsgRes().getResponse());

        } else if (messageType == Message.MessageType.LOGIN_RES) {

            LoginRes loginRes = msg.getLoginRes();
            System.out.println(loginRes.getResponse());
            if (loginRes.getStatus() == LoginRes.LoginStatus.SUCCESS) {
                ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(loginRes.getUserId());
                sendMsg(ctx);
            } else {
                login(ctx);
            }
        } else if (messageType == Message.MessageType.MSG_RX) {
            MsgRX msgRX = msg.getMsgRX();
            System.out.println("用户" + msgRX.getSUserId() + "说: " + msgRX.getContent());
            sendMsg(ctx);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        login(ctx);
    }

    private void sendMsg(ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);
        new Thread(()->{
            while (true) {
                System.out.print("请输入接收方的userId: ");
                int dUserId = scanner.nextInt();
                System.out.print("请输入要发送的消息, 按回车结束: ");
                scanner.nextLine();         // 就很烦，要接收 nextInt() 中的 \n
                String line = scanner.nextLine();

                Integer sUserId = ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get();

                MsgReq msgReq = Message.newBuilder().getMsgReq()
                        .newBuilderForType()
                        .setSUserId(sUserId)
                        .setMsg(line)
                        .setDUserId(dUserId)
                        .build();


                Message message = Message.newBuilder()
                        .setMessageType(Message.MessageType.MSG_REQ)
                        .setMsgReq(msgReq)
                        .build();
                ctx.channel().writeAndFlush(message);
            }
        }).start();
    }

    private void login(ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入UserId: ");
        int userId = scanner.nextInt();

        LoginReq loginReq = Message.newBuilder().getLoginReq().newBuilderForType()
                .setUserId(userId)
                .setUsername(username)
                .build();

        Message message = Message.newBuilder()
                .setMessageType(Message.MessageType.LOGIN_REQ)
                .setLoginReq(loginReq)
                .build();
        ctx.channel().writeAndFlush(message);
    }
}
