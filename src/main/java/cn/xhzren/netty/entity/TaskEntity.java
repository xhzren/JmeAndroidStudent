package cn.xhzren.netty.entity;

import lombok.Data;

import java.util.List;

@Data
public class TaskEntity {
    private String id;
    private String name;
    private String content;
    private List<String> result;
    private boolean finish;
    private boolean currentTag;
    private float progress;
    private List<String> names;
    private List<TaskRangeEntity> rangeEntities;
}
