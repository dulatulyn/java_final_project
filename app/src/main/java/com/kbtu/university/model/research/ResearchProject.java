package com.kbtu.university.model.research;

import com.kbtu.university.exception.NotResearcherException;
import com.kbtu.university.model.user.ResearcherRole;
import com.kbtu.university.model.user.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String topic;
    private List<ResearcherRole> participants;
    private List<ResearchPaper> papers;
    private LocalDate startDate;
    private String status;

    public ResearchProject(String topic, LocalDate startDate, String status) {
        this.topic = topic;
        this.startDate = startDate;
        this.status = status;
        this.participants = new ArrayList<>();
        this.papers = new ArrayList<>();
    }

    public void addParticipant(User u) throws NotResearcherException {
        if (!(u instanceof ResearcherRole)) {
            throw new NotResearcherException(
                    "User " + u.getId() + " is not a ResearcherRole and cannot join project " + topic);
        }
        ResearcherRole r = (ResearcherRole) u;
        if (!participants.contains(r)) {
            participants.add(r);
        }
    }

    public void addPaper(ResearchPaper p) {
        if (!papers.contains(p)) {
            papers.add(p);
        }
    }

    public String getTopic() {
        return topic;
    }

    public List<ResearcherRole> getParticipants() {
        return participants;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
