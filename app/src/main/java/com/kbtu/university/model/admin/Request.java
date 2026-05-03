package com.kbtu.university.model.admin;

import com.kbtu.university.model.enums.RequestStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String submitterId;
    private String text;
    private RequestStatusEnum status;
    private String signedById;
    private LocalDateTime createdAt;

    public Request(String id, String submitterId, String text) {
        this.id = id;
        this.submitterId = submitterId;
        this.text = text;
        this.status = RequestStatusEnum.PENDING;
        this.signedById = null;
        this.createdAt = LocalDateTime.now();
    }

    public void sign(String signerId) {
        this.status = RequestStatusEnum.SIGNED;
        this.signedById = signerId;
    }

    public void reject(String signerId) {
        this.status = RequestStatusEnum.REJECTED;
        this.signedById = signerId;
    }

    public String getId() {
        return id;
    }

    public String getSubmitterId() {
        return submitterId;
    }

    public String getText() {
        return text;
    }

    public RequestStatusEnum getStatus() {
        return status;
    }

    public String getSignedById() {
        return signedById;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Request[" + id + " from=" + submitterId + " " + status
                + (signedById != null ? " by=" + signedById : "")
                + " text=\"" + text + "\"]";
    }
}
