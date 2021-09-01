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

    // 在这里接收消息，即处理各种 *Res
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        Message.MessageType messageType = msg.getMessageType();

        switch (messageType){
            case MSG_RES:{
                System.out.println(msg.getMsgRes().getResponse());
                break;
            }
            case LOGIN_RES:{
                LoginRes loginRes = msg.getLoginRes();
                System.out.println(loginRes.getResponse());
                if (loginRes.getStatus() == LoginRes.LoginStatus.SUCCESS) {
                    ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(loginRes.getUserId());
                    latch.countDown();
                } else {
                    System.out.println("登录失败");
                    ctx.close();
                }
                break;
            }
            case MSG_RX:{
                MsgRX msgRX = msg.getMsgRX();
                System.out.println("用户" + msgRX.getSUserId() + "说: " + msgRX.getContent());
                break;
            }
            case GROUP_RES:{
                System.out.println(msg.getGroupRes().getReason());
                break;
            }

        }
    }

    // 在这里发送消息，即构造各种 *Req。
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            login(ctx);
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
                                .setSUserId(ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get())
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
                    case "gcreate": {
                        GroupCreateReq groupCreateReq = Message.newBuilder()
                                .getGroupCreateReq().newBuilderForType()
                                .setUserId(Integer.parseInt(s[1]))
                                .setGroupId(Integer.parseInt(s[2]))
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_CREATE_REQ)
                                .setGroupCreateReq(groupCreateReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gjoin":{
                        GroupJoinReq joinReq = Message.newBuilder()
                                .getGroupJoinReq().newBuilderForType()
                                .setUserId(Integer.parseInt(s[1]))
                                .setJoinId(Integer.parseInt(s[2]))
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_JOIN_REQ)
                                .setGroupJoinReq(joinReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gquit":{
                        GroupQuitReq groupQuitReq = Message.newBuilder()
                                .getGroupQuitReq().newBuilderForType()
                                .setUserId(Integer.parseInt(s[1]))
                                .setGroupId(Integer.parseInt(s[2]))
                                .build();

                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_QUIT_REQ)
                                .setGroupQuitReq(groupQuitReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "quit": {
                        ctx.close();
                        System.out.println("您已退出聊天室！");
                        return;
                    }
                }
            }
        }, "waitForSelection").start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println("连接:" + ctx.channel() + " 出现异常: " + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(null);

        super.channelInactive(ctx);
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
        System.out.println(">>>>>>>发送消息: send [userId] [message]");
        System.out.println(">>>>>>>创建聊天组: gcreate [userId] [groupId]");
        System.out.println(">>>>>>>加入聊天组: gjoin [userId] [groupId]");
        System.out.println(">>>>>>>向聊天组内发消息: gsend [groupId] [message]");
        System.out.println(">>>>>>>查询加入的聊天组: gquery [userId]");
        System.out.println(">>>>>>>查询聊天组内的成员: gquerymen [groupId]");
        System.out.println(">>>>>>>离开聊天组: gquit [userId] [groupId]");
        System.out.println(">>>>>>>quit");
    }
}
