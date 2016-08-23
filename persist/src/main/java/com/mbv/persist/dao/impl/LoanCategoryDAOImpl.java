package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.LoanCategoryDAO;
import com.mbv.persist.entity.LoanCategory;

/**
 * Created by arindamnath on 25/02/16.
 */
public class LoanCategoryDAOImpl extends BaseEntityDAOImpl<LoanCategory> implements LoanCategoryDAO {

    public LoanCategoryDAOImpl() {
        super(LoanCategory.class);
    }
}
