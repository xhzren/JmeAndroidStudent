package cn.xhzren.avg;

import cn.xhzren.avg.entity.DialogEnter;
import cn.xhzren.avg.entity.EndingEnter;
import cn.xhzren.test.gui.DialogDemoState;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BranchDialogHelper {
    //分支对话内容列表
    public static JSONArray branchDialogList = new JSONArray();
    //当前分支最终要到达的 对话id
    public static String jumpId = "";
    public static int jumpIndex;
    //当前分支对话 实体
    public static DialogEnter currentBranchDialog = null;
    //当前分支对话数据索引
    public static int currentBranchIndex = 0;
    //当前分支对话 id
    public static String currentBranchId = "";

    /**
     * 初始化分支对话数据
     */
    public static void init() {
        currentBranchId = branchDialogList.getJSONObject(0).getString("id");
        currentBranchDialog = JSON.parseObject(branchDialogList.getJSONObject(currentBranchIndex).toJSONString(),
                DialogEnter.class);
        currentBranchIndex = currentBranchDialog.getIndex();
    }

    /**
     * 获取下一个对话内容
     * @return 可能是文本,选项
     */
    public static JSONObject nextDialog() {
        if(currentBranchDialog == null) {
            init();
            return JSON.parseObject(JSONObject.toJSONString(currentBranchDialog));
        }

        //只能是文本类型的对话才走这个逻辑
        if("text".equals(currentBranchDialog.getType()) && currentBranchDialog.getCurrentIndex() < currentBranchDialog.getContent().size() - 1) {
            currentBranchDialog.setCurrentIndex(currentBranchDialog.getCurrentIndex()+1);
            return  JSON.parseObject(JSONObject.toJSONString(currentBranchDialog));
        }

        //如果是结局
        if("ending".equals(currentBranchDialog.getType())) {
            String endingId = currentBranchDialog.getJump();
            EndingEnter res = DialogHelper.endingList.stream().filter((e)->{
                return endingId.equals(e.getId());
            }).findFirst().orElse(null);
            return JSON.parseObject(JSONObject.toJSONString(res));
        }

        //如果所有对话已经读取完了, 就跳转到相应的对话
        if(currentBranchIndex == branchDialogList.size() - 1) {
            DialogHelper.currentIndex = jumpIndex;
            DialogHelper.currentId = DialogHelper.dialogList.getJSONObject(DialogHelper.currentIndex).getString("id");
            //如果是文本, 在本类中存下文本对话的数据
            if("text".equals(DialogHelper.dialogList.getJSONObject(DialogHelper.currentIndex).getString("type"))) {
                DialogHelper.currentDialog = JSON.parseObject(DialogHelper.dialogList.getJSONObject(DialogHelper.currentIndex).toJSONString(),
                        DialogEnter.class);
            }
            DialogDemoState.storyType = 1;
            return DialogHelper.dialogList.getJSONObject(DialogHelper.currentIndex);
        }


        //索引下移, 以获取下一个对话
        currentBranchIndex++;
        currentBranchId = branchDialogList.getJSONObject(currentBranchIndex).getString("id");
        //如果是文本, 在本类中存下文本对话的数据
        if("text".equals(branchDialogList.getJSONObject(currentBranchIndex).getString("type"))) {
            currentBranchDialog = JSON.parseObject(branchDialogList.getJSONObject(currentBranchIndex).toJSONString(),
                    DialogEnter.class);
        }
        return branchDialogList.getJSONObject(currentBranchIndex);
    }
}
