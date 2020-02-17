package cn.xhzren.avg;

import cn.xhzren.avg.entity.DialogEnter;
import cn.xhzren.test.gui.EventCommon;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * 对话 工具类
 */
public class DialogHelper {

    //章节内容
    public static JSONObject chapterContent = new JSONObject();
    //对话内容列表
    public static JSONArray dialogList = new JSONArray();
    //当前对话数据索引
    public static int currentIndex = 0;
    //当前对话 id
    public static String currentId = "";
    //当前对话 实体
    public static DialogEnter currentDialog = new DialogEnter();

    /**
     * 初始化数据
     */
    public static void init() {
        BufferedReader reader = null;
        StringBuilder jsonStr = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader("D:\\work\\niffey\\JmeAndroidStudent\\src\\main\\resources\\dialog\\one.json"));
            reader.lines().forEach((e)-> {
                jsonStr.append(e);
            });
        }catch (IOException e) {
            e.printStackTrace();
        }
        chapterContent = JSON.parseObject(jsonStr.toString());
        dialogList = chapterContent.getJSONArray("dialogList");
        currentId = dialogList.getJSONObject(0).getString("id");
        currentDialog = JSON.parseObject(dialogList.getJSONObject(currentIndex).toJSONString(),
                DialogEnter.class);
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
//            retur(currentDialog);
        }
        //如果所有对话已经读取完了, 就返回个null
        if(currentIndex == dialogList.size() - 1) {
            return null;
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

}
