package cn.xhzren.netty.entity;

import com.jme3.math.Vector2f;
import lombok.Data;

import java.util.List;

@Data
public class TaskRangeEntity {

    private Vector2f top;
    private Vector2f down;
    private Vector2f left;
    private Vector2f right;
}
