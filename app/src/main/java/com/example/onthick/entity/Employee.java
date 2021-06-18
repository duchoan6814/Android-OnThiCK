package com.example.onthick.entity;

public class Employee {
    private String id;
    private String fullName;
    private boolean sex;
    private double salary;

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sex=" + sex +
                ", salary=" + salary +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Employee() {
    }

    public Employee(String id, String fullName, boolean sex, double salary) {
        this.id = id;
        this.fullName = fullName;
        this.sex = sex;
        this.salary = salary;
    }
}
