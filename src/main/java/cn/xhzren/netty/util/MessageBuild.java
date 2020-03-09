package cn.xhzren.netty.util;

import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.entity.LoginProto.RequestInfo.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;

public class MessageBuild {

    public static ConnectionMessage.Builder requestInfoBuild(String token, RequestType type) {
        ConnectionMessage.Builder builder = ConnectionMessage.newBuilder()
                .setDataType(ConnectionMessage.DataType.RequestInfo)
                .setRequestInfo(RequestInfo.newBuilder()
                        .setToken(token).setRequestType(type).build());
        return builder;
    }

    public static ConnectionMessage.Builder receiveInfoBuild(ReceiveStatus status, ReceiveType type, String content) {
        ConnectionMessage.Builder builder = ConnectionMessage.newBuilder()
                .setDataType(DataType.ReceiveInfo)
                .setReceiveInfo(ReceiveInfo.newBuilder()
                        .setContent(content)
                        .setReceiveType(type).setReceiveStatus(status).build());
        return builder;
    }
}
