package cn.xhzren.netty.websocket;

import lombok.Data;

@Data
public class BaseEntity {
    String id;
    String creator;
    String updater;
    String status;
}
