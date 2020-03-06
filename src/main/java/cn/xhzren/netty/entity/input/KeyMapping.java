package cn.xhzren.netty.entity.input;

import lombok.Data;

import java.util.List;

@Data
public class KeyMapping {
    private String id;
    private String name;
    private boolean use;
    private List<MappingItem> actionMapping;
    private List<MappingItem> analogMapping;
}
