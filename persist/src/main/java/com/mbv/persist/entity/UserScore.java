package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 18/03/16.
 */
@Entity
@Table(name="user_score")
public class UserScore extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="loan_quota")
    private Long loanQuota;

    @Column(name="user_rating")
    private Long userRating;

    @Column(name="user_rating_count")
    private Long userRatingCount;

    @Column(name="system_rating")
    private Long systemRating;

    @Column(name="system_rating_count")
    private Long systemRatingCount;

    @Column(name="default_counter")
    private Long defaults;

    @Column(name="credits")
    private Long credits;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLoanQuota() {
        return loanQuota;
    }

    public void setLoanQuota(Long loanQuota) {
        this.loanQuota = loanQuota;
    }

    public Long getUserRating() {
        return userRating;
    }

    public void setUserRating(Long userRating) {
        this.userRating = userRating;
    }

    public Long getSystemRating() {
        return systemRating;
    }

    public void setSystemRating(Long systemRating) {
        this.systemRating = systemRating;
    }

    public Long getDefaults() {
        return defaults;
    }

    public void setDefaults(Long defaults) {
        this.defaults = defaults;
    }

    public Long getCredits() {
        return credits;
    }

    public void setCredits(Long credits) {
        this.credits = credits;
    }

    public Long getUserRatingCount() {
        return userRatingCount;
    }

    public void setUserRatingCount(Long userRatingCount) {
        this.userRatingCount = userRatingCount;
    }

    public Long getSystemRatingCount() {
        return systemRatingCount;
    }

    public void setSystemRatingCount(Long systemRatingCount) {
        this.systemRatingCount = systemRatingCount;
    }
}
