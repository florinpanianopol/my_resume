package com.myresume.common.entity;



import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
@Table(name="languages")
public class LanguageSection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Integer id;

    @Size(min = 3, message = "- at least 3 characters")
    @NotEmpty(message = "- is required")
    @Column(length = 128, nullable = false)
    private String language;

    @Min(value = 1, message = "Value should not be less than 1")
    @Max(value = 100, message = "Value should not be greater than 100")
    @Column(length = 128, nullable = false)
    private int languageLevel;

    private Integer user_id;

    private boolean enabled;

    public LanguageSection() {

    }

    public LanguageSection(@Size(min = 3, message = "- at least 3 characters") @NotEmpty(message = "- is required") String language, @Min(value = 1, message = "Value should not be less than 1") @Max(value = 100, message = "Value should not be greater than 10") int languageLevel, Integer user_id, boolean enabled) {
        this.language = language;
        this.languageLevel = languageLevel;
        this.user_id = user_id;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(int languageLevel) {
        this.languageLevel = languageLevel;
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
        return "LanguageSection{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", languageLevel=" + languageLevel +
                ", user_id=" + user_id +
                ", enabled=" + enabled +
                '}';
    }
}
