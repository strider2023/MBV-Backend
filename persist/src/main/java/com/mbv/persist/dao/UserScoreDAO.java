package com.mbv.persist.dao;

import com.mbv.persist.entity.UserScore;

/**
 * Created by arindamnath on 18/03/16.
 */
public interface UserScoreDAO extends BaseEntityDAO<Long,UserScore> {

    public UserScore getUserScoreInfoById(Long userId);
}
