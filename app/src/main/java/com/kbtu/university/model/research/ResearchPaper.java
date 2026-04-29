package com.kbtu.university.model.research;

import com.kbtu.university.model.user.ResearcherRole;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResearchPaper implements Comparable<ResearchPaper>, Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private List<ResearcherRole> authors;
    private String journal;
    private String doi;
    private int citations;
    private int pages;
    private LocalDate datePublished;
    private List<String> keywords;

    public ResearchPaper(String title, String journal, String doi, int citations, int pages,
                         LocalDate datePublished) {
        this.title = title;
        this.journal = journal;
        this.doi = doi;
        this.citations = citations;
        this.pages = pages;
        this.datePublished = datePublished;
        this.authors = new ArrayList<>();
        this.keywords = new ArrayList<>();
    }

    public void addAuthor(ResearcherRole a) {
        if (!authors.contains(a)) {
            authors.add(a);
        }
    }

    public void addKeyword(String k) {
        keywords.add(k);
    }

    @Override
    public int compareTo(ResearchPaper other) {
        return Integer.compare(other.citations, this.citations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper)) return false;
        return doi.equals(((ResearchPaper) o).doi);
    }

    @Override
    public int hashCode() {
        return doi.hashCode();
    }

    @Override
    public String toString() {
        return "Paper[" + doi + " \"" + title + "\" " + citations + " cites, " + pages + " pages, " + datePublished + "]";
    }

    public String getTitle() {
        return title;
    }

    public List<ResearcherRole> getAuthors() {
        return authors;
    }

    public String getJournal() {
        return journal;
    }

    public String getDoi() {
        return doi;
    }

    public int getCitations() {
        return citations;
    }

    public int getPages() {
        return pages;
    }

    public LocalDate getDatePublished() {
        return datePublished;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
