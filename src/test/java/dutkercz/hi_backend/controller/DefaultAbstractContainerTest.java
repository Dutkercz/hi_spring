package dutkercz.hi_backend.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
@SpringBootTest
@ActiveProfiles("mysql")
public abstract class DefaultAbstractContainerTest {

    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("test");

    static{
        mysql.start();
    }
}
