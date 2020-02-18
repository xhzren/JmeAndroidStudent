package cn.xhzren.avg;

import cn.xhzren.avg.entity.DialogEnter;
import cn.xhzren.avg.entity.EndingEnter;
import cn.xhzren.test.gui.EventCommon;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对话 工具类
 */
public class DialogHelper {

    //章节内容
    public static JSONObject chapterContent = null;
    //对话内容列表
    public static JSONArray dialogList = new JSONArray();
    //结局列表
    public static List<EndingEnter> endingList = new ArrayList<>();
    //当前对话数据索引
    public static int currentIndex = 0;
    //当前对话 id
    public static String currentId = "";
    //当前对话 实体
    public static DialogEnter currentDialog = new DialogEnter();
    public static EndingEnter defaultEnding = new EndingEnter();


    /**
     * 初始化数据
     */
    public static void init() {
        dialogList = chapterContent.getJSONArray("dialogList");
        currentId = dialogList.getJSONObject(0).getString("id");
        currentDialog = JSON.parseObject(dialogList.getJSONObject(currentIndex).toJSONString(),
                DialogEnter.class);

        endingList = JSON.parseArray(chapterContent.getJSONArray("ending").toJSONString(), EndingEnter.class);
        endingList.stream().forEach((e) -> {
            if(e.getId().contains("default")) {
                defaultEnding = e;
            }
        });

    }

    /**
     * 获取下一个对话内容
     * @return 可能是文本,选项
     */
    public static JSONObject nextDialog() {
        //只能是文本类型的对话才走这个逻辑
        if("text".equals(currentDialog.getType()) && currentDialog.getCurrentIndex() < currentDialog.getContent().size() - 1) {
            currentDialog.setCurrentIndex(currentDialog.getCurrentIndex()+1);
            return  JSON.parseObject(JSONObject.toJSONString(currentDialog));
        }

        //如果是结局
            if("ending".equals(currentDialog.getType())) {
            String endingId = currentDialog.getJump();
            EndingEnter res = endingList.stream().filter((e)->{
                        return endingId.equals(e.getId());
                    }).findFirst().orElse(null);
            return JSON.parseObject(JSONObject.toJSONString(res));
        }

        //如果所有对话已经读取完了, 就返回个默认结局
        if(currentIndex == dialogList.size() - 1) {
            return JSON.parseObject(JSONObject.toJSONString(defaultEnding));
        }
        //索引下移, 以获取下一个对话
        currentIndex++;
        currentId = dialogList.getJSONObject(currentIndex).getString("id");

        //如果是文本, 在本类中存下文本对话的数据
        if("text".equals(dialogList.getJSONObject(currentIndex).getString("type"))) {
            currentDialog = JSON.parseObject(dialogList.getJSONObject(currentIndex).toJSONString(),
             DialogEnter.class);
        }
        return dialogList.getJSONObject(currentIndex);
    }

    public static EndingEnter getEndingById(String id) {
        return endingList.stream().filter((e)->{
            return id.equals(e.getId());
        }).findFirst().orElse(null);
    }

    /**
     * 回退到上一个对话内容
     */
    public static void backDialog() {
        currentIndex--;
        currentId = dialogList.getJSONObject(currentIndex).getString("id");
        if("text".equals(dialogList.getJSONObject(currentIndex).getString("type"))) {
            currentDialog = JSON.parseObject(dialogList.getJSONObject(currentIndex).toJSONString(),
                    DialogEnter.class);
        }
    }

    public static void resetDialogData() {
        if(chapterContent == null) {
            chapterContent = JSON.parseObject(FileHelper.readJsonData(Constant.chapterPath));
        }
        dialogList = null;
        endingList = null;
        currentIndex = 0;
        currentId = "";
        currentDialog = null;
        defaultEnding = null;

        init();
    }

}
