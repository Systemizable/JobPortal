package me.josephsf.jobportaljosephsfeir.model;

import java.time.LocalDate;

public class Education {
    private String degree;
    private String institution;
    private String field;
    private LocalDate graduationDate;
    private Double gpa;

    // Constructors
    public Education() {}

    public Education(String degree, String institution, String field, LocalDate graduationDate, Double gpa) {
        this.degree = degree;
        this.institution = institution;
        this.field = field;
        this.graduationDate = graduationDate;
        this.gpa = gpa;
    }

    // Getters and Setters
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public LocalDate getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(LocalDate graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
}