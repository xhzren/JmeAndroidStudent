package cn.xhzren.netty.entity.input;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import lombok.Data;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.util.List;

@Data
public class MappingItem {
    private String name;
    private KeyMappingType type;
    private List<CodeItem> codes;
}
