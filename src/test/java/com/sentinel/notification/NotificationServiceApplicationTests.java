package com.sentinel.notification;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Test d'intégration — nécessite PostgreSQL et Redis (lancer docker-compose avant)")
class NotificationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
