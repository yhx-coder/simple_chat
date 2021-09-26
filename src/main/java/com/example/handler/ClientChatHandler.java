package com.example.handler;

import com.example.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


/**
 * @author: ming
 * @date: 2021/8/27 18:16
 */
public class ClientChatHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger logger = LoggerFactory.getLogger(ClientChatHandler.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private Thread selectMenu;

    // 在这里接收消息，即处理各种 *Res
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        Message.MessageType messageType = msg.getMessageType();

        switch (messageType) {
            case MSG_RES: {
                System.out.println(msg.getMsgRes().getResponse());
                break;
            }
            case LOGIN_RES: {
                LoginRes loginRes = msg.getLoginRes();
                if (loginRes.getStatus() == LoginRes.LoginStatus.SUCCESS) {

                    // 有时间好好研究下通道属性，重点看属性在客户端、服务端间是独立的
                    ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(loginRes.getSUserId());
                    System.out.println(loginRes.getResponse());
                    latch.countDown();
                } else if (loginRes.getStatus() == LoginRes.LoginStatus.REMOTE) {
                    // 被人挤下登录状态
                    selectMenu.interrupt();

                    latch = new CountDownLatch(1);

                    System.out.println(loginRes.getResponse());
//                    logger.error(loginRes.getResponse());
                    login(ctx);

                } else {
                    // 用户名或密码错误
                    System.out.println(loginRes.getResponse());
                    loginInput(ctx);
                }
                break;
            }
            case MSG_RX: {
                MsgRX msgRX = msg.getMsgRX();
                System.out.println("用户" + msgRX.getSUsername() + "说: " + msgRX.getContent());
                break;
            }
            case GROUP_RES: {
                System.out.println(msg.getGroupRes().getReason());
                break;
            }
            case GROUP_JOINED_QUERY_RES: {
                GroupJoinedQueryRes groupQueryRes = msg.getGroupQueryRes();
                if (!groupQueryRes.getStatus()) {
                    System.out.println(groupQueryRes.getReason());
                } else {
                    List<Integer> groupIdList = groupQueryRes.getGroupIdList();
                    System.out.print("用户加入的群组有: ");
                    groupIdList.forEach(id -> {
                        System.out.print(id + " ");
                    });
                    System.out.println();
                }
                break;
            }
            case GROUP_MEMBER_QUERY_RES: {
                GroupMemberQueryRes groupMemberQueryRes = msg.getGroupMemberQueryRes();
                if (!groupMemberQueryRes.getStatus()) {
                    System.out.println(groupMemberQueryRes.getReason());
                } else {
                    List<String> userNameList = groupMemberQueryRes.getUsernameList();
                    System.out.print("群组中的用户有: ");
                    userNameList.forEach(userName -> {
                        System.out.print(userName + " ");
                    });
                    System.out.println();
                }
                break;
            }
            case GROUP_MSG_RX: {
                GroupMessageRX messageRX = msg.getGroupMessageRX();
                System.out.println("用户" + messageRX.getSUserId() + "说: " + messageRX.getMessage());
                break;
            }
        }
    }

    // 在这里发送消息，即构造各种 *Req。
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        login(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println("连接:" + ctx.channel() + " 出现异常: " + cause.getMessage());
        logger.error("客户端出现异常: " + cause);
        ctx.close();
        System.exit(0);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).set(null);
        super.channelInactive(ctx);
    }

    private void loginInput(ChannelHandlerContext ctx) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名: ");
