package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.EducationCategory;
import com.mbv.api.data.UserEducationData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.UserEducationServices;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.util.UploadFile;
import com.mbv.persist.dao.EducationDegreeDAO;
import com.mbv.persist.dao.UserEducationDAO;
import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.entity.EducationDegree;
import com.mbv.persist.entity.UserEducation;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.enums.EducationDegreeType;
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
@Path("/mpokket/api/education/")
public class UserEducationServicesImpl implements UserEducationServices {

    @InjectParam
    UserTimelineDAO userTimelineDAO;

    @InjectParam
    UserEducationDAO userEducationDAO;

    @InjectParam
    EducationDegreeDAO educationDegreeDAO;

    @Autowired
    UploadFile uploadFile;

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getAllEducationInfoByUser(@PathParam("userId") Long id,
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
        List<UserEducationData> userEducationDatas = new ArrayList<>();
        List<UserEducation> userEducations = userEducationDAO.getEducationByUserId(id, paginationInfo);
        if(userEducations.size() > 0) {
            for (UserEducation userEducation : userEducations) {
                UserEducationData userEducationData = new UserEducationData();
                userEducationData.setId(userEducation.getId());
                userEducationData.setUserId(userEducation.getUserId());
                userEducationData.setInstitutionName(userEducation.getInstitutionName());
                userEducationData.setDegreeType(userEducation.getType());
                userEducationData.setDegreeCategoryName(educationDegreeDAO.get(userEducation.getDegreeCategory()).getName());
                userEducationData.setDescription(userEducation.getDescription());
                userEducationData.setStartDate(userEducation.getStartDate());
                userEducationData.setEndDate(userEducation.getEndDate());
                userEducationData.setCity(userEducation.getCity());
                userEducationData.setState(userEducation.getState());
                userEducationData.setCountry(userEducation.getCountry());
                userEducationData.setPincode(userEducation.getPincode());
                userEducationData.setReportUrl(userEducation.getReportImageUrl());
                userEducationData.setIsVerified(userEducation.getIsVerified());
                userEducationDatas.add(userEducationData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userEducationDatas.size(), userEducationDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/fetch/institute/{instituteId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getEducationInfo(@PathParam("userId") Long id, @PathParam("instituteId") Long instituteId) throws AppException {
        UserEducation userEducation = userEducationDAO.get(instituteId);
        if (userEducation != null) {
            UserEducationData userEducationData = new UserEducationData();
            userEducationData.setId(userEducation.getId());
            userEducationData.setUserId(userEducation.getUserId());
            userEducationData.setInstitutionName(userEducation.getInstitutionName());
            userEducationData.setDegreeType(userEducation.getType());
            userEducationData.setDegreeCategoryName(educationDegreeDAO.get(userEducation.getDegreeCategory()).getName());
            userEducationData.setDescription(userEducation.getDescription());
            userEducationData.setStartDate(userEducation.getStartDate());
            userEducationData.setEndDate(userEducation.getEndDate());
            userEducationData.setCity(userEducation.getCity());
            userEducationData.setState(userEducation.getState());
            userEducationData.setCountry(userEducation.getCountry());
            userEducationData.setPincode(userEducation.getPincode());
            userEducationData.setReportUrl(userEducation.getReportImageUrl());
            userEducationData.setIsVerified(userEducation.getIsVerified());
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, 1, userEducationData);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/create/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse saveEducationInfo(@PathParam("userId") Long id, UserEducationData userEducationData) throws AppException {
        //Add degree if not present
        EducationDegree educationDegree = educationDegreeDAO.getDegreeByNameAndType(
                userEducationData.getDegreeCategoryName(), userEducationData.getDegreeType());
        if (educationDegree == null) {
            educationDegree = new EducationDegree();
            educationDegree.setType(userEducationData.getDegreeType());
            educationDegree.setName(userEducationData.getDegreeCategoryName());
            educationDegree.setCreatedBy(-1l);
            educationDegree.setLastUpdatedBy(-1l);
            educationDegree.setStatus(Status.ACTIVE);
            educationDegreeDAO.create(educationDegree);
        }
        //Check education information
        if (userEducationDAO.getEducationByDetails(id,
                userEducationData.getInstitutionName(),
                userEducationData.getDegreeType(),
                userEducationData.getCity(),
                userEducationData.getState()) == null) {
            UserEducation userEducation = new UserEducation();
            userEducation.setUserId(id);
            userEducation.setInstitutionName(userEducationData.getInstitutionName());
            userEducation.setDegreeCategory(educationDegree.getId());
            userEducation.setType(userEducationData.getDegreeType());
            userEducation.setDescription(userEducationData.getDescription());
            userEducation.setStartDate(userEducationData.getStartDate());
            userEducation.setEndDate(userEducationData.getEndDate());
            userEducation.setCity(userEducationData.getCity());
            userEducation.setState(userEducationData.getState());
            userEducation.setCountry(userEducationData.getCountry());
            userEducation.setPincode(userEducationData.getPincode());
            if(userEducationData.getScore() != null) {
                userEducation.setScore(userEducationData.getScore());
            }
            userEducation.setIsVerified(false);
            userEducation.setStatus(Status.ACTIVE);
            userEducation.setCreatedBy(id);
            userEducation.setLastUpdatedBy(id);
            userEducationDAO.create(userEducation);

            UserTimeline userTimeline = new UserTimeline();
            userTimeline.setUserId(id);
            userTimeline.setType(UserTimelineType.PROFILE_UPDATE);
            userTimeline.setDescription("Added education information.");
            userTimeline.setRefUserId(id);
            userTimeline.setStatus(Status.ACTIVE);
            userTimeline.setCreatedBy(id);
            userTimeline.setLastUpdatedBy(id);
            userTimelineDAO.create(userTimeline);
            return new BaseResponse(userEducation.getId(), BaseResponse.ResponseCode.SUCCESS, "Education info added.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Education info exists!");
        }
    }

    @PUT
    @Path("/user/{userId}/update/institute/{instituteId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateEducationInfo(@PathParam("userId") Long id,
                                            @PathParam("instituteId") Long instituteId,
                                            UserEducationData userEducationData) throws AppException {
        EducationDegree educationDegree = educationDegreeDAO.getDegreeByNameAndType(
                userEducationData.getDegreeCategoryName(), userEducationData.getDegreeType());
        if (educationDegree == null) {
            educationDegree = new EducationDegree();
            educationDegree.setType(userEducationData.getDegreeType());
            educationDegree.setName(userEducationData.getInstitutionName());
            educationDegree.setCreatedBy(-1l);
            educationDegree.setLastUpdatedBy(-1l);
            educationDegree.setStatus(Status.ACTIVE);
            educationDegreeDAO.create(educationDegree);
        }
        UserEducation userEducation = userEducationDAO.get(instituteId);
        if (userEducation != null) {
            userEducation.setInstitutionName(userEducationData.getInstitutionName());
            userEducation.setDegreeCategory(educationDegree.getId());
            userEducation.setType(userEducationData.getDegreeType());
            userEducation.setDescription(userEducationData.getDescription());
            userEducation.setStartDate(userEducationData.getStartDate());
            userEducation.setEndDate(userEducationData.getEndDate());
            userEducation.setCity(userEducationData.getCity());
            userEducation.setState(userEducationData.getState());
            userEducation.setCountry(userEducationData.getCountry());
            userEducation.setPincode(userEducationData.getPincode());
            userEducation.setLastUpdatedBy(id);
            userEducationDAO.updateAndFlush(userEducation);
            return new BaseResponse(instituteId, BaseResponse.ResponseCode.UPDATED, "Education info updated.");
        } else {
            return new BaseResponse(instituteId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @DELETE
    @Path("/user/{userId}/delete/institute/{instituteId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse deleteEducationInfo(@PathParam("userId") Long id, @PathParam("instituteId") Long instituteId) throws AppException {
        UserEducation userEducation = userEducationDAO.get(instituteId);
        if (userEducation != null) {
            userEducation.setStatus(Status.DELETED);
            userEducationDAO.updateAndFlush(userEducation);
            return new BaseResponse(instituteId, BaseResponse.ResponseCode.SUCCESS, "Education info removed.");
        } else {
            return new BaseResponse(instituteId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/add/institute/{instituteId}/marksheet")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse uploadMarksheetImage(@PathParam("userId") Long id,
                                             @PathParam("instituteId") Long instituteId,
                                             @FormDataParam("file") InputStream inputStream,
                                             @FormDataParam("file") FormDataContentDisposition fileDetail) throws AppException {
        UserEducation userEducation = userEducationDAO.get(instituteId);
        if (userEducation != null) {
            String error = "";
            if (fileDetail.getName().length() == 0 || fileDetail.getName() == null) {
                error += " File is empty,";
            }
            if (error.length() > 0) {
                return new BaseResponse(400, BaseResponse.ResponseCode.FAILURE, error);
            }
            String[] fName = fileDetail.getFileName().split("\\.");
            String fileName = "user_edu_" + String.valueOf(id) + "_" + fName[0] + "_" + System.currentTimeMillis() + "." + fName[1];
            String etag = uploadFile.uploadImage(inputStream, fileDetail, fileName);
            if (etag.startsWith("ERROR")) {
                return new BaseResponse(400, BaseResponse.ResponseCode.FAILURE, "Not uploaded in s3 " + etag);
            }
            String url = "https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/user-images/" + fileName;
            userEducation.setReportImageUrl(url);
            userEducationDAO.updateAndFlush(userEducation);
            return new BaseResponse(instituteId, BaseResponse.ResponseCode.SUCCESS, url);
        } else {
            return new BaseResponse(instituteId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/categories/type/{eduType}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getAllEducationDegreesByType(@PathParam("eduType") EducationDegreeType educationDegreeType) throws AppException {
        List<EducationCategory> educationCategories = new ArrayList<>();
        List<EducationDegree> educationDegrees = educationDegreeDAO.getDegreesByType(educationDegreeType);
        if (educationDegrees.size() > 0) {
            for (EducationDegree educationDegree : educationDegrees) {
                EducationCategory educationCategory = new EducationCategory();
                educationCategory.setId(educationDegree.getId());
                educationCategory.setType(educationDegree.getType());
                educationCategory.setCategoryName(educationDegree.getName());
                educationCategories.add(educationCategory);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, educationCategories.size(), educationCategories);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/categories/create/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse createEducationDegree(EducationCategory educationCategory) throws AppException {
        if (educationDegreeDAO.getDegreeByNameAndType(educationCategory.getCategoryName(), educationCategory.getType()) == null) {
            EducationDegree educationDegree = new EducationDegree();
            educationDegree.setType(educationCategory.getType());
            educationDegree.setName(educationCategory.getCategoryName());
            educationDegree.setCreatedBy(-1l);
            educationDegree.setLastUpdatedBy(-1l);
            educationDegree.setStatus(Status.ACTIVE);
            educationDegreeDAO.create(educationDegree);
            return new BaseResponse(educationDegree.getId(), BaseResponse.ResponseCode.SUCCESS, "Education degree added.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Education degree record already present!");
        }
    }
}
