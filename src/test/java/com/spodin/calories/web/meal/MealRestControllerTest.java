package com.spodin.calories.web.meal;

import com.spodin.calories.MealTestData;
import com.spodin.calories.TestUtil;
import com.spodin.calories.UserTestData;
import com.spodin.calories.model.AbstractBaseEntity;
import com.spodin.calories.model.Meal;
import com.spodin.calories.service.MealService;
import com.spodin.calories.util.MealsUtil;
import com.spodin.calories.web.AbstractControllerTest;
import com.spodin.calories.web.json.JsonUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.ADMIN_MEAL_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TestUtil.contentJson(MealTestData.ADMIN_MEAL1));
    }

    @Test
    public void testGetUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.MEAL1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.ADMIN_MEAL_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.MEAL1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isNoContent());
        MealTestData.assertMatch(service.getAll(AbstractBaseEntity.START_SEQ), MealTestData.MEAL6, MealTestData.MEAL5, MealTestData.MEAL4, MealTestData.MEAL3, MealTestData.MEAL2);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.ADMIN_MEAL_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = MealTestData.getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk());

        MealTestData.assertMatch(service.get(MealTestData.MEAL1_ID, AbstractBaseEntity.START_SEQ), updated);
    }

    @Test
    public void testCreate() throws Exception {
        Meal created = MealTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)));

        Meal returned = TestUtil.readFromJson(action, Meal.class);
        created.setId(returned.getId());

        MealTestData.assertMatch(returned, created);
        MealTestData.assertMatch(service.getAll(UserTestData.ADMIN_ID), MealTestData.ADMIN_MEAL2, created, MealTestData.ADMIN_MEAL1);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TestUtil.contentJson(MealsUtil.getWithExceeded(MealTestData.MEALS, UserTestData.USER.getCaloriesPerDay())));
    }

    @Test
    public void testFilter() throws Exception {
        mockMvc.perform(get(REST_URL + "filter")
                .param("startDate", "2015-05-30").param("startTime", "07:00")
                .param("endDate", "2015-05-31").param("endTime", "11:00")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(TestUtil.contentJsonArray(
                        MealsUtil.createWithExceed(MealTestData.MEAL4, true),
                        MealsUtil.createWithExceed(MealTestData.MEAL1, false)));
    }

    @Test
    public void testFilterAll() throws Exception {
        mockMvc.perform(get(REST_URL + "filter?startDate=&endTime=")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(TestUtil.contentJson(MealsUtil.getWithExceeded(Arrays.asList(MealTestData.MEAL6, MealTestData.MEAL5, MealTestData.MEAL4, MealTestData.MEAL3, MealTestData.MEAL2, MealTestData.MEAL1), UserTestData.USER.getCaloriesPerDay())));
    }
}