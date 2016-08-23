package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.FeedbackDAO;
import com.mbv.persist.entity.Feedback;
import com.mbv.persist.enums.FeedbackType;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class FeedbackDAOImpl extends BaseEntityDAOImpl<Feedback> implements FeedbackDAO {

    public FeedbackDAOImpl() {
        super(Feedback.class);
    }

    @Override
    public List<Feedback> getAllUserConversations(Long userId, FeedbackType feedbackType, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(Feedback.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("parentId", -1l));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(feedbackType == FeedbackType.ALL) {
            criteria.add(Restrictions.in("type", Arrays.asList(feedbackType.GENERAL, feedbackType.LOAN, feedbackType.WALLET)));
        } else {
            criteria.add(Restrictions.eq("type", feedbackType));
        }
        List<Feedback> feedbacks = criteria.list();
        evictCollection(feedbacks);
        return feedbacks;
    }

    @Override
    public List<Feedback> getAllConversationsByParentId(Long parentId, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(Feedback.class);
        criteria.add(Restrictions.eq("parentId", parentId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        List<Feedback> feedbacks = criteria.list();
        evictCollection(feedbacks);
        return feedbacks;
    }
}
