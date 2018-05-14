package com.spodin.calories.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import com.spodin.calories.service.AbstractUserServiceTest;

import static com.spodin.calories.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
}