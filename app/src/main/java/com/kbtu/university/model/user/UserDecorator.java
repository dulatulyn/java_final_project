package com.kbtu.university.model.user;

import com.kbtu.university.model.enums.RoleEnum;

public abstract class UserDecorator extends User {

    private static final long serialVersionUID = 1L;

    protected User wrappedUser;

    public UserDecorator(User wrappedUser) {
        super(wrappedUser.getId(), wrappedUser.getLogin(), wrappedUser.getPasswordHash());
        this.wrappedUser = wrappedUser;
    }

    public User getWrappedUser() {
        return wrappedUser;
    }

    @Override
    public abstract RoleEnum getRole();
}
