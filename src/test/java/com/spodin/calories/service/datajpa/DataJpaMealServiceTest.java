package com.spodin.calories.service.datajpa;

import com.spodin.calories.MealTestData;
import com.spodin.calories.Profiles;
import com.spodin.calories.util.exception.NotFoundException;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import com.spodin.calories.UserTestData;
import com.spodin.calories.model.Meal;
import com.spodin.calories.service.AbstractMealServiceTest;

import static com.spodin.calories.UserTestData.ADMIN_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void testGetWithUser() throws Exception {
        Meal adminMeal = service.getWithUser(MealTestData.ADMIN_MEAL_ID, ADMIN_ID);
        MealTestData.assertMatch(adminMeal, MealTestData.ADMIN_MEAL1);
        UserTestData.assertMatch(adminMeal.getUser(), UserTestData.ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void testGetWithUserNotFound() throws Exception {
        service.getWithUser(MealTestData.MEAL1_ID, ADMIN_ID);
    }
}
