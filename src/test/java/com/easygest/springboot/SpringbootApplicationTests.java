package com.easygest.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"EASYGEST_SECURITY_USERNAME=test-user",
		"EASYGEST_SECURITY_PASSWORD=test-password"
})
class SpringbootApplicationTests {

	@Test
	void contextLoads() {
	}

}
