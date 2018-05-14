package com.spodin.calories.service.datajpa;

import com.spodin.calories.MealTestData;
import com.spodin.calories.Profiles;
import com.spodin.calories.model.User;
import com.spodin.calories.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import com.spodin.calories.service.AbstractJpaUserServiceTest;

import static com.spodin.calories.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractJpaUserServiceTest {
    @Test
    public void testGetWithMeals() throws Exception {
        User admin = service.getWithMeals(ADMIN_ID);
        assertMatch(admin, ADMIN);
        MealTestData.assertMatch(admin.getMeals(), MealTestData.ADMIN_MEAL2, MealTestData.ADMIN_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithMealsNotFound() throws Exception {
        service.getWithMeals(1);
    }
}