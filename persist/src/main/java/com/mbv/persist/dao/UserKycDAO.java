package com.mbv.persist.dao;

import com.mbv.persist.entity.UserKYC;
import com.mbv.persist.enums.KYCType;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserKycDAO extends BaseEntityDAO<Long,UserKYC> {

    public List<UserKYC> getUserKYCInfoById(Long userId, Map<Object, Object> paginationInfo);

    public UserKYC getUserKYCByDetials(Long userId, String id, KYCType type);
}
