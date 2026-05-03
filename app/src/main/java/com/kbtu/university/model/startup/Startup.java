package com.kbtu.university.model.startup;

import com.kbtu.university.model.enums.StartupStatusEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Startup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String founderId;
    private List<String> memberIds;
    private StartupStatusEnum status;
    private LocalDate foundedDate;

    public Startup(String id, String name, String description, String founderId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.founderId = founderId;
        this.memberIds = new ArrayList<>();
        this.memberIds.add(founderId);
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFounderId() {
        return founderId;
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
    public String toString() {
        return "Startup[" + id + " \"" + name + "\" founder=" + founderId
                + " members=" + memberIds.size() + " status=" + status + "]";
    }
}
