package cn.xhzren.netty.util;

import cn.xhzren.netty.entity.JsonItem;
import cn.xhzren.netty.entity.LoginProto;
import cn.xhzren.netty.entity.input.KeyMapping;
import cn.xhzren.netty.servers.RedisHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static String root = "";
    private static String rootJson = "Interface\\Conf\\root.json";
    private static String localDataJson = "Interface\\Conf\\Test\\LocalData.json";
    public static JSONObject localData;
    private static List<JsonItem> confItems = new ArrayList<>();
    public static List<KeyMapping> keyMappings = new ArrayList<>();

    static {
        try {
            root = new File("").getCanonicalPath() + "\\assets\\";
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initDefaultLoad() {
        confItems = JSONArray.parseArray(readJsonData(root + rootJson), JsonItem.class);
        keyMappings = JSONArray.parseArray(readJsonData(root + confItems.stream().filter((e)->{
            return Constancts.KEY_MAPPING_JSON.equals(e.getName());
        }).findFirst().orElse(new JsonItem()).getPath()),KeyMapping.class);
        localData = JSONArray.parseObject(readJsonData(root + localDataJson));
    }

    public static void main(String[] args) {

        LoginProto.ConnectionMessage message = LoginProto.ConnectionMessage.newBuilder()
                .setDataType(LoginProto.ConnectionMessage.DataType.ReceiveInfo)
                .setReceiveInfo(LoginProto.ReceiveInfo.newBuilder()
                        .setReceiveType(LoginProto.ReceiveInfo.ReceiveType.LOGIN_RECEIVE)
                        .setReceiveStatus(LoginProto.ReceiveInfo.ReceiveStatus.SUCCESS).build()).build();
        System.out.println(message.getReceiveInfo());
    }

    public synchronized static void writeLocalData() {
        writeJsonData(JSONObject.toJSONString(localData), root+localDataJson);
    }

    public static String readJsonData(String file) {
        BufferedReader reader = null;
        StringBuilder jsonStr = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            reader.lines().forEach((e)-> {
                jsonStr.append(e);
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr.toString();
    }

    public static void writeJsonData(String data, String recordPath) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(recordPath));
            writer.write(data);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
