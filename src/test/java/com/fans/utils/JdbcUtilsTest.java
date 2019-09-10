package com.fans.utils;

import com.fans.singleton.proxy.LocalCacheProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JdbcUtilsTest {


    @Test
    public void main() {
        LocalCacheProxy instance = LocalCacheProxy.getInstance();
        instance.put("1", "111111");
        instance.put("2", "222222");
        instance.put("3", "333333");
        System.out.println(instance.getSize());
//        instance.invalidate("1");
//        instance.refreshAll();
//        Object all = instance.get("1");
//        System.out.println(all);
//        System.out.println(instance.get("4") == null);
    }
}
