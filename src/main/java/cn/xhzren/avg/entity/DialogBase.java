package cn.xhzren.avg.entity;

import lombok.Data;

@Data
public class DialogBase {
    private String id;//对话id
    private String name;//人物姓名
    private String type;//对话类型 1主线2分支
    private EventEnter event;
    private String audio;//语音/音效
    private String jump;//如果是分支, 最终跳转的对话id
    private int jumpIndex;//跳转的对话索引
    private int index;//对话索引
    private int currentIndex = 0;//当前对话索引
}
