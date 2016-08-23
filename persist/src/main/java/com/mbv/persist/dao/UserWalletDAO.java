package com.mbv.persist.dao;

import com.mbv.persist.entity.UserWallet;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserWalletDAO extends BaseEntityDAO<Long,UserWallet> {

    public UserWallet getWalletInfoByUserId(Long userId);
}
