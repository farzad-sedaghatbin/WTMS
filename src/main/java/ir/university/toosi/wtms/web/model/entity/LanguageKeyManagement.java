package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;


public class LanguageKeyManagement extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String descriptionKey;
    @JsonProperty
    private Set<LanguageManagement> languageManagements;


    public LanguageKeyManagement(String descriptionKey, Set<LanguageManagement> languageManagements) {
        this.descriptionKey = descriptionKey;
        this.languageManagements = languageManagements;
    }
    LanguageKeyManagement(long id){
        this.id=id;
    }
    public LanguageKeyManagement(Set<LanguageManagement> languageManagements) {
        this.languageManagements = languageManagements;
    }

    public LanguageKeyManagement() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public Set<LanguageManagement> getLanguageManagements() {
        return languageManagements;
    }

    public void setLanguageManagements(Set<LanguageManagement> languageManagements) {
        this.languageManagements = languageManagements;
    }
}
