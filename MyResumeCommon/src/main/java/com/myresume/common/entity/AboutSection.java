package com.myresume.common.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "about_section")
public class AboutSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String name;
    //
    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String header;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 1000, nullable = false)
    private String subHeader;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String currentJob;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 250, nullable = false)
    private String shortDesc;

//    @Size(min=3, message="- at least 3 characters")
//    @NotEmpty(message="- is required")
//    @Column(length = 128, nullable = false)
//    private String birthDay;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String webSite;

//    @Size(min=3, message="- at least 3 characters")
//    @NotEmpty(message="- is required")
//    @Column(length = 128, nullable = false)
//    private String phone;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String city;


//    @Digits(integer = 4, fraction = 2)
//    @Column(length = 128, nullable = false)
//    private int age;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String degree;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 250, nullable = false)
    private String footer;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String email;


    @Column(length = 1, nullable = false)
    private boolean currInd;


    @Column(length = 64)
    private String profilePhoto;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public boolean getCurrInd() {
        return currInd;
    }

    public void setCurrInd(boolean currInd) {
        this.currInd = currInd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header.trim();
    }

    public String getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(String subHeader) {
        this.subHeader = subHeader.trim();
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob.trim();
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc.trim();
    }

//    public String getBirthDay() {
//        return birthDay;
//    }
//
//    public void setBirthDay(String birthDay) {
//        this.birthDay = birthDay.trim();
//    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite.trim();
    }

//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone.trim();
//    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city.trim();
    }

//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree.trim();
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public AboutSection() {
    }

    public AboutSection(String name, String header, String subHeader, String currentJob, String shortDesc, String webSite, String city, String degree, String footer, String email, boolean currInd) {
        this.name = name;
        this.header = header;
        this.subHeader = subHeader;
        this.currentJob = currentJob;
        this.shortDesc = shortDesc;
//        this.birthDay = birthDay;
        this.webSite = webSite;
//        this.phone = phone;
        this.city = city;
//        this.age = age;
        this.degree = degree;
        this.footer = footer;
        this.email = email;
        this.currInd = currInd;
    }

    @Override
    public String toString() {
        return "About{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", header='" + header + '\'' +
                ", subHeader='" + subHeader + '\'' +
                ", currentJob='" + currentJob + '\'' +
                ", webSite='" + webSite + '\'' +
                ", city='" + city + '\'' +
                ", degree='" + degree + '\'' +
                ", footer='" + footer + '\'' +
                ", email='" + email + '\'' +
                ", currInd='" + currInd + '\'' +
                '}';
    }

    @Transient
    public String getProfilePhotoImagePath() {
        if (id == null || profilePhoto == null) return "/images/pic.png";

        return "/profile-photos/" + this.id + "/" + this.profilePhoto;
    }
}
