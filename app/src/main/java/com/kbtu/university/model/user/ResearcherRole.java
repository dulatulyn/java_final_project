package com.kbtu.university.model.user;

import com.kbtu.university.exception.NotResearcherException;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.model.research.ResearchPaper;
import com.kbtu.university.model.research.ResearchProject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResearcherRole extends UserDecorator {

    private static final long serialVersionUID = 1L;

    private int hIndex;
    private List<ResearchPaper> papers;
    private List<ResearchProject> projects;

    public ResearcherRole(User wrappedUser, int hIndex) {
        super(wrappedUser);
        this.hIndex = hIndex;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
        sorted.sort(comparator);
        for (ResearchPaper p : sorted) {
            System.out.println(p);
        }
    }

    public int totalCitations() {
        int sum = 0;
        for (ResearchPaper p : papers) {
            sum += p.getCitations();
        }
        return sum;
    }

    public int getHIndex() {
        return hIndex;
    }

    public void addPaper(ResearchPaper p) {
        papers.add(p);
        p.addAuthor(this);
    }

    public void joinProject(ResearchProject rp) throws NotResearcherException {
        rp.addParticipant(this);
        projects.add(rp);
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public List<ResearchProject> getProjects() {
        return projects;
    }

    public void setHIndex(int hIndex) {
        this.hIndex = hIndex;
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.RESEARCHER;
    }
}
