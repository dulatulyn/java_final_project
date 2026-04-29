package com.kbtu.university.model.research;

import com.kbtu.university.model.user.ResearcherRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResearchPaperBuilder {

    private String title;
    private String journal;
    private String doi;
    private int citations;
    private int pages;
    private LocalDate datePublished;
    private final List<ResearcherRole> authors = new ArrayList<>();
    private final List<String> keywords = new ArrayList<>();

    public ResearchPaperBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ResearchPaperBuilder journal(String journal) {
        this.journal = journal;
        return this;
    }

    public ResearchPaperBuilder doi(String doi) {
        this.doi = doi;
        return this;
    }

    public ResearchPaperBuilder citations(int citations) {
        this.citations = citations;
        return this;
    }

    public ResearchPaperBuilder pages(int pages) {
        this.pages = pages;
        return this;
    }

    public ResearchPaperBuilder datePublished(LocalDate datePublished) {
        this.datePublished = datePublished;
        return this;
    }

    public ResearchPaperBuilder addAuthor(ResearcherRole author) {
        authors.add(author);
        return this;
    }

    public ResearchPaperBuilder addKeyword(String keyword) {
        keywords.add(keyword);
        return this;
    }

    public ResearchPaper build() {
        if (doi == null || doi.isEmpty()) {
            throw new IllegalStateException("doi is required");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalStateException("title is required");
        }
        ResearchPaper paper = new ResearchPaper(title, journal, doi, citations, pages, datePublished);
        for (ResearcherRole a : authors) {
            paper.addAuthor(a);
        }
        for (String k : keywords) {
            paper.addKeyword(k);
        }
        return paper;
    }
}
