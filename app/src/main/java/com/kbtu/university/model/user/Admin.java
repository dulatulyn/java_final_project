package com.kbtu.university.model.user;

import com.kbtu.university.model.admin.LogEntry;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.util.List;

public class Admin extends Employee {

    private static final long serialVersionUID = 1L;

    public Admin(String id, String login, String passwordHash,
                 double salary, LocalDate hireDate, String school) {
        super(id, login, passwordHash, salary, hireDate, school);
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

    public List<LogEntry> viewLog() {
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
