package shenzhen.teamway.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shenzhen.teamway.protobuf.MessageProtobuf;

import java.util.UUID;


/**
 * @program: NettyServer
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2018-08-24 11:22
 **/
public class NettyServerSSLHandler extends SimpleChannelInboundHandler<MessageProtobuf.PDGMessage> {
    Logger log = LoggerFactory.getLogger(NettyServerSSLHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("服务端检查到客户端失效的channel");
    }


    protected void messageReceived(ChannelHandlerContext ctx, MessageProtobuf.PDGMessage message) {
        log.info("messageReceived=================================");
        final MessageProtobuf.PDGMessage.Builder basebuilder = MessageProtobuf.PDGMessage.newBuilder();
        final MessageProtobuf.PDGHeader.Builder pdgBuilder = MessageProtobuf.PDGHeader.newBuilder();
        final MessageProtobuf.ResponseMessage.Builder resBuilder = MessageProtobuf.ResponseMessage.newBuilder();
        final MessageProtobuf.ReportAlarm.Builder alarmBuilder = MessageProtobuf.ReportAlarm.newBuilder();
        final MessageProtobuf.ReportSensorData.Builder reportBuilder = MessageProtobuf.ReportSensorData.newBuilder();
        final MessageProtobuf.RegisterRsp.Builder regRspBuilder = MessageProtobuf.RegisterRsp.newBuilder();
        final MessageProtobuf.HeartbeatRsp.Builder heartRsp = MessageProtobuf.HeartbeatRsp.newBuilder();
        //创建随机字符串
        String uuid = UUID.randomUUID().toString();
        MessageProtobuf.CommandType type = message.getPDGHeader().getCommand();
        if (type == MessageProtobuf.CommandType.REGISTER) {
            final MessageProtobuf.Register register = message.getRegister();
            final String password = register.getPassword();
            // TODO: 2018/9/5 服务端查数据库比对密码
            if (password.equals("Teamway123456")) {

                pdgBuilder.setCommand(MessageProtobuf.CommandType.REGISTER_RSP)
                        .setSession(uuid).setFromAddr("sdfge")
                        .setSeq(2).setStationCode("dsf");
                resBuilder.setResult(true).setMessage("register success");
                regRspBuilder.setResponse(resBuilder);
                basebuilder.setRegisterRsp(regRspBuilder).setPDGHeader(pdgBuilder);

            } else {
                resBuilder.setResult(false);
                resBuilder.setMessage("register error");
            }
            ctx.writeAndFlush(basebuilder.build());
        } else if (type == MessageProtobuf.CommandType.CATALOG) {
            System.out.println("走到了设备列表");
            final MessageProtobuf.Catalog catalog = message.getCatalog();
            final MessageProtobuf.PDGHeader pdgHeader = message.getPDGHeader();
            pdgBuilder.setCommand(MessageProtobuf.CommandType.CATALOG_RSP)
                    .setSession(pdgHeader.getSession()).setFromAddr(pdgHeader.getFromAddr())
                    .setSeq(pdgHeader.getSeq()).setStationCode(pdgHeader.getStationCode());
            resBuilder.setResult(true).setMessage("catalog success");
            regRspBuilder.setResponse(resBuilder);
            basebuilder.setRegisterRsp(regRspBuilder).setPDGHeader(pdgBuilder);
            // System.out.println(catalog.toString());
            ctx.writeAndFlush(basebuilder.build());
        } else if (type == MessageProtobuf.CommandType.HEARTBEAT) {
            System.out.println("走心跳");
        } else if (type == MessageProtobuf.CommandType.REPORT_ALARM) {
            final MessageProtobuf.ReportAlarm alarm = message.getReportAlarm();
            System.out.println(alarm.toString());
            System.out.println("告警收到了");
        } else if (type == MessageProtobuf.CommandType.REPORT_ENVDATA) {
            System.out.println("上报采集值收到了");
            final String s = message.getReportSensorData().toString();
           System.out.println(s);
        } else if (type == MessageProtobuf.CommandType.SYSTEM_TIME) {
            System.out.println("对时间收到了");
        } else if (type == MessageProtobuf.CommandType.DEVICE_STATE) {
            System.out.println("设备状态上报收到了");
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}