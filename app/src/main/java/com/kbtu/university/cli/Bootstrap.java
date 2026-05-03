package com.kbtu.university.cli;

import com.kbtu.university.exception.CreditLimitException;
import com.kbtu.university.factory.UserFactory;
import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Room;
import com.kbtu.university.model.enums.LessonType;
import com.kbtu.university.model.enums.ManagerTypeEnum;
import com.kbtu.university.model.enums.RoomType;
import com.kbtu.university.model.enums.TitleEnum;
import com.kbtu.university.model.research.ResearchPaper;
import com.kbtu.university.model.research.ResearchPaperBuilder;
import com.kbtu.university.model.research.ResearchProject;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.RegistrarOfficer;
import com.kbtu.university.model.user.ResearcherRole;
import com.kbtu.university.model.user.Student;
import com.kbtu.university.model.user.Teacher;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;

public class Bootstrap {

    private Bootstrap() {}

    public static void seed() {
        DataStorage db = DataStorage.getInstance();
        if (!db.getUsers().isEmpty()) {
            return;
        }

        UserFactory.createAdmin("admin", "admin");

        Teacher ivanov = UserFactory.createTeacher(
                "ivanov", "pass", 800000, LocalDate.of(2015, 9, 1), "SITE", TitleEnum.ASSOCIATE_PROFESSOR);
        Teacher petrov = UserFactory.createTeacher(
                "petrov", "pass", 600000, LocalDate.of(2018, 9, 1), "SITE", TitleEnum.SENIOR_LECTOR);
        Teacher sidorova = UserFactory.createTeacher(
                "sidorova", "pass", 900000, LocalDate.of(2012, 9, 1), "SITE", TitleEnum.PROFESSOR);
        Teacher kim = UserFactory.createTeacher(
                "kim", "pass", 500000, LocalDate.of(2020, 9, 1), "SITE", TitleEnum.TUTOR);

        Manager dean = UserFactory.createManager(
                "dean", "pass", 1200000, LocalDate.of(2010, 9, 1), "SITE",
                TitleEnum.PROFESSOR, ManagerTypeEnum.DEAN);

        RegistrarOfficer registrar = UserFactory.createRegistrar(
                "registrar", "pass", 500000, LocalDate.of(2017, 9, 1), "SITE");

        ResearcherRole sidorovaResearcher = new ResearcherRole(sidorova, 12);
        db.addResearcher(sidorovaResearcher);

        ResearcherRole ivanovResearcher = new ResearcherRole(ivanov, 5);
        db.addResearcher(ivanovResearcher);

        Course oopL = new Course("OOP-L", "OOP Lecture", 3, 50, 3, LessonType.LECTURE, RoomType.LECTURE_HALL);
        Course oopP = new Course("OOP-P", "OOP Practice", 1, 20, 3, LessonType.PRACTICE, RoomType.LAB);
        Course mathL = new Course("MATH-L", "Mathematics", 3, 100, 1, LessonType.LECTURE, RoomType.LECTURE_HALL);
        Course dbL = new Course("DB-L", "Databases", 3, 60, 2, LessonType.LECTURE, RoomType.LECTURE_HALL);
        Course dbP = new Course("DB-P", "Databases Lab", 1, 20, 2, LessonType.PRACTICE, RoomType.LAB);
        Course netL = new Course("NET-L", "Networks", 3, 30, 3, LessonType.LECTURE, RoomType.LECTURE_HALL);
        Course netP = new Course("NET-P", "Networks Lab", 1, 20, 3, LessonType.PRACTICE, RoomType.LAB);
        Course philS = new Course("PHIL-S", "Philosophy Seminar", 2, 20, 1, LessonType.PRACTICE, RoomType.SEMINAR);

        for (Course c : new Course[]{oopL, oopP, mathL, dbL, dbP, netL, netP, philS}) {
            db.addCourse(c);
        }

        dean.assignTeacher(ivanov, oopL);
        dean.assignTeacher(petrov, oopL);
        dean.assignTeacher(petrov, oopP);
        dean.assignTeacher(ivanov, mathL);
        dean.assignTeacher(sidorova, dbL);
        dean.assignTeacher(sidorova, dbP);
        dean.assignTeacher(kim, netL);
        dean.assignTeacher(kim, netP);
        dean.assignTeacher(sidorova, philS);
        dean.assignTeacher(ivanov, philS);

        db.addRoom(new Room("R101", 30, RoomType.LECTURE_HALL));
        db.addRoom(new Room("R102", 50, RoomType.LECTURE_HALL));
        db.addRoom(new Room("R201", 100, RoomType.LECTURE_HALL));
        db.addRoom(new Room("L1", 20, RoomType.LAB));
        db.addRoom(new Room("L2", 20, RoomType.LAB));
        db.addRoom(new Room("S1", 15, RoomType.SEMINAR));

        Student first = UserFactory.createStudent("alice", "pass", "SITE", 1);
        Student second = UserFactory.createStudent("bob", "pass", "SITE", 2);
        Student third = UserFactory.createStudent("carol", "pass", "SITE", 3);
        Student fourth = UserFactory.createStudent("dmitry", "pass", "SITE", 4);

        registerSilently(first, mathL);
        registerSilently(first, philS);
        registerSilently(second, dbL);
        registerSilently(second, dbP);
        registerSilently(third, oopL);
        registerSilently(third, oopP);
        registerSilently(third, netL);
        registerSilently(fourth, oopL);
        registerSilently(fourth, netP);

        registrar.approveRegistration(first, mathL);
        registrar.approveRegistration(second, dbL);
        registrar.approveRegistration(third, oopL);

        java.time.LocalDateTime week1Mon9 = java.time.LocalDateTime.of(2026, 1, 5, 9, 0);
        ivanov.markAttendance(third, oopL, week1Mon9, com.kbtu.university.model.enums.AttendanceStatusEnum.PRESENT);
        ivanov.markAttendance(fourth, oopL, week1Mon9, com.kbtu.university.model.enums.AttendanceStatusEnum.LATE);
        sidorova.markAttendance(second, dbL, week1Mon9.plusHours(1).plusMinutes(30),
                com.kbtu.university.model.enums.AttendanceStatusEnum.ABSENT);

        third.asFounder().createStartup("EduPath", "Personalized learning paths for students");
        fourth.asFounder().createStartup("CampusBite", "Food delivery inside KBTU campus");

        ResearchPaper paper1 = new ResearchPaperBuilder()
                .title("Image Recognition with CNNs")
                .doi("10.1/img-cnn")
                .journal("IEEE Transactions on Neural Networks")
                .citations(420)
                .pages(12)
                .datePublished(LocalDate.of(2024, 6, 1))
                .addAuthor(sidorovaResearcher)
                .addKeyword("CNN")
                .addKeyword("image")
                .build();

        ResearchPaper paper2 = new ResearchPaperBuilder()
                .title("Fairness in Machine Learning")
                .doi("10.1/fair-ml")
                .journal("ACM Computing Surveys")
                .citations(85)
                .pages(20)
                .datePublished(LocalDate.of(2025, 2, 1))
                .addAuthor(ivanovResearcher)
                .addKeyword("fairness")
                .build();

        ResearchPaper paper3 = new ResearchPaperBuilder()
                .title("Edge Computing Architectures")
                .doi("10.1/edge-arch")
                .journal("IEEE Internet of Things")
                .citations(150)
                .pages(8)
                .datePublished(LocalDate.of(2025, 9, 1))
                .addAuthor(sidorovaResearcher)
                .addKeyword("edge")
                .build();

        sidorovaResearcher.addPaper(paper1);
        sidorovaResearcher.addPaper(paper3);
        ivanovResearcher.addPaper(paper2);

        db.addPaper(paper1);
        db.addPaper(paper2);
        db.addPaper(paper3);

        ResearchProject project = new ResearchProject(
                "AI Fairness for Education", LocalDate.of(2025, 9, 1), "active");
        db.addProject(project);

        db.subscribeToNews(first);
        db.subscribeToNews(second);
        db.subscribeToNews(third);
        db.subscribeToNews(fourth);
    }

    private static void registerSilently(Student s, Course c) {
        try {
            s.registerForCourse(c);
        } catch (CreditLimitException e) {
            DataStorage.getInstance().log("Skipped registration: " + e.getMessage());
        }
    }
}
