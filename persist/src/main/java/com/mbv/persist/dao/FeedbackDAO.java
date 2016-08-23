package com.mbv.persist.dao;

import com.mbv.persist.entity.Feedback;
import com.mbv.persist.enums.FeedbackType;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface FeedbackDAO extends BaseEntityDAO<Long,Feedback> {

    public List<Feedback> getAllUserConversations(Long userId, FeedbackType feedbackType, Map<Object, Object> paginationInfo);

    public List<Feedback> getAllConversationsByParentId(Long parentId, Map<Object, Object> paginationInfo);
}
