package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.EducationCategory;
import com.mbv.api.data.UserEducationData;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.enums.EducationDegreeType;
import com.sun.jersey.core.header.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserEducationServices {

    public BaseResponse getAllEducationInfoByUser(Long id, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getEducationInfo(Long id, Long instituteId) throws AppException;

    public BaseResponse saveEducationInfo(Long id, UserEducationData userKYCData) throws AppException;

    public BaseResponse updateEducationInfo(Long id, Long instituteId, UserEducationData userKYCData) throws AppException;

    public BaseResponse deleteEducationInfo(Long id, Long instituteId) throws AppException;

    public BaseResponse uploadMarksheetImage(Long id, Long instituteId, InputStream inputStream, FormDataContentDisposition fileDetail) throws AppException;

    public BaseResponse getAllEducationDegreesByType(EducationDegreeType educationDegreeType) throws AppException;

    public BaseResponse createEducationDegree(EducationCategory educationCategory) throws AppException;
}
