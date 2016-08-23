package com.mbv.persist.dao;

import com.mbv.persist.entity.EducationDegree;
import com.mbv.persist.enums.EducationDegreeType;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface EducationDegreeDAO extends BaseEntityDAO<Long,EducationDegree> {

    public EducationDegree getDegreeByNameAndType(String dergeeName, EducationDegreeType type);

    public List<EducationDegree> getDegreesByType(EducationDegreeType type);
}
