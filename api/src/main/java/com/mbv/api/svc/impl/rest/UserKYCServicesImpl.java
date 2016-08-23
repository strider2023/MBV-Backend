package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserKYCData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.UserKYCServices;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.util.UploadFile;
import com.mbv.persist.dao.UserKycDAO;
import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.entity.UserKYC;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.UserTimelineType;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 25/02/16.
 */
@Component
@Path("/mpokket/api/identity/")
public class UserKYCServicesImpl implements UserKYCServices {

    @InjectParam
    UserTimelineDAO userTimelineDAO;

    @InjectParam
    UserKycDAO userKycDAO;

    @Autowired
    UploadFile uploadFile;

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getAllKYCInfoByUser(@PathParam("userId") Long id,
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
        List<UserKYCData> userKYCDatas = new ArrayList<>();
        List<UserKYC> userKYCs = userKycDAO.getUserKYCInfoById(id, paginationInfo);
        if(userKYCs.size() > 0) {
            for (UserKYC userKYC : userKYCs) {
                UserKYCData userKYCData = new UserKYCData();
                userKYCData.setId(userKYC.getId());
                userKYCData.setUserId(id);
                userKYCData.setKycId(userKYC.getKycId());
                userKYCData.setType(userKYC.getType());
                userKYCData.setImageUrl(userKYC.getImageUrl());
                userKYCData.setIsVerified(userKYC.getIsVerified());
                userKYCDatas.add(userKYCData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userKYCDatas.size(), userKYCDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/fetch/kyc/{kycId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getKYCInfo(@PathParam("userId") Long id, @PathParam("kycId") Long kycId) throws AppException {
        UserKYC userKYC = userKycDAO.get(kycId);
        if (userKYC != null) {
            UserKYCData userKYCData = new UserKYCData();
            userKYCData.setId(userKYC.getId());
            userKYCData.setUserId(id);
            userKYCData.setKycId(userKYC.getKycId());
            userKYCData.setType(userKYC.getType());
            userKYCData.setImageUrl(userKYC.getImageUrl());
            userKYCData.setIsVerified(userKYC.getIsVerified());
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, 1, userKYCData);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/create/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse saveKYCInfo(@PathParam("userId") Long id, UserKYCData userKYCData) throws AppException {
        if (userKycDAO.getUserKYCByDetials(id, userKYCData.getKycId(), userKYCData.getType()) == null) {
            UserKYC userKYC = new UserKYC();
            userKYC.setUserId(id);
            userKYC.setKycId(userKYCData.getKycId());
            userKYC.setType(userKYCData.getType());
            userKYC.setImageUrl("");
            userKYC.setIsVerified(false);
            userKYC.setStatus(Status.ACTIVE);
            userKYC.setCreatedBy(id);
            userKYC.setLastUpdatedBy(id);
            userKycDAO.create(userKYC);

            UserTimeline userTimeline = new UserTimeline();
            userTimeline.setUserId(id);
            userTimeline.setType(UserTimelineType.PROFILE_UPDATE);
            userTimeline.setDescription("Added KYC information.");
            userTimeline.setRefUserId(id);
            userTimeline.setStatus(Status.ACTIVE);
            userTimeline.setCreatedBy(id);
            userTimeline.setLastUpdatedBy(id);
            userTimelineDAO.create(userTimeline);

            return new BaseResponse(userKYC.getId(), BaseResponse.ResponseCode.SUCCESS, "KYC info added.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "KYC info exists!");
        }
    }

    @PUT
    @Path("/user/{userId}/update/kyc/{kycId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateKYCInfo(@PathParam("userId") Long id, @PathParam("kycId") Long kycId, UserKYCData userKYCData) throws AppException {
        UserKYC userKYC = userKycDAO.get(kycId);
        if (userKYC != null) {
            userKYC.setKycId(userKYCData.getKycId());
            userKYC.setIsVerified(false);
            userKycDAO.updateAndFlush(userKYC);
            return new BaseResponse(kycId, BaseResponse.ResponseCode.UPDATED, "KYC info updated.");
        } else {
            return new BaseResponse(kycId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @DELETE
    @Path("/user/{userId}/delete/kyc/{kycId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse deleteKYCInfo(@PathParam("userId") Long id, @PathParam("kycId") Long kycId) throws AppException {
        UserKYC userKYC = userKycDAO.get(kycId);
        if (userKYC != null) {
            userKYC.setStatus(Status.DELETED);
            userKycDAO.updateAndFlush(userKYC);
            return new BaseResponse(kycId, BaseResponse.ResponseCode.SUCCESS, "KYC info removed.");
        } else {
            return new BaseResponse(kycId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/add/kyc/{kycId}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse uploadKYCImage(@PathParam("userId") Long id,
                                       @PathParam("kycId") Long kycId,
                                       @FormDataParam("file") InputStream inputStream,
                                       @FormDataParam("file") FormDataContentDisposition fileDetail) throws AppException {
        UserKYC userKYC = userKycDAO.get(kycId);
        if (userKYC != null) {
            String error = "";
            if (fileDetail.getName().length() == 0 || fileDetail.getName() == null) {
                error += " File is empty,";
            }
            if (error.length() > 0) {
                return new BaseResponse(400, BaseResponse.ResponseCode.FAILURE, error);
            }
            String[] fName = fileDetail.getFileName().split("\\.");
            String fileName = "user_kyc_" + String.valueOf(id) + "_" + fName[0] + "_" + System.currentTimeMillis() + "." + fName[1];
            String etag = uploadFile.uploadImage(inputStream, fileDetail, fileName);
            if (etag.startsWith("ERROR")) {
                return new BaseResponse(400, BaseResponse.ResponseCode.FAILURE, "Not uploaded in s3 " + etag);
            }
            String url = "https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/user-images/" + fileName;
            userKYC.setImageUrl(url);
            userKYC.setIsVerified(false);
            userKycDAO.updateAndFlush(userKYC);
            return new BaseResponse(kycId, BaseResponse.ResponseCode.SUCCESS, url);
        } else {
            return new BaseResponse(kycId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }
}
