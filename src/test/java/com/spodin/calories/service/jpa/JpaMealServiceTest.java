package com.spodin.calories.service.jpa;

import com.spodin.calories.Profiles;
import org.springframework.test.context.ActiveProfiles;
import com.spodin.calories.service.AbstractMealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {
}