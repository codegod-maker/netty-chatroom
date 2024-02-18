package com.xwm.service;

import com.xwm.beans.User;

import java.util.Collection;

public interface UserService {
    public Boolean saveUser(User user);

    public User queryUser(String username);

    public Collection<User> getUsers();

    public void quit(User user);
}
