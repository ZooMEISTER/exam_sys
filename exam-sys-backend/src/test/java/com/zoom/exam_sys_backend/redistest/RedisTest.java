package com.zoom.exam_sys_backend.redistest;

import com.alibaba.fastjson2.JSONObject;
import com.zoom.exam_sys_backend.exception.code.TouristResultCode;
import com.zoom.exam_sys_backend.pojo.vo.TouristLoginResultVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/1/22 20:18
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test() {
        TouristLoginResultVO touristLoginResultVO = new TouristLoginResultVO(TouristResultCode.TOURIST_LOGIN_FAIL_WRONG_PASSWORD, 0, "0", "NULL", "NULL", "NULL", "NULL", "NULL", "NULL", 0, 0);
        //redisTemplate.opsForValue().set(String.valueOf(touristLoginResultVO.getUserid()), JSONObject.toJSONString(touristLoginResultVO));
        System.out.println(redisTemplate.opsForValue().get(String.valueOf(touristLoginResultVO.getUserid())));
        System.out.println(JSONObject.parseObject(String.valueOf(redisTemplate.opsForValue().get(String.valueOf(touristLoginResultVO.getUserid()))), TouristLoginResultVO.class).toString());
    }
}
