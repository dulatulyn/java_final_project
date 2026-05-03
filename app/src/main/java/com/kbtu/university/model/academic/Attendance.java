package com.kbtu.university.model.academic;

import com.kbtu.university.model.enums.AttendanceStatusEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentId;
    private String courseCode;
    private LocalDateTime lessonDateTime;
    private AttendanceStatusEnum status;
    private LocalDate recordedAt;

    public Attendance(String studentId, String courseCode,
                      LocalDateTime lessonDateTime, AttendanceStatusEnum status) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.lessonDateTime = lessonDateTime;
        this.status = status;
        this.recordedAt = LocalDate.now();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public LocalDateTime getLessonDateTime() {
        return lessonDateTime;
    }

    public AttendanceStatusEnum getStatus() {
        return status;
    }

    public LocalDate getRecordedAt() {
        return recordedAt;
    }

    @Override
    public String toString() {
        return "Attendance[" + studentId + " " + courseCode
                + " @" + lessonDateTime + " " + status + "]";
    }
}
