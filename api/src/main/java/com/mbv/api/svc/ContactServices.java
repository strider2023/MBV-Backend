package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserFeedbackData;
import com.mbv.framework.exception.AppException;

/**
 * Created by arindamnath on 01/04/16.
 */
public interface ContactServices {

    public BaseResponse getUserConvesations(Long userId, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse postComment(Long userId, UserFeedbackData userFeedbackData) throws AppException;
}
