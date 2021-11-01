package com.myresume.common.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity
@Table(name="education_section")
public class EducationSection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String institutionName;

    @Column(length = 128, nullable = false)
    private Date fromDate;

    @Column(length = 128)
    private Date toDate;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String programType;

    private Integer user_id;

    private boolean enabled;

    private String dateDiff;

    public EducationSection() {
    }

    public EducationSection(String institutionName, Date fromDate, Date toDate, String programType, Integer user_id, boolean enabled, String dateDiff) {
        this.institutionName = institutionName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.programType = programType;
        this.user_id = user_id;
        this.enabled = enabled;
        this.dateDiff = dateDiff;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDateDiff() {
        return dateDiff;
    }

    public void setDateDiff(String dateDiff) {
        this.dateDiff = dateDiff;
    }

    @Override
    public String toString() {
        return "EducationSection{" +
                "id=" + id +
                ", institutionName='" + institutionName + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", programType='" + programType + '\'' +
                ", user_id=" + user_id +
                ", enabled=" + enabled +
                ", dateDiff=" + dateDiff +
                '}';
    }
}

