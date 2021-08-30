package com.example.handler;

import com.example.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


/**
 * @author: ming
 * @date: 2021/8/27 18:16
 */
public class ClientChatHandler extends SimpleChannelInboundHandler<Message> {

    private CountDownLatch latch = new CountDownLatch(1);

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
                latch.countDown();
            } else {
                System.out.println("登录失败");
                ctx.close();
            }
        } else if (messageType == Message.MessageType.MSG_RX) {
            MsgRX msgRX = msg.getMsgRX();
            System.out.println("用户" + msgRX.getSUserId() + "说: " + msgRX.getContent());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            login(ctx);
            System.out.println(latch);
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                menu();

                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[] s = line.split(" ");
                switch (s[0]) {
                    case "send": {
                        MsgReq req = Message.newBuilder()
                                .getMsgReq().newBuilderForType()
                                .setDUserId(Integer.parseInt(s[1]))
                                .setMsg(s[2])
                                .build();

                        Message msgReq = Message.newBuilder()
                                .setMessageType(Message.MessageType.MSG_REQ)
                                .setMsgReq(req)
                                .build();

                        ctx.channel().writeAndFlush(msgReq);
                        break;
                    }
                    case "quit":{
                        System.out.println("您已退出聊天！");
                        ctx.close();
                        return;
                    }
                }
            }
        }, "waitForSelection").start();
    }


    private void login(ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入UserId: ");
        int userId = scanner.nextInt();

        // 一定要分两段构建消息，否则要报错。因为要解析的是 Message 。 若一次构建则是 LoginReq 了。
        LoginReq req = Message.newBuilder()
                .getLoginReq().newBuilderForType()
                .setUsername(username)
                .setUserId(userId)
                .build();

        Message loginReq = Message.newBuilder()
                .setMessageType(Message.MessageType.LOGIN_REQ)
                .setLoginReq(req)
                .build();

        ctx.channel().writeAndFlush(loginReq);
    }

    private void menu() {
        System.out.println("send [userId] [message]");
        System.out.println("quit");
    }
}
