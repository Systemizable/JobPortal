package me.josephsf.jobportaljosephsfeir.model;

import java.time.LocalDate;

/**
 * Model class representing an educational record for a candidate in the Job Portal system.
 * <p>
 * The Education class is an embedded document within the Candidate entity that captures
 * details about a candidate's educational background, including degrees, institutions,
 * fields of study, graduation dates, and academic performance.
 * </p>
 *
 * <p>This class is used to structure educational history information within a candidate's
 * profile. Multiple Education instances can be included in a Candidate document to represent
 * the complete educational background of a job seeker.</p>
 *
 * <p>Education records include:</p>
 * <ul>
 *   <li>Degree or certification earned</li>
 *   <li>Educational institution attended</li>
 *   <li>Field or major of study</li>
 *   <li>Graduation date</li>
 *   <li>Academic performance (GPA)</li>
 * </ul>
 *
 * <p>This information is used for candidate qualification evaluation, matching to job
 * requirements, and displaying educational background to recruiters.</p>
 *
 * @author Joseph Sfeir
 * @version 1.0
 * @since 2025-05-15
 * @see Candidate
 */
public class Education {
    /**
     * Name or type of degree or certification earned.
     * Example: "Bachelor of Science", "Master of Business Administration", etc.
     */
    private String degree;

    /**
     * Name of the educational institution attended.
     * Example: "Harvard University", "Massachusetts Institute of Technology", etc.
     */
    private String institution;

    /**
     * Field, major, or area of study.
     * Example: "Computer Science", "Business Administration", etc.
     */
    private String field;

    /**
     * Date when the degree was or will be awarded.
     */
    private LocalDate graduationDate;

    /**
     * Grade Point Average or equivalent academic performance metric.
     * Typically on a scale of 0.0 to 4.0 for US institutions.
     */
    private Double gpa;

    /**
     * Default constructor.
     */
    public Education() {}

    /**
     * Constructor with all education details.
     *
     * @param degree the name or type of degree earned
     * @param institution the name of the educational institution
     * @param field the field or major of study
     * @param graduationDate the date of graduation
     * @param gpa the Grade Point Average or equivalent metric
     */
    public Education(String degree, String institution, String field, LocalDate graduationDate, Double gpa) {
        this.degree = degree;
        this.institution = institution;
        this.field = field;
        this.graduationDate = graduationDate;
        this.gpa = gpa;
    }

    /**
     * Gets the degree or certification name.
     *
     * @return the degree name
     */
    public String getDegree() {
        return degree;
    }

    /**
     * Sets the degree or certification name.
     *
     * @param degree the degree name to set
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }

    /**
     * Gets the name of the educational institution.
     *
     * @return the institution name
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * Sets the name of the educational institution.
     *
     * @param institution the institution name to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * Gets the field or major of study.
     *
     * @return the field of study
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the field or major of study.
     *
     * @param field the field of study to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Gets the graduation date.
     *
     * @return the graduation date
     */
    public LocalDate getGraduationDate() {
        return graduationDate;
    }

    /**
     * Sets the graduation date.
     *
     * @param graduationDate the graduation date to set
     */
    public void setGraduationDate(LocalDate graduationDate) {
        this.graduationDate = graduationDate;
    }

    /**
     * Gets the Grade Point Average or equivalent metric.
     *
     * @return the GPA
     */
    public Double getGpa() {
        return gpa;
    }

    /**
     * Sets the Grade Point Average or equivalent metric.
     *
     * @param gpa the GPA to set
     */
    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
}