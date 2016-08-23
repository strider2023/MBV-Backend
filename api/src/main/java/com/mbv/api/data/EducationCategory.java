package com.mbv.api.data;

import com.mbv.persist.enums.EducationDegreeType;

/**
 * Created by arindamnath on 26/02/16.
 */
public class EducationCategory {

    private EducationDegreeType type;

    private String categoryName;

    private Long id;

    public EducationCategory() { }

    public EducationDegreeType getType() {
        return type;
    }

    public void setType(EducationDegreeType type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
