package com.kbtu.university.model.user;

import com.kbtu.university.model.enums.RoleEnum;

import java.io.Serializable;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String login;
    protected String passwordHash;
    protected String language;

    public User(String id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.language = "en";
    }

    public boolean authenticate(String login, String passwordHash) {
        return this.login.equals(login) && this.passwordHash.equals(passwordHash);
    }

    public void logout() {
        System.out.println("User " + id + " logged out");
    }

    public abstract RoleEnum getRole();

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getRole() + "[" + id + " " + login + "]";
    }
}
