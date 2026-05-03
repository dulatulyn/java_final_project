package com.kbtu.university.scheduling;

import com.kbtu.university.model.academic.Course;
import com.kbtu.university.model.academic.Lesson;
import com.kbtu.university.model.academic.Room;
import com.kbtu.university.model.user.Manager;
import com.kbtu.university.model.user.Teacher;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Scheduler {

    private static final LocalTime FIRST_PAIR_START = LocalTime.of(9, 0);
    private static final int PAIR_MINUTES = 90;
    private static final int UNPLACED_PENALTY = 1000;
    private static final int VACANCY_PENALTY = 10;

    private Scheduler() {}

    public static List<Constraint> defaultConstraints() {
        List<Constraint> list = new ArrayList<>();
        list.add(new CapacityConstraint());
        list.add(new RoomTypeConstraint());
        list.add(new RoomFreeConstraint());
        list.add(new StudentClashConstraint());
        return list;
    }

    public static List<LocalDateTime> buildWeek(LocalDate weekStart, int pairsPerDay) {
        LocalDate monday = weekStart;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.plusDays(1);
        }
        List<LocalDateTime> slots = new ArrayList<>();
        for (int day = 0; day < 5; day++) {
            LocalDate date = monday.plusDays(day);
            for (int i = 0; i < pairsPerDay; i++) {
                LocalTime time = FIRST_PAIR_START.plusMinutes((long) i * PAIR_MINUTES);
                slots.add(LocalDateTime.of(date, time));
            }
        }
        return slots;
    }

    public static Schedule greedy(List<Course> courses, List<Room> rooms,
                                  List<LocalDateTime> slots, List<Constraint> constraints) {
        for (Course c : courses) {
            c.clearLessons();
        }
        List<Course> ordered = new ArrayList<>(courses);
        ordered.sort(new Comparator<Course>() {
            @Override
            public int compare(Course a, Course b) {
                return Integer.compare(b.getEnrolledCount(), a.getEnrolledCount());
            }
        });

        List<Lesson> placed = new ArrayList<>();
        List<Course> unplaced = new ArrayList<>();

        for (Course course : ordered) {
            Lesson lesson = tryPlace(course, rooms, slots, placed, constraints);
            if (lesson != null) {
                placed.add(lesson);
                course.addLesson(lesson);
            } else {
                unplaced.add(course);
            }
        }
        return new Schedule(placed, unplaced);
    }

    public static Schedule multiStart(List<Course> courses, List<Room> rooms,
                                      List<LocalDateTime> slots, List<Constraint> constraints,
                                      int iterations, long seed) {
        Random rng = new Random(seed);
        Schedule best = greedy(courses, rooms, slots, constraints);
        int bestScore = score(best);
        for (int i = 1; i < iterations; i++) {
            List<Course> shuffled = new ArrayList<>(courses);
            Collections.shuffle(shuffled, rng);
            Schedule candidate = greedy(shuffled, rooms, slots, constraints);
            int s = score(candidate);
            if (s < bestScore) {
                best = candidate;
                bestScore = s;
            }
        }
        return best;
    }

    public static int score(Schedule schedule) {
        int total = schedule.getUnplaced().size() * UNPLACED_PENALTY;
        total += schedule.countVacancies() * VACANCY_PENALTY;
        return total;
    }

    private static Lesson tryPlace(Course course, List<Room> rooms, List<LocalDateTime> slots,
                                   List<Lesson> placed, List<Constraint> constraints) {
        for (LocalDateTime slot : slots) {
            Teacher teacher = pickTeacher(course, slot, placed);
            if (teacher == null) continue;
            for (Room room : rooms) {
                if (allPass(course, room, slot, placed, constraints)) {
                    return new Lesson(course.getLessonType(), room, slot, course, teacher);
                }
            }
        }
        for (LocalDateTime slot : slots) {
            for (Room room : rooms) {
                if (allPass(course, room, slot, placed, constraints)) {
                    return new Lesson(course.getLessonType(), room, slot, course, null);
                }
            }
        }
        return null;
    }

    private static boolean allPass(Course course, Room room, LocalDateTime slot,
                                   List<Lesson> placed, List<Constraint> constraints) {
        for (Constraint c : constraints) {
            if (!c.check(course, room, slot, placed)) return false;
        }
        return true;
    }

    private static Teacher pickTeacher(Course course, LocalDateTime slot, List<Lesson> placed) {
        for (Teacher candidate : Manager.getInstructorsOf(course)) {
            boolean busy = false;
            for (Lesson l : placed) {
                if (l.getDateTime().equals(slot)
                        && l.getTeacher() != null
                        && l.getTeacher().getId().equals(candidate.getId())) {
                    busy = true;
                    break;
                }
            }
            if (!busy) return candidate;
        }
        return null;
    }
}
