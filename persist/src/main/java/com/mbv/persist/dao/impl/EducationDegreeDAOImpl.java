package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.EducationDegreeDAO;
import com.mbv.persist.entity.EducationDegree;
import com.mbv.persist.enums.EducationDegreeType;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public class EducationDegreeDAOImpl extends BaseEntityDAOImpl<EducationDegree> implements EducationDegreeDAO {

    public EducationDegreeDAOImpl() {
        super(EducationDegree.class);
    }

    @Override
    public EducationDegree getDegreeByNameAndType(String dergeeName, EducationDegreeType type) {
        Criteria criteria = this.getSession().createCriteria(EducationDegree.class);
        criteria.add(Restrictions.eq("name", dergeeName));
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (EducationDegree) criteria.uniqueResult();
    }

    @Override
    public List<EducationDegree> getDegreesByType(EducationDegreeType type) {
        Criteria criteria = this.getSession().createCriteria(EducationDegree.class);
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        List<EducationDegree> educationDegrees = criteria.list();
        evictCollection(educationDegrees);
        return educationDegrees;
    }
}
