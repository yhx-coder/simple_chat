package com.example;

import com.example.config.Config;
import com.example.handler.ServerChatHandler;
import com.example.message.Message;
import com.example.message.MessageProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author: ming
 * @date: 2021/8/26 16:46
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(Message.getDefaultInstance()));

                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());

                            // 服务器 50 秒没有收到消息就断开连接。
                            pipeline.addLast(new IdleStateHandler(50,0,0, TimeUnit.SECONDS));
                            pipeline.addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    if (evt instanceof IdleStateEvent){
                                        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                                        if (idleStateEvent.state() == IdleState.READER_IDLE){
                                            ctx.channel().close();
                                        }
                                    }
                                }
                            });

                            pipeline.addLast(new ServerChatHandler());
                        }
                    })
                    .bind(Config.getPort())
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}
