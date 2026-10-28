package com.easygest.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"EASYGEST_SECURITY_USERNAME=test-user",
		"EASYGEST_SECURITY_PASSWORD=test-password",
		"SPRING_MONGODB_URI=mongodb://localhost:27017/EasyGest"
})
class SpringbootApplicationTests {

	@Test
	void contextLoads() {
	}

}
