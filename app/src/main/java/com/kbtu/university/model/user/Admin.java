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

    public boolean changePassword(String userId, String newPassword) {
        User u = DataStorage.getInstance().findUserById(userId);
        if (u == null) return false;
        u.setPasswordHash(newPassword);
        DataStorage.getInstance().log("Admin " + this.getId() + " changed password for " + userId);
        return true;
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
