package shenzhen.teamway.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shenzhen.teamway.protobuf.MessageProtobuf;
import shenzhen.teamway.ssl.ContextSSLFactory;
import shenzhen.teamway.utils.PropertiesUtils;

import javax.net.ssl.SSLEngine;


/**
 * @program: NettyServer
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2018-08-24 11:41
 **/
public class NettyServer {
    private int port;
    //单位是秒
    private int readTimeOut = Integer.parseInt(PropertiesUtils.getValue("readTimeOut"));
    public SocketChannel socketChannel;
    Logger log = LoggerFactory.getLogger(NettyServer.class);

    public NettyServer(int port) throws InterruptedException {
        this.port = port;
        bind();
    }

    private void bind() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        //保持长连接状态
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                ChannelPipeline p = socketChannel.pipeline();
                p.addLast(new ProtobufVarint32FrameDecoder());
                p.addLast(new ProtobufDecoder(MessageProtobuf.PDGMessage.getDefaultInstance()));
                p.addLast(new ProtobufVarint32LengthFieldPrepender());
                p.addLast(new ProtobufEncoder());
                //没有数据读取就关闭连接
                p.addLast(new ReadTimeoutHandler(readTimeOut));
                //自定义的handler
                p.addLast(new NettyServerSSLHandler());
                final SSLEngine engine = ContextSSLFactory.getSslContext().createSSLEngine();
                engine.setUseClientMode(false);
                engine.setNeedClientAuth(true);
                // TODO: 2018/8/28  添加ssl验证
                //添加ssl验证
              //  p.addFirst(new SslHandler(engine));
            }
        });
        ChannelFuture f = bootstrap.bind(port).sync();
        if (f.isSuccess()) {
            log.info("server start---------------" + port);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            int serverPort = Integer.parseInt(PropertiesUtils.getValue("port"));
            try {
                new NettyServer(serverPort);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new NettyServer(Integer.parseInt(args[0]));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}