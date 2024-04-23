package cn.net.ssd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(value = "cn.net.ssd.mapper.*")
public class Springboot27SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot27SecurityApplication.class, args);
    }

}
