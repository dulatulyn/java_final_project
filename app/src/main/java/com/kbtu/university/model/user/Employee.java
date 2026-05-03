package com.kbtu.university.model.user;

import com.kbtu.university.model.admin.Request;
import com.kbtu.university.model.enums.RoleEnum;
import com.kbtu.university.storage.DataStorage;

import java.time.LocalDate;

public class Employee extends User {

    private static final long serialVersionUID = 1L;

    protected double salary;
    protected LocalDate hireDate;
    protected String school;

    public Employee(String id, String login, String passwordHash,
                    double salary, LocalDate hireDate, String school) {
        super(id, login, passwordHash);
        this.salary = salary;
        this.hireDate = hireDate;
        this.school = school;
    }

    public String getInfo() {
        return "Employee[id=" + id + ", login=" + login + ", salary=" + salary + ", school=" + school + "]";
    }

    public void sendMessage(Employee to, String text) {
        System.out.println("[msg " + this.id + " -> " + to.getId() + "] " + text);
    }

    public Request submitRequest(String text) {
        return DataStorage.getInstance().submitRequest(this.id, text);
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getSchool() {
        return school;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public RoleEnum getRole() {
        return RoleEnum.TEACHER;
    }
}
