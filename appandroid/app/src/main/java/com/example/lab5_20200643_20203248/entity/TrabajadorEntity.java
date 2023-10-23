package com.example.lab5_20200643_20203248.entity;

public class TrabajadorEntity {
    private Integer employee_id;
    private String first_name;
    private String last_name;
    private String email;
    private Integer manager_id;
    private String employee_feedback;

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getManager_id() {
        return manager_id;
    }

    public void setManager_id(Integer manager_id) {
        this.manager_id = manager_id;
    }

    public String getEmployee_feedback() {
        return employee_feedback;
    }

    public void setEmployee_feedback(String employee_feedback) {
        this.employee_feedback = employee_feedback;
    }
}
