package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


public class TimeClient {

    public static void main(String[] args) {
        new Thread(new MyThread()).start();

    }

    static class MyThread implements Runnable{

        @Override
        public void run() {
            connect("127.0.0.1",9898);
        }

        public void connect(String host,int port){

            EventLoopGroup group = new NioEventLoopGroup();

            try {

                Bootstrap bootstrap = new Bootstrap();

                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<NioSocketChannel>() {

                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                nioSocketChannel.pipeline().addLast(new TimeClientHandler());
                            }

                        });

                ChannelFuture channelFuture = bootstrap.connect(host,port).sync();

                System.out.println(Thread.currentThread().getName() + ",客户端发起异步连接..........");

                channelFuture.channel().closeFuture().sync();


            }catch (Exception e){
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
        }
    }

    @Slf4j
    static class  TimeClientHandler extends ChannelHandlerAdapter{

        /**
         * 当客户端和服务端 TCP 链路建立成功之后，Netty 的 NIO 线程会调用 channelActive 方法
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String reqMsg = "我是客户端==================== " + Thread.currentThread().getName();
            byte[] reqMsgByte = reqMsg.getBytes("UTF-8");
            ByteBuf reqByteBuf = Unpooled.buffer(reqMsgByte.length);
            /**
             * writeBytes：将指定的源数组的数据传输到缓冲区
             * 调用 ChannelHandlerContext 的 writeAndFlush 方法将消息发送给服务器
             */
            reqByteBuf.writeBytes(reqMsgByte);
            ctx.writeAndFlush(reqByteBuf);
        }

        /**
         * 当服务端返回应答消息时，channelRead 方法被调用，从 Netty 的 ByteBuf 中读取并打印应答消息
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, "UTF-8");
            System.out.println(Thread.currentThread().getName() + ",Server return Message：" + body);
            ctx.close();
        }

        /**
         * 当发生异常时，打印异常 日志，释放客户端资源
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            /**释放资源*/
            log.warn("Unexpected exception from downstream : " + cause.getMessage());
            ctx.close();
        }
    }

}
