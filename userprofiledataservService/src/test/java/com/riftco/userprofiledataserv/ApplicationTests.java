package com.riftco.userprofiledataserv;

import com.riftco.userprofiledataserv.adapter.persistence.users.eventstore.EventSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.riftco.userprofiledataserv.adapter.persistence.users.eventstore.EventStore;

@SpringBootTest(properties = {
	"spring.data.mongodb.host=localhost",
	"spring.data.mongodb.port=27017",
	"spring.data.mongodb.database=test"
})
class ApplicationTests {
	
	@MockBean
	private EventStore eventStore;
	
	@MockBean
	private com.riftco.userprofiledataserv.application.port.out.SendUserEventToBroker sendUserEventToBroker;
	
	@MockBean
	private EventSerializer eventSerializer;

	@Test
	void contextLoads() {
	}

}
