package cn.xhzren.avg.entity;

import lombok.Data;

import java.util.List;

@Data
public class DialogEnter extends DialogBase {
    private List<String> content;
}
