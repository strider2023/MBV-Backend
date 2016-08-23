package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserKYCData;
import com.mbv.framework.exception.AppException;
import com.sun.jersey.core.header.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserKYCServices {

    public BaseResponse getAllKYCInfoByUser(Long id, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getKYCInfo(Long id, Long kycId) throws AppException;

    public BaseResponse saveKYCInfo(Long id, UserKYCData userKYCData) throws AppException;

    public BaseResponse updateKYCInfo(Long id, Long kycId, UserKYCData userKYCData) throws AppException;

    public BaseResponse deleteKYCInfo(Long id, Long kycId) throws AppException;

    public BaseResponse uploadKYCImage(Long id, Long kycId, InputStream inputStream, FormDataContentDisposition fileDetail) throws AppException;
}
