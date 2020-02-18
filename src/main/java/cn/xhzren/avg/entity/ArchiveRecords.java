package cn.xhzren.avg.entity;

import lombok.Data;

@Data
public class ArchiveRecords {
    private String name;
    private String time;
    private String image;
    private String dialogId;
    private int dialogIndex;
    private int recordIndex;
    private GameData data;
}
