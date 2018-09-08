package shenzhen.teamway.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shenzhen.teamway.protobuf.AlarmMessage;

import java.util.UUID;


/**
 * @program: NettyServer
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2018-08-24 11:22
 **/
public class NettyServerSSLHandler extends SimpleChannelInboundHandler<AlarmMessage.BaseData> {
    Logger log = LoggerFactory.getLogger(NettyServerSSLHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("服务端检查到客户端失效的channel");
    }


    protected void messageReceived(ChannelHandlerContext ctx, AlarmMessage.BaseData message) {
        log.info("messageReceived=================================");
        final AlarmMessage.BaseData.Builder basebuilder = AlarmMessage.BaseData.newBuilder();
        final AlarmMessage.PDGHeader.Builder pdgBuilder = AlarmMessage.PDGHeader.newBuilder();
        final AlarmMessage.ResponseMessage.Builder resBuilder = AlarmMessage.ResponseMessage.newBuilder();
        final AlarmMessage.ReportAlarm.Builder alarmBuilder = AlarmMessage.ReportAlarm.newBuilder();
        final AlarmMessage.ReportSensorData.Builder reportBuilder = AlarmMessage.ReportSensorData.newBuilder();
        final AlarmMessage.RegisterRsp.Builder regRspBuilder = AlarmMessage.RegisterRsp.newBuilder();
        final AlarmMessage.HeartbeatRsp.Builder heartRsp = AlarmMessage.HeartbeatRsp.newBuilder();
        //创建随机字符串
        String uuid = UUID.randomUUID().toString();
        final AlarmMessage.BaseData.CommandType type = message.getType();
        if (type == AlarmMessage.BaseData.CommandType.REGISTER) {
            final AlarmMessage.Register register = message.getRegister();
            final String password = register.getPassword();
            // TODO: 2018/9/5 服务端查数据库比对密码
            if (password.equals("Teamway123456")) {

                pdgBuilder.setCommand(AlarmMessage.BaseData.CommandType.REGISTER_RSP.getNumber())
                        .setSession(uuid).setFromAddr(message.getRegister().getHeader().getFromAddr())
                        .setSeq(message.getRegister().getHeader().getSeq()).setStationCode(message.getRegister().getHeader().getStationCode());
                resBuilder.setResult(true).setMessage("register success");
                regRspBuilder.setHeader(pdgBuilder).setResponse(resBuilder);
                basebuilder.setRegisterRsp(regRspBuilder).setType(AlarmMessage.BaseData.CommandType.REGISTER_RSP);

            } else {
                resBuilder.setResult(false);
                resBuilder.setMessage("register error");
            }
            ctx.writeAndFlush(basebuilder.build());
        } else if (type == AlarmMessage.BaseData.CommandType.CATALOG) {
            final AlarmMessage.Catalog catalog = message.getCatalog();
            final AlarmMessage.PDGHeader pdgHeader = message.getCatalog().getHeader();
            pdgBuilder.setCommand(AlarmMessage.BaseData.CommandType.CATALOG_RSP.getNumber())
                    .setSession(pdgHeader.getSession()).setFromAddr(pdgHeader.getFromAddr())
                    .setSeq(pdgHeader.getSeq()).setStationCode(pdgHeader.getStationCode());
            resBuilder.setResult(true).setMessage("catalog success");
            regRspBuilder.setHeader(pdgBuilder).setResponse(resBuilder);
            basebuilder.setRegisterRsp(regRspBuilder).setType(AlarmMessage.BaseData.CommandType.REGISTER_RSP);
            System.out.println(catalog.toString());
            ctx.writeAndFlush(basebuilder.build());
        } else if (type == AlarmMessage.BaseData.CommandType.HEARTBEAT) {
            pdgBuilder.setCommand(20004).setSession(uuid).setFromAddr(message.getPDGHeader().getFromAddr()).setSeq(message.getPDGHeader().getSeq());
            heartRsp.setHeader(pdgBuilder);
            basebuilder.setHeartbeatRsp(heartRsp).setType(AlarmMessage.BaseData.CommandType.HEARTBEAT_RSP);
            System.out.println("走心跳");
            ctx.writeAndFlush(basebuilder.build());
        }
        //switch (type) {
        //    case 20001:
        //        //获取注册对象
        //        final AlarmMessage.Register register = message.getRegister();
        //        final String password = register.getPassword();
        //        if (password.equals("123456")) {
        //            // TODO: 2018/9/3 pdgheader少一个编码
        //            pdgBuilder.setCommand(20002).setSession(uuid).setFromAddr(message.getPDGHeader().getFromAddr()).setSeq(message.getPDGHeader().getSeq());
        //            resBuilder.setResult(true);
        //            regRspBuilder.setHeader(pdgBuilder).setResponse(resBuilder);
        //            basebuilder.setRegisterRsp(regRspbuilder);
        //            basebuilder.setPDGHeader(pdgBuilder);
        //        } else {
        //            resBuilder.setResult(false);
        //            resBuilder.setMessage("密码不正确");
        //        }
        //        ctx.writeAndFlush(basebuilder.build());
        //        break;
        //    case 20003:
        //        //pong
        //
        //        basebuilder.setPDGHeader(pdgBuilder);
        //        ctx.writeAndFlush(basebuilder.build());
        //        break;
        //    case 20011:
        //        //采集数据列表
        //        pdgBuilder.setCommand(20012).setSession(uuid).setFromAddr(message.getPDGHeader().getFromAddr()).setSeq(message.getPDGHeader().getSeq());
        //        if (message.getPDGHeader().getSession() == null) {
        //            log.info("未登录");
        //            resBuilder.setResult(false).setMessage("未登录");
        //        } else {
        //            resBuilder.setResult(true);
        //        }
        //        basebuilder.setResponseMessage(resBuilder).setPDGHeader(pdgBuilder);
        //        ctx.writeAndFlush(basebuilder.build());
        //        //获取上报采集数据列表的数据
        //        message.getReportSensorData();
        //        break;
        //    case 20013:
        //        //告警上报
        //        pdgBuilder.setCommand(20014).setSession(uuid).setFromAddr(message.getPDGHeader().getFromAddr()).setSeq(message.getPDGHeader().getSeq());
        //        if (message.getPDGHeader().getSession() == null) {
        //            log.info("未登录");
        //            resBuilder.setResult(false).setMessage("未登录");
        //        } else {
        //            resBuilder.setResult(true);
        //        }
        //        basebuilder.setResponseMessage(resBuilder).setPDGHeader(pdgBuilder);
        //        ctx.writeAndFlush(basebuilder.build());
        //        //获取告警的东西
        //        final AlarmMessage.ReportAlarm reportAlarm = message.getReportAlarm();
        // }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}