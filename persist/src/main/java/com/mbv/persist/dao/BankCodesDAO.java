package com.mbv.persist.dao;

import com.mbv.persist.entity.BankCodes;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 04/03/16.
 */
public interface BankCodesDAO extends BaseEntityDAO<Long,BankCodes> {

    public BankCodes getBankCodeByIFSC(String ifsc);

    public List<BankCodes> getBankCodesByString(Map<Object, Object> paginationInfo);
}
