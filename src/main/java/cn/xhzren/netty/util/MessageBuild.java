package cn.xhzren.netty.util;

import cn.xhzren.netty.entity.LocalAccountData;
import cn.xhzren.netty.entity.LoginProto.*;
import cn.xhzren.netty.entity.LoginProto.ConnectionMessage.*;
import cn.xhzren.netty.entity.LoginProto.RequestInfo.*;
import cn.xhzren.netty.entity.LoginProto.ReceiveInfo.*;
import com.alibaba.fastjson.JSONObject;

import java.util.Optional;

public class MessageBuild {

    public static ConnectionMessage.Builder requestInfoBuild(RequestType type) {
        ConnectionMessage.Builder builder = ConnectionMessage.newBuilder()
                .setDataType(ConnectionMessage.DataType.RequestInfo)
                .setRequestInfo(RequestInfo.newBuilder()
                        .setToken(getActiveToken().getToken()).setRequestType(type).build());
        return builder;
    }

    public static LocalAccountData getActiveToken() {
       return JsonUtils.localData.stream().filter((e)->
                e.isActive()
        ).filter((e)->
                !e.getToken().isEmpty()
        ).findFirst().get();
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
