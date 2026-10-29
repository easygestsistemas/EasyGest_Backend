package com.easygest.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"SPRING_MONGODB_URI=mongodb://localhost:27017/EasyGest"
})
class SpringbootApplicationTests {

	@Test
	void contextLoads() {
	}

}
