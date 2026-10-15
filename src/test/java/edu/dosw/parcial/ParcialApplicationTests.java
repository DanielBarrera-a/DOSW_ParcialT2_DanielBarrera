package edu.dosw.parcial;

import edu.dosw.parcial.main.ParcialApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Se indica la clase principal porque está en el subpaquete main
@SpringBootTest(classes = ParcialApplication.class,
		properties = "jwt.secret=dGVzdHNlY3JldHRlc3RzZWNyZXR0ZXN0c2VjcmV0dGVzdHNlY3JldA==")
class ParcialApplicationTests {

	@Test
	void contextLoads() {
	}

}
