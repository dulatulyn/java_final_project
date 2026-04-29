package com.kbtu.university.model.user;

import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.storage.DataStorage;

import java.util.List;

public class Admin extends User {

    private static final long serialVersionUID = 1L;

    public Admin(String id, String login, String passwordHash) {
        super(id, login, passwordHash);
    }

    public void addUser(User u) {
        DataStorage.getInstance().addUser(u);
    }

    public void removeUser(String userId) {
        DataStorage.getInstance().removeUser(userId);
    }

    public void updateUser(User u) {
        DataStorage.getInstance().replaceUser(u);
    }

    public List<String> viewLog() {
        return DataStorage.getInstance().getLog();
    }

    public List<User> viewUsers() {
        return DataStorage.getInstance().getUsers();
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.ADMIN;
    }
}