//        logger.error("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();


        // 一定要分两段构建消息，否则要报错。因为要解析的是 Message 。 若一次构建则是 LoginReq 了。
        LoginReq req = Message.newBuilder()
                .getLoginReq().newBuilderForType()
                .setUsername(username)
                .setPassword(password)
                .build();

        Message loginReq = Message.newBuilder()
                .setMessageType(Message.MessageType.LOGIN_REQ)
                .setLoginReq(req)
                .build();

        ctx.channel().writeAndFlush(loginReq);
    }

    private void menu() {
        System.out.println("*******************************************");
        System.out.println(">>>>>>>发送消息: send [userName] [message]");
        System.out.println(">>>>>>>创建聊天组: gcreate [groupId]");
        System.out.println(">>>>>>>加入聊天组: gjoin [groupId]");
        System.out.println(">>>>>>>向聊天组内发消息: gsend [groupId] [message]");
        System.out.println(">>>>>>>查询加入的聊天组: gquery");
        System.out.println(">>>>>>>查询聊天组内的成员: gquerymen [groupId]");
        System.out.println(">>>>>>>离开聊天组: gquit [groupId]");
        System.out.println(">>>>>>>离开聊天室: quit");
        System.out.println("*******************************************");
    }

    private void login(ChannelHandlerContext ctx) {
        selectMenu = new Thread(() -> {
            try {
                loginInput(ctx);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 想解决指令输错的异常，还不用重启客户端，我该怎么办？？？？

            while (true) {

                Thread thread = Thread.currentThread();
                if (thread.isInterrupted()) {
                    System.out.println("您已下线，请重新登录。");
//                    logger.error("您已下线，请重新登录。");
                    break;

                }

                menu();

                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                String[] s = line.split(" ");
                switch (s[0]) {
                    case "send": {
                        MsgReq req = Message.newBuilder()
                                .getMsgReq().newBuilderForType()
                                .setDUsername(s[1])
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
                                .setUserId(ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get())
                                .setGroupId(Integer.parseInt(s[1]))
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_CREATE_REQ)
                                .setGroupCreateReq(groupCreateReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gjoin": {
                        GroupJoinReq joinReq = Message.newBuilder()
                                .getGroupJoinReq().newBuilderForType()
                                .setUserId(ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get())
                                .setJoinId(Integer.parseInt(s[1]))
                                .build();
                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_JOIN_REQ)
                                .setGroupJoinReq(joinReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gquit": {
                        GroupQuitReq groupQuitReq = Message.newBuilder()
                                .getGroupQuitReq().newBuilderForType()
                                .setUserId(ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get())
                                .setGroupId(Integer.parseInt(s[1]))
                                .build();

                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_QUIT_REQ)
                                .setGroupQuitReq(groupQuitReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gquery": {
                        GroupJoinedQueryReq groupJoinedQueryReq = Message.newBuilder()
                                .getGroupQueryReq().newBuilderForType()
                                .setUserId(ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get())
                                .build();

                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_JOINED_QUERY_REQ)
                                .setGroupQueryReq(groupJoinedQueryReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gquerymen": {
                        GroupMemberQueryReq memberQueryReq = Message.newBuilder()
                                .getGroupMemberQueryReq().newBuilderForType()
                                .setGroupId(Integer.parseInt(s[1]))
                                .build();

                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_MEMBER_QUERY_REQ)
                                .setGroupMemberQueryReq(memberQueryReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "gsend": {
                        GroupMessageReq groupMessageReq = Message.newBuilder()
                                .getGroupMessageReq().newBuilderForType()
                                .setSUserId(ctx.channel().attr(AttributeKey.<Integer>valueOf("userId")).get())
                                .setGroupId(Integer.parseInt(s[1]))
                                .setMessage(s[2])
                                .build();

                        Message message = Message.newBuilder()
                                .setMessageType(Message.MessageType.GROUP_MSG_REQ)
                                .setGroupMessageReq(groupMessageReq)
                                .build();
                        ctx.channel().writeAndFlush(message);
                        break;
                    }
                    case "quit": {
                        ctx.close();
                        System.out.println("您已退出聊天室！");
                        return;
                    }
                    case"":{
                        break;
                    }
                    default: {
                        System.out.println("输入指令有误，请重新输入。");
                        break;
                    }
                }
            }
        });

        // 刚查的，在外面捕获子线程的错误。
        selectMenu.setUncaughtExceptionHandler((t, e) -> {
            logger.error("客户端崩溃，请重启客户端！");
            ctx.channel().close();
        });

        selectMenu.start();
    }

}
