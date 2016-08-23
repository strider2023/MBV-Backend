package com.mbv.persist.dao;

import com.mbv.persist.entity.UserEducation;
import com.mbv.persist.enums.EducationDegreeType;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserEducationDAO extends BaseEntityDAO<Long,UserEducation> {

    public List<UserEducation> getEducationByUserId(Long userId, Map<Object, Object> paginationInfo);

    public UserEducation getEducationByDetails(Long userId, String institutionName, EducationDegreeType type, String city, String state);
}
