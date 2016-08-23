package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserSignUpData;
import com.mbv.api.data.UserLocationData;
import com.mbv.api.data.UserTimelineData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.UserServices;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.util.UploadFile;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.dao.UserLocationDAO;
import com.mbv.persist.dao.UserScoreDAO;
import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.entity.User;
import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.entity.UserScore;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.enums.RoleType;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.UserTimelineType;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 20/01/16.
 */
@Component
@Path("/mpokket/api/user/")
public class UserServicesImpl implements UserServices {

    @InjectParam
    UserDAO userDAO;

    @InjectParam
    UserScoreDAO userScoreDAO;

    @InjectParam
    UserTimelineDAO userTimelineDAO;

    @InjectParam
    UserLocationDAO userLocationDAO;

    @Autowired
    UploadFile uploadFile;

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUser(@PathParam("userId") Long userId) throws AppException {
        User user = userDAO.get(userId);
        UserSignUpData userSignUpData = new UserSignUpData();
        userSignUpData.setId(user.getId());
        userSignUpData.setAccountType(user.getType());
        userSignUpData.setName(StringUtil.getName(user));
        userSignUpData.setEmail(user.getEmail());
        userSignUpData.setPhoneNumber(user.getPhone());
        userSignUpData.setDob(user.getDob());
        userSignUpData.setWorkStatus(user.getWorkStatus());
        userSignUpData.setResidentialStatus(user.getResidentialStatus());
        userSignUpData.setMaritalStatus(user.getMaritalStatus());
        userSignUpData.setGender(user.getGender());
        userSignUpData.setUserImage(user.getImageUrl());
        userSignUpData.setRoleType(user.getRole());
        userSignUpData.setFatherName(user.getFatherName());
        if(user.getGcmId() != null) {
            userSignUpData.setGcmId(user.getGcmId());
        }

        List<UserLocation> userLocations = userLocationDAO.getUserLocationInfoById(user.getId(), null);
        if (userLocations.size() > 0) {
            List<UserLocationData> userLocationDatas = new ArrayList<>();
            for (UserLocation userLocation : userLocations) {
                UserLocationData userLocationData = new UserLocationData();
                userLocationData.setId(userLocation.getId());
                userLocationData.setUserId(userLocation.getUserId());
                userLocationData.setAddress(userLocation.getAddress());
                userLocationData.setCity(userLocation.getCity());
                userLocationData.setState(userLocation.getState());
                userLocationData.setCountry(userLocation.getCountry());
                userLocationData.setPincode(userLocation.getPincode());
                userLocationData.setType(userLocation.getType());
                userLocationDatas.add(userLocationData);
            }
            userSignUpData.setUserLocationDatas(userLocationDatas);
        }
        return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, 1, userSignUpData);
    }

    @GET
    @Path("/{userId}/update/type/{typeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateUserType(@PathParam("userId") Long userId, @PathParam("typeId") RoleType roleType) throws AppException {
        User user = userDAO.get(userId);
        user.setRole(roleType);
        userDAO.updateAndFlush(user);
        return new BaseResponse(userId, BaseResponse.ResponseCode.SUCCESS);
    }

    @PUT
    @Path("/{userId}/update/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateUser(@PathParam("userId") Long id, UserSignUpData userSignUpData) throws AppException {
        User user = userDAO.get(id);
        user.setFirstname(userSignUpData.getFirstName());
        user.setMiddlename(userSignUpData.getMiddleName());
        user.setLastname(userSignUpData.getLastName());
        user.setPhone(userSignUpData.getPhoneNumber());
        user.setDob(userSignUpData.getDob());
        user.setGender(userSignUpData.getGender());
        user.setMaritalStatus(userSignUpData.getMaritalStatus());
        user.setResidentialStatus(userSignUpData.getResidentialStatus());
        user.setWorkStatus(userSignUpData.getWorkStatus());
        user.setFatherName(userSignUpData.getFatherName());
        if(user.getGcmId() != null) {
            userSignUpData.setGcmId(user.getGcmId());
        }
        user.setLastUpdatedBy(id);
        userDAO.updateAndFlush(user);
        //Check for location information
        if(userSignUpData.getUserLocationDatas() != null) {
            if(userSignUpData.getUserLocationDatas().size() > 0) {
                for(UserLocationData userLocationData : userSignUpData.getUserLocationDatas()){
                    UserLocation userLocation = userLocationDAO.getUserLocationByDetails(id,
                            userLocationData.getCity(),
                            userLocationData.getState(),
                            userLocationData.getCountry(),
                            userLocationData.getPincode(),
                            userLocationData.getType());
                    //Create if not present else update
                    if(userLocation == null) {
                        userLocation = new UserLocation();
                        userLocation.setUserId(id);
                        userLocation.setAddress(userLocationData.getAddress());
                        userLocation.setCity(userLocationData.getCity());
                        userLocation.setState(userLocationData.getState());
                        userLocation.setCountry(userLocationData.getCountry());
                        userLocation.setPincode(userLocationData.getPincode());
                        userLocation.setMonthsOfOccupation(new Date());
                        userLocation.setType(userLocationData.getType());
                        userLocation.setIsVerified(false);
                        userLocation.setStatus(Status.ACTIVE);
                        userLocation.setCreatedBy(id);
                        userLocation.setLastUpdatedBy(id);
                        userLocationDAO.create(userLocation);
                    } else {
                        userLocation.setAddress(userLocationData.getAddress());
                        userLocation.setCity(userLocationData.getCity());
                        userLocation.setState(userLocationData.getState());
                        userLocation.setCountry(userLocationData.getCountry());
                        userLocation.setPincode(userLocationData.getPincode());
                        userLocation.setMonthsOfOccupation(new Date());
                        userLocation.setType(userLocationData.getType());
                        userLocation.setLastUpdatedBy(id);
                        userLocationDAO.updateAndFlush(userLocation);
                    }
                }
            }
        }
        //Add timeline information
        UserTimeline userTimeline = new UserTimeline();
        userTimeline.setUserId(user.getId());
        userTimeline.setType(UserTimelineType.PROFILE_UPDATE);
        userTimeline.setDescription("Updated profile information.");
        userTimeline.setRefUserId(user.getId());
        userTimeline.setStatus(Status.ACTIVE);
        userTimeline.setCreatedBy(user.getId());
        userTimeline.setLastUpdatedBy(user.getId());
        userTimelineDAO.create(userTimeline);

        return new BaseResponse(id, BaseResponse.ResponseCode.SUCCESS, "Profile information updated successfully");
    }

    @POST
    @Path("/{userId}/update/notification")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateUserGMCId(@PathParam("userId") Long userId, UserSignUpData userSignUpData) throws AppException {
        User user = userDAO.get(userId);
        user.setGcmId(userSignUpData.getGcmId());
        userDAO.updateAndFlush(user);
        return new BaseResponse(userId, BaseResponse.ResponseCode.SUCCESS, "Profile information updated successfully");
    }

    @GET
    @Path("/{userId}/rate/{rating}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse rateUser(@PathParam("userId") Long id, @PathParam("rating") Long rating) throws AppException {
        UserScore userScore = userScoreDAO.getUserScoreInfoById(id);
        userScore.setUserRating(rating);
        userScore.setUserRatingCount(userScore.getUserRatingCount() + 1);
        userScoreDAO.updateAndFlush(userScore);
        return new BaseResponse(id, BaseResponse.ResponseCode.SUCCESS, "User rating updated.");
    }

    @POST
    @Path("/{userId}/upload/image/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse uploadUserImage(@PathParam("userId") Long id,
                                        @FormDataParam("file") InputStream inputStream,
                                        @FormDataParam("file") FormDataContentDisposition fileDetail) throws AppException {
        String error = "";
        if (fileDetail.getName().length() == 0 || fileDetail.getName() == null) {
            error += " File is empty,";
        }
        if (error.length() > 0) {
            return new BaseResponse(400, BaseResponse.ResponseCode.FAILURE, error);
        }
        String[] fName = fileDetail.getFileName().split("\\.");
        String fileName = "user_profile_" + String.valueOf(id) + "_" + fName[0] + "_" + System.currentTimeMillis() + "." + fName[1];
        String etag = uploadFile.uploadImage(inputStream, fileDetail, fileName);
        if (etag.startsWith("ERROR")) {
            return new BaseResponse(400, BaseResponse.ResponseCode.FAILURE, "Not uploaded in s3 " + etag);
        }

        String url = "https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/user-images/" + fileName;
        User user = userDAO.get(id);
        user.setImageUrl(url);
        userDAO.updateAndFlush(user);
        return new BaseResponse(id, BaseResponse.ResponseCode.SUCCESS, url);
    }

    @GET
    @Path("/{userId}/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUserTimeline(@PathParam("userId") Long userId,
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
        List<UserTimelineData> userTimelineDatas = new ArrayList<>();
        List<UserTimeline> userTimelines = userTimelineDAO.getAllTimelineInfoByUser(userId, paginationInfo);
        if (userTimelines.size() > 0) {
            for (UserTimeline userTimeline : userTimelines) {
                UserTimelineData userTimelineData = new UserTimelineData();
                userTimelineData.setUserId(userId);
                userTimelineData.setDescription(userTimeline.getDescription());
                userTimelineData.setUserTimelineType(userTimeline.getType());
                userTimelineData.setReferenceUserId(userTimeline.getRefUserId());
                userTimelineData.setReferenceLoanId(userTimeline.getRefLoanId());
                userTimelineData.setCreatedOn(userTimeline.getCreatedDate());
                userTimelineDatas.add(userTimelineData);
            }
        }
        return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userTimelineDatas.size(), userTimelineDatas);
    }
}
