package com.gbujak.nolege;

import com.gbujak.nolege.config.db.TestDatabaseConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestDatabaseConfig.class)
public abstract class BaseIntegrationTest {
}
