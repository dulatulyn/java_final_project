package com.kbtu.university.model.user;

import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.startup.Startup;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StartupFounderRole extends UserDecorator {

    private static final long serialVersionUID = 1L;

    private List<Startup> startups;
    private LocalDate becameFounderAt;

    public StartupFounderRole(User wrappedUser) {
        super(wrappedUser);
        this.startups = new ArrayList<>();
        this.becameFounderAt = LocalDate.now();
    }

    public Startup createStartup(String name, String description) {
        Startup s = DataStorage.getInstance().createStartup(getWrappedUser().getId(), name, description);
        startups.add(s);
        return s;
    }

    public boolean joinStartup(String startupId) {
        Startup s = DataStorage.getInstance().findStartupById(startupId);
        if (s == null) return false;
        boolean added = s.addMember(getWrappedUser().getId());
        if (added) {
            startups.add(s);
            DataStorage.getInstance().log("Founder " + getWrappedUser().getId() + " joined " + startupId);
        }
        return added;
    }

    public List<Startup> getStartups() {
        return startups;
    }

    public LocalDate getBecameFounderAt() {
        return becameFounderAt;
    }

    @Override
    public RoleEnum getRole() {
        return getWrappedUser().getRole();
    }
}
