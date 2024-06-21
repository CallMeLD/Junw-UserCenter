package my.junw.usercenter.service.Impl;

import jakarta.annotation.Resource;
import my.junw.usercenter.entity.UserEO;
import my.junw.usercenter.service.IUserEOService;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserEOServiceTest {

    @Resource
    private IUserEOService userService;

    @Test
    public void testAddUser(){
        UserEO user = new UserEO();
        user.setUsername("测试1");
        user.setUserAccount("admin1");
        user.setAvatarUrl("/头像");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123456");
        user.setEmail("123456@qq.com");
        user.setUserStatus(0);
        boolean result =  userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }



}