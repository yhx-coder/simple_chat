package com.example;

import com.example.handler.ClientChatHandler;
import com.example.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: ming
 * @date: 2021/8/27 18:04
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(Message.getDefaultInstance()));
                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            // 连接检查
                            //  30s 没向服务器写数据, 用户事件触发。下面发送 ping 消息。
                            pipeline.addLast(new IdleStateHandler(0,30,0, TimeUnit.SECONDS));
                            pipeline.addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    if (evt instanceof IdleStateEvent){
                                        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                                        if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                                            Message message = Message.newBuilder().setMessageType(Message.MessageType.PING).build();
                                            ctx.channel().writeAndFlush(message);
                                        }
                                    }
                                }
                            });
                            pipeline.addLast(new ClientChatHandler());
                        }
                    })
                    .connect("localhost",8899)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
