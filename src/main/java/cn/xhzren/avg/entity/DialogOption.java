package cn.xhzren.avg.entity;

import lombok.Data;

import java.util.List;

/**
 * 分支选项实体
 */
@Data
public class DialogOption extends DialogBase {
    private String jump;
    private List<OptionItem> optionItems;
}
