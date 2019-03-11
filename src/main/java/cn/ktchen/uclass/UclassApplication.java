package cn.ktchen.uclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

@SpringBootApplication
public class UclassApplication {

	public static void main(String[] args) {
		SpringApplication.run(UclassApplication.class, args);
	}

}
