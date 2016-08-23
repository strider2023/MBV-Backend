package com.mbv.persist.dao;

import com.mbv.persist.entity.LoanOfferCode;
import com.mbv.persist.enums.Status;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface LoanOfferCodeDAO extends BaseEntityDAO<Long,LoanOfferCode> {

    public List<LoanOfferCode> getOffercode(Status status, Map<Object, Object> paginationInfo);

    public LoanOfferCode getOfferCodeByDetails(String offerCode);
}
