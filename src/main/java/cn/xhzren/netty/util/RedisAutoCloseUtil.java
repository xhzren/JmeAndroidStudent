package cn.xhzren.netty.util;

import cn.xhzren.netty.servers.RedisHelper;
import redis.clients.jedis.Jedis;

public class RedisAutoCloseUtil extends Jedis {
    Jedis jedis;

    public RedisAutoCloseUtil getInstnal() {
        jedis = RedisHelper.getJedis();
        this.client = jedis.getClient();
        return this;
    }

}
