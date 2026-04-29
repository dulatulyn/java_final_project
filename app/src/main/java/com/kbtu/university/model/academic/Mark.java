package com.kbtu.university.model.academic;

import java.io.Serializable;

public class Mark implements Comparable<Mark>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MAX_ATTESTATION = 30;
    public static final int MAX_FINAL = 40;
    public static final int PASSING_TOTAL = 50;

    private int attestation1;
    private int attestation2;
    private int finalExam;
    private String studentId;
    private String courseCode;

    public Mark(String studentId, String courseCode, int attestation1, int attestation2, int finalExam) {
        if (attestation1 < 0 || attestation1 > MAX_ATTESTATION) {
            throw new IllegalArgumentException("attestation1 must be in 0.." + MAX_ATTESTATION);
        }
        if (attestation2 < 0 || attestation2 > MAX_ATTESTATION) {
            throw new IllegalArgumentException("attestation2 must be in 0.." + MAX_ATTESTATION);
        }
        if (finalExam < 0 || finalExam > MAX_FINAL) {
            throw new IllegalArgumentException("finalExam must be in 0.." + MAX_FINAL);
        }
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.attestation1 = attestation1;
        this.attestation2 = attestation2;
        this.finalExam = finalExam;
    }

    public int getTotal() {
        return attestation1 + attestation2 + finalExam;
    }

    public String getLetterGrade() {
        int total = getTotal();
        if (total >= 90) return "A";
        if (total >= 75) return "B";
        if (total >= 60) return "C";
        if (total >= 50) return "D";
        return "F";
    }

    public boolean isFail() {
        return getTotal() < PASSING_TOTAL;
    }

    @Override
    public int compareTo(Mark other) {
        return Integer.compare(this.getTotal(), other.getTotal());
    }

    public int getAttestation1() {
        return attestation1;
    }

    public int getAttestation2() {
        return attestation2;
    }

    public int getFinalExam() {
        return finalExam;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mark)) return false;
        Mark other = (Mark) o;
        return studentId.equals(other.studentId) && courseCode.equals(other.courseCode);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode() * 31 + courseCode.hashCode();
    }

    @Override
    public String toString() {
        return "Mark[" + studentId + "/" + courseCode + "=" + getTotal() + " " + getLetterGrade() + "]";
    }
}
