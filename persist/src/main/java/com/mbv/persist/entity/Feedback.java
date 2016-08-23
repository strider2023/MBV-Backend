package com.mbv.persist.entity;

import com.mbv.persist.enums.FeedbackType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="feedback")
public class Feedback extends BaseEntity<Long> implements Serializable {

    @Column(name="user_id")
    private Long userId;

    @Column(name="parent_id")
    private Long parentId;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private FeedbackType type;

    @Column(name="title")
    private String title;

    @Column(name="comment")
    private String comment;

    @Column(name="loan_id")
    private Long loanId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public FeedbackType getType() {
        return type;
    }

    public void setType(FeedbackType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}
