package cn.xhzren.netty.entity;

import lombok.Data;

@Data
public class LocalAccountData {
    private String username;
    private String token;
    private boolean active;
}
