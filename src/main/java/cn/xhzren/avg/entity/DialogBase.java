package cn.xhzren.avg.entity;

import lombok.Data;

@Data
public class DialogBase {
    private String id;
    private String name;
    private String type;
    private int index;
    private int currentIndex = 0;
}
