package com.fans;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringbootApplicationTests {
    @Resource(name = "httpClient")
    private HttpClient httpClient;

    @Test
    public void contextLoads() {
        try {
            log.info("--> " + EntityUtils.toString(httpClient.execute(new HttpGet("http://www.baidu.com")).getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

