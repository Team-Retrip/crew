package com.retrip.crew.common;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import com.retrip.crew.application.in.CrewService;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.CrewRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
@TestInstance(PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseTest {

    private DatabaseCleaner databaseCleaner;
    @Autowired
    protected CrewRepository crewRepository;

    @Autowired
    protected CrewQueryRepository crewQueryRepository;

    @Autowired
    protected CrewService crewService;

    protected UUID 정수_ID = UUID.fromString("13c8ab91-76bc-4f70-93e9-89f1a65dc640");
    protected UUID 홍석_ID = UUID.fromString("bf97d20b-d1f7-46a9-8362-11b9fa02d67d");
    protected UUID 준호_ID = UUID.fromString("8b9b67fd-1d88-4b30-bfea-cd8f89fc10d9");
    protected UUID 지수_ID = UUID.fromString("de3b60d2-5672-464d-8769-bf5c9de5eaff");
    protected UUID 혁진_ID = UUID.fromString("42880aaf-4b97-4b0c-8a8a-72df4bb592f6");

    @BeforeAll
    void beforeAll(@Autowired JdbcTemplate jdbcTemplate) {
        this.databaseCleaner = new DatabaseCleaner(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        databaseCleaner.clean();
    }
}
