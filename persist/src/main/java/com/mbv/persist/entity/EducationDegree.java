package com.mbv.persist.entity;

import com.mbv.persist.enums.EducationDegreeType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="degree_categories")
public class EducationDegree extends BaseEntity<Long> implements Serializable {

    @Column(name="degree_type")
    @Enumerated(EnumType.ORDINAL)
    private EducationDegreeType type;

    @Column(name="name")
    private String name;

    public EducationDegreeType getType() {
        return type;
    }

    public void setType(EducationDegreeType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
