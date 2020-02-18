package cn.xhzren.avg.entity;

import lombok.Data;

@Data
public class ArchiveRecords {
    private String recordName;
    private String time;
    private String image;
    private DialogEnter currentDialog;
    private int recordIndex;
    private GameData data;
}
