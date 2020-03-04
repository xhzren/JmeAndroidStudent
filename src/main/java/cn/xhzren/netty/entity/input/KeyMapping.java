package cn.xhzren.netty.entity;

import lombok.Data;

import java.util.List;

@Data
public class KeyMapping {
    private String id;
    private String name;
    private boolean isUse;
    private List<MappingItem> actionMapping;
    private List<MappingItem> analogMapping;
}
