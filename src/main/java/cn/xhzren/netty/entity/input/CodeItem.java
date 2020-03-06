package cn.xhzren.netty.entity.input;

import cn.xhzren.netty.entity.input.KeyMappingType;
import lombok.Data;

@Data
public class CodeItem {
    private KeyMappingType type;
    private String code;
}
