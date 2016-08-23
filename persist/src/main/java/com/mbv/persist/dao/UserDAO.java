package com.mbv.persist.dao;

import com.mbv.persist.entity.User;
import com.mbv.persist.enums.AccountType;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 20/01/16.
 */
public interface UserDAO extends BaseEntityDAO<Long,User> {

    public User getUserByEmail(String email);

    public User getUserByEmailAndPhone(String email, String phone);

    public User getUserByEmailAndHash(String email, String hash);

    public List<User> getUserList(AccountType type, Map<Object, Object> paginationInfo);
}
