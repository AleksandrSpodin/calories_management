package com.spodin.calories.service;

import com.spodin.calories.MealTestData;
import com.spodin.calories.model.Meal;
import com.spodin.calories.util.exception.NotFoundException;
import org.junit.Assume;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static com.spodin.calories.UserTestData.ADMIN_ID;
import static com.spodin.calories.UserTestData.USER_ID;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;

    @Test
    public void delete() throws Exception {
        service.delete(MealTestData.MEAL1_ID, USER_ID);
        MealTestData.assertMatch(service.getAll(USER_ID), MealTestData.MEAL6, MealTestData.MEAL5, MealTestData.MEAL4, MealTestData.MEAL3, MealTestData.MEAL2);
    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(MealTestData.MEAL1_ID, 1);
    }

    @Test
    public void create() throws Exception {
        Meal created = MealTestData.getCreated();
        service.create(created, USER_ID);
        MealTestData.assertMatch(service.getAll(USER_ID), created, MealTestData.MEAL6, MealTestData.MEAL5, MealTestData.MEAL4, MealTestData.MEAL3, MealTestData.MEAL2, MealTestData.MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(MealTestData.ADMIN_MEAL_ID, ADMIN_ID);
        MealTestData.assertMatch(actual, MealTestData.ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(MealTestData.MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        MealTestData.assertMatch(service.get(MealTestData.MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Not found entity with id=" + MealTestData.MEAL1_ID);
        service.update(MealTestData.MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        MealTestData.assertMatch(service.getAll(USER_ID), MealTestData.MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        MealTestData.assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MealTestData.MEAL3, MealTestData.MEAL2, MealTestData.MEAL1);
    }

    @Test
    public void testValidation() throws Exception {
        Assume.assumeTrue(isJpaBased());
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "  ", 300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, null, "Description", 300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 9), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 5001), USER_ID), ConstraintViolationException.class);
    }
}