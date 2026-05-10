package com.kbtu.university.model.startup;

import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.enums.StartupStatusEnum;
import com.kbtu.university.model.user.User;
import com.kbtu.university.model.user.UserDecorator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Startup extends UserDecorator {

    private static final long serialVersionUID = 1L;

    private String startupId;
    private String name;
    private String description;
    private List<String> memberIds;
    private StartupStatusEnum status;
    private LocalDate foundedDate;

    public Startup(User founder, String startupId, String name, String description) {
        super(founder);
        this.startupId = startupId;
        this.name = name;
        this.description = description;
        this.memberIds = new ArrayList<>();
        this.memberIds.add(founder.getId());
        this.status = StartupStatusEnum.IDEA;
        this.foundedDate = LocalDate.now();
    }

    public boolean addMember(String userId) {
        if (memberIds.contains(userId)) return false;
        memberIds.add(userId);
        return true;
    }

    public void advanceStatus(StartupStatusEnum newStatus) {
        this.status = newStatus;
    }

    public String getStartupId() {
        return startupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFounderId() {
        return getWrappedUser().getId();
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public StartupStatusEnum getStatus() {
        return status;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    @Override
    public RoleEnum getRole() {
        return getWrappedUser().getRole();
    }

    @Override
    public String toString() {
        return "Startup[" + startupId + " \"" + name + "\" founder=" + getFounderId()
                + " members=" + memberIds.size() + " status=" + status + "]";
    }
}
