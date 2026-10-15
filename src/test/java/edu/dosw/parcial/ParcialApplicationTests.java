package edu.dosw.parcial;

import edu.dosw.parcial.main.ParcialApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ParcialApplication.class,
		properties = {"jwt.secret=dGVzdHNlY3JldHRlc3RzZWNyZXR0ZXN0c2VjcmV0dGVzdHNlY3JldA==",
                "app.catalogo.inicializar=false",
                "spring.data.mongodb.auto-index-creation=false"})
class ParcialApplicationTests {

	@Test
	void contextLoads() {
	}

}
