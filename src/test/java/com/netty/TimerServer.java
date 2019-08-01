package com.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TimerServer {

    public static void main(String[] args) {

        new TimerServer().bind(9898);

    }

    public void bind(int port){

        EventLoopGroup work = new NioEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup();

        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChildChannelHandler());



            /**服务器启动辅助类配置完成后，调用 bind 方法绑定监听端口，调用 sync 方法同步等待绑定操作完成*/
            ChannelFuture f = b.bind(port).sync();

            System.out.println(Thread.currentThread().getName() + ",服务器开始监听端口，等待客户端连接.........");
            /**下面会进行阻塞，等待服务器连接关闭之后 main 方法退出，程序结束
             *
             * */
            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel channel) throws Exception {

            channel.pipeline()
                    .addLast(new TimeServerHandler())
                    .addLast("logging", new LoggingHandler(LogLevel.INFO));

        }
    }

    private class TimeServerHandler extends ChannelHandlerAdapter{
        /**
         * 收到客户端消息，自动触发
         *
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            /**
             * 将 msg 转为 Netty 的 ByteBuf 对象，类似 JDK 中的 java.nio.ByteBuffer，不过 ButeBuf 功能更强，更灵活
             */
            ByteBuf buf = (ByteBuf) msg;
            /**readableBytes：获取缓冲区可读字节数,然后创建字节数组
             * 从而避免了像 java.nio.ByteBuffer 时，只能盲目的创建特定大小的字节数组，比如 1024
             * */
            byte[] reg = new byte[buf.readableBytes()];
            /**readBytes：将缓冲区字节数组复制到新建的 byte 数组中
             * 然后将字节数组转为字符串
             * */
            buf.readBytes(reg);
            String body = new String(reg, "UTF-8");
            System.out.println(Thread.currentThread().getName() + ",The server receive  order : " + body);

            /**回复消息
             * copiedBuffer：创建一个新的缓冲区，内容为里面的参数
             * 通过 ChannelHandlerContext 的 write 方法将消息异步发送给客户端
             * */
            String respMsg = "I am Server，消息接收 success!"+body;
            ByteBuf respByteBuf = Unpooled.copiedBuffer(respMsg.getBytes());
            ctx.write(respByteBuf);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            /**flush：将消息发送队列中的消息写入到 SocketChannel 中发送给对方，为了频繁的唤醒 Selector 进行消息发送
             * Netty 的 write 方法并不直接将消息写如 SocketChannel 中，调用 write 只是把待发送的消息放到发送缓存数组中，再通过调用 flush
             * 方法，将发送缓冲区的消息全部写入到 SocketChannel 中
             * */
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            /**当发生异常时，关闭 ChannelHandlerContext，释放和它相关联的句柄等资源 */
            ctx.close();
        }
    }
}

