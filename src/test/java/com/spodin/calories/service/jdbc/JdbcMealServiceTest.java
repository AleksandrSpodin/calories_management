package com.spodin.calories.service.jdbc;

import com.spodin.calories.Profiles;
import org.springframework.test.context.ActiveProfiles;
import com.spodin.calories.service.AbstractMealServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
}