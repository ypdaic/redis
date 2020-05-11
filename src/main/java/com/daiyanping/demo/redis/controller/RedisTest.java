package com.daiyanping.demo.redis.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 当进行reshard时，数据写入是没有问题的
     * @return
     */
    @PostMapping("/cluster/test/set")
    public JSONObject clusterTestSet() {
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String key = "foo" + i;
                redisTemplate.boundValueOps(key).set(i);

                log.info("插入的数据:{}", key);

            }
        }).start();
        return new JSONObject();
    }

    /**
     * 当进行reshard时，数据读取是没有问题的
     * @return
     */
    @PostMapping("/cluster/test/get")
    public JSONObject clusterTestGet() {
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String key = "foo" + i;
                Object o = redisTemplate.boundValueOps(key).get();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("线程被中断", e);
                } catch (Exception e) {
                    log.error("操作异常", e);
                }
                log.info("获取的数据:{}", o);

            }
        }).start();
        return new JSONObject();
    }
}
