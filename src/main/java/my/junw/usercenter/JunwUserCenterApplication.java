package my.junw.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("my.junw.usercenter.dao")
public class JunwUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JunwUserCenterApplication.class, args);
    }

}
