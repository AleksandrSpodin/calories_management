package com.spodin.calories.service;


import com.spodin.calories.model.User;
import com.spodin.calories.util.exception.NotFoundException;
import com.spodin.calories.to.UserTo;

import java.util.List;

public interface UserService {

    User create(User user);

    void delete(int id) throws NotFoundException;

    User get(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    void update(User user);

    void update(UserTo user);

    List<User> getAll();

    void enable(int id, boolean enable);

    User getWithMeals(int id);
}