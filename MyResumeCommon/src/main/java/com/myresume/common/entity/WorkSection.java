package com.myresume.common.entity;

import com.myresume.common.entity.customvalidation.RichTextEditorMaxLength;
import com.myresume.common.entity.customvalidation.RichTextEditorMinLength;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity
@Table(name="work_section")
public class WorkSection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String jobName;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String company;

    @Size(min = 3, message = "- at least 3 characters")
    @Column(length = 128, nullable = false)
    private String webSite;


    
    @Column(length = 128, nullable = false)
    private Date fromDate;


    @Column(length = 128)
    private Date toDate;

    @RichTextEditorMinLength(message="- at least 3 characters")
    @RichTextEditorMaxLength(message="- max 12000 characters")
    @Column(length = 12000, nullable = false,name="job_desc")
    private String jobDesc;

    private Integer user_id;

    private boolean isCurrentJob;

    private boolean enabled;

    private String dateDiff;

    private int daysDiff;

    @Column(length = 64)
    private String companyLogoPhoto;

    public WorkSection() {
    }

    public WorkSection(@Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String jobName, @Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String company, @Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String webSite,Date fromDate, Date toDate, String jobDesc, Integer user_id, boolean isCurrentJob, boolean enabled, String dateDiff,int daysDiff) {
        this.jobName = jobName;
        this.company = company;
        this.webSite = webSite;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.jobDesc = jobDesc;
        this.user_id = user_id;
        this.isCurrentJob = isCurrentJob;
        this.enabled = enabled;
        this.dateDiff = dateDiff;
        this.daysDiff = daysDiff;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
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

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public boolean isCurrentJob() {
        return isCurrentJob;
    }

    public void setCurrentJob(boolean currentJob) {
        isCurrentJob = currentJob;
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

    public String getCompanyLogoPhoto() {
        return companyLogoPhoto;
    }

    public void setCompanyLogoPhoto(String companyLogoPhoto) {
        this.companyLogoPhoto = companyLogoPhoto;
    }

    public int getDaysDiff() {
        return daysDiff;
    }

    public void setDaysDiff(int daysDiff) {
        this.daysDiff = daysDiff;
    }

    @Override
    public String toString() {
        return "WorkSection{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", company='" + company + '\'' +
                ", webSite='" + webSite + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", jobDesc='" + jobDesc + '\'' +
                ", user_id=" + user_id +
                ", isCurrentJob=" + isCurrentJob +
                ", enabled=" + enabled +
                ", dateDiff=" + dateDiff +
                ", daysDiff=" + daysDiff +
                '}';
    }


    @Transient
    public String getCompanyLogoImagePath() {
        if(id==null||companyLogoPhoto ==null) return "/images/pic.png";

        return "/user_company_logos/"+this.id +"/" +this.companyLogoPhoto;
    }


}
