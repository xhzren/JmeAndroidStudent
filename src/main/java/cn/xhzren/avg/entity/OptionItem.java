package cn.xhzren.avg.entity;

import lombok.Data;

import java.util.List;

/**
 * 分支选项子实体
 */
@Data
public class OptionItem {
    private int jumpIndex;
    private String content;
    private String event;
    private List<DialogEnter> dialogList;
}
