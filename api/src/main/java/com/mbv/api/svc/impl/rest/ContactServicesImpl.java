package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserFeedbackData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.ContactServices;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.dao.FeedbackDAO;
import com.mbv.persist.dao.LoanBorrowerInfoDAO;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.entity.Feedback;
import com.mbv.persist.entity.LoanBorrowerInfo;
import com.mbv.persist.enums.FeedbackType;
import com.mbv.persist.enums.Status;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 01/04/16.
 */
@Component
@Path("/mpokket/api/conversations")
public class ContactServicesImpl extends BaseAPIServiceImpl implements ContactServices {

    @InjectParam
    UserDAO userDAO;

    @InjectParam
    LoanBorrowerInfoDAO loanBorrowerInfoDAO;

    @InjectParam
    FeedbackDAO feedbackDAO;

    @GET
    @Path("/user/{userId}/fetch")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUserConvesations(@PathParam("userId") Long userId,
                                            @QueryParam("offset") Integer offset,
                                            @QueryParam("pageSize") Integer pageSize,
                                            @QueryParam("searchField") String searchField,
                                            @QueryParam("searchText") String searchText,
                                            @QueryParam("sortField") String sortField,
                                            @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                            @QueryParam("filters") String filters) throws AppException {
        Map<Object, Object> paginationInfo = null;
        if (offset != null) {
            paginationInfo = PaginationMapper.mapPaginationInfo(offset, pageSize, searchField, searchText, sortField, sortInAscOrder);
        }
        List<Feedback> feedbacks = feedbackDAO.getAllUserConversations(userId, FeedbackType.ALL, paginationInfo);
        if(feedbacks.size() > 0) {
            Map<UserFeedbackData, List<UserFeedbackData>> userFeedbackDataListMap = new HashMap<>();
            for(Feedback feedback : feedbacks) {
                List<Feedback> children = feedbackDAO.getAllConversationsByParentId(feedback.getParentId(), null);
                List<UserFeedbackData> userFeedbackDatas = new ArrayList<>();
                if(children != null) {
                    if(children.size() > 0) {
                        for(Feedback feedback1 : children) {
                            userFeedbackDatas.add(mapData(feedback1));
                        }
                    }
                }
                userFeedbackDatas.add(mapData(feedback));
                userFeedbackDataListMap.put(mapData(feedback), userFeedbackDatas);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userFeedbackDataListMap.size(), userFeedbackDataListMap);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/comment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse postComment(@PathParam("userId") Long userId, UserFeedbackData userFeedbackData) throws AppException {
        Feedback parentFeedback = null;
        if(userFeedbackData.getParentId() != -1) {
            parentFeedback = feedbackDAO.get(userFeedbackData.getParentId());
            if (parentFeedback == null) {
                return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, "Unable to find parent conversation!");
            }
        }
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setParentId(userFeedbackData.getParentId());
        feedback.setTitle((parentFeedback != null) ? parentFeedback.getTitle() : userFeedbackData.getTitle());
        feedback.setComment(userFeedbackData.getComment());
        feedback.setType((parentFeedback != null) ? parentFeedback.getType() : userFeedbackData.getFeedbackType());
        feedback.setLoanId((parentFeedback != null) ? parentFeedback.getLoanId() : userFeedbackData.getLoanId());
        feedback.setStatus(Status.ACTIVE);
        feedback.setCreatedBy(userId);
        feedback.setLastUpdatedBy(userId);
        feedbackDAO.updateAndFlush(feedback);
        return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, "Conversation saved.");
    }

    private UserFeedbackData mapData(Feedback feedback) {
        UserFeedbackData userFeedbackData = new UserFeedbackData();
        userFeedbackData.setId(feedback.getId());
        userFeedbackData.setParentId(feedback.getParentId());
        userFeedbackData.setUserId(feedback.getUserId());
        userFeedbackData.setTitle(feedback.getTitle());
        userFeedbackData.setComment(feedback.getComment());
        userFeedbackData.setLoanId(feedback.getLoanId());
        userFeedbackData.setPostDate(feedback.getCreatedDate());
        return userFeedbackData;
    }
}
