package com.myresume.common.entity;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
@Table(name="user_skills")
public class SkillsSection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String skillDescription;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String skillTitle;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String skillCategory;


    @Min(value = 1, message = "Value should not be less than 1")
    @Max(value = 100, message = "Value should not be greater than 10")
    @Column(length = 128, nullable = false)
    private int skillLevel;

    private Integer user_id;

    private boolean enabled;

    public SkillsSection() {

    }

    public SkillsSection(@Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String skillDescription, @Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String skillTitle, @Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String skillCategory, @Min(value = 1, message = "Value should not be less than 1") @Max(value = 100, message = "Value should not be greater than 10") int skillLevel, Integer user_id) {
        this.skillDescription = skillDescription;
        this.skillTitle = skillTitle;
        this.skillCategory = skillCategory;
        this.skillLevel = skillLevel;
        this.user_id = user_id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public String getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }

    public String getSkillTitle() {
        return skillTitle;
    }

    public void setSkillTitle(String skillTitle) {
        this.skillTitle = skillTitle;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
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

    @Override
    public String toString() {
        return "UserSkills{" +
                "id=" + id +
                ", skillDescription='" + skillDescription + '\'' +
                ", skillTitle='" + skillTitle + '\'' +
                ", skillCategory=" + skillCategory +
                ", skillLevel=" + skillLevel +
                ", user_id=" + user_id +
                ", enabled=" + enabled +
                '}';
    }
}
