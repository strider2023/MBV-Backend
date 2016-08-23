package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserLocationData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.UserLocationServices;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.dao.UserLocationDAO;
import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.UserTimelineType;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
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
@Path("/mpokket/api/locations/")
public class UserLocationServicesImpl implements UserLocationServices {

    @InjectParam
    UserTimelineDAO userTimelineDAO;

    @InjectParam
    UserLocationDAO userLocationDAO;

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getAllUserLocations(@PathParam("userId") Long id,
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
        List<UserLocationData> userLocationDatas = new ArrayList<>();
        List<UserLocation> userLocations = userLocationDAO.getUserLocationInfoById(id, paginationInfo);
        if(userLocations.size() > 0) {
            for (UserLocation userLocation : userLocations) {
                UserLocationData userLocationData = new UserLocationData();
                userLocationData.setId(userLocation.getId());
                userLocationData.setUserId(id);
                userLocationData.setAddress(userLocation.getAddress());
                userLocationData.setCity(userLocation.getCity());
                userLocationData.setState(userLocation.getState());
                userLocationData.setCountry(userLocation.getCountry());
                userLocationData.setPincode(userLocation.getPincode());
                userLocationData.setType(userLocation.getType());
                userLocationDatas.add(userLocationData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userLocationDatas.size(), userLocationDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/fetch/location/{locationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUserLocationInfo(@PathParam("userId") Long id, @PathParam("locationId") Long locationId) throws AppException {

        UserLocation userLocation = userLocationDAO.get(locationId);
        if (userLocation != null) {
            UserLocationData userLocationData = new UserLocationData();
            userLocationData.setId(userLocation.getId());
            userLocationData.setUserId(id);
            userLocationData.setAddress(userLocation.getAddress());
            userLocationData.setCity(userLocation.getCity());
            userLocationData.setState(userLocation.getState());
            userLocationData.setCountry(userLocation.getCountry());
            userLocationData.setPincode(userLocation.getPincode());
            userLocationData.setType(userLocation.getType());
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, 1, userLocationData);
        } else {
            return new BaseResponse(locationId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/create/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse saveUserLocation(@PathParam("userId") Long id, UserLocationData userLocationData) throws AppException {
        if (userLocationDAO.getUserLocationByDetails(id,
                userLocationData.getCity(),
                userLocationData.getState(),
                userLocationData.getCountry(),
                userLocationData.getPincode(),
                userLocationData.getType()) == null) {
            UserLocation userLocation = new UserLocation();
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

            UserTimeline userTimeline = new UserTimeline();
            userTimeline.setUserId(id);
            userTimeline.setType(UserTimelineType.PROFILE_UPDATE);
            userTimeline.setDescription("Added location information.");
            userTimeline.setRefUserId(id);
            userTimeline.setStatus(Status.ACTIVE);
            userTimeline.setCreatedBy(id);
            userTimeline.setLastUpdatedBy(id);
            userTimelineDAO.create(userTimeline);
            return new BaseResponse(userLocation.getId(), BaseResponse.ResponseCode.SUCCESS, "Education info added.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Location info exists!");
        }
    }

    @PUT
    @Path("/user/{userId}/update/location/{locationId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateUserLocation(@PathParam("userId") Long id, @PathParam("locationId") Long locationId,
                                           UserLocationData userLocationData) throws AppException {
        UserLocation userLocation = userLocationDAO.get(locationId);
        if (userLocation != null) {
            userLocation.setAddress(userLocationData.getAddress());
            userLocation.setCity(userLocationData.getCity());
            userLocation.setState(userLocationData.getState());
            userLocation.setCountry(userLocationData.getCountry());
            userLocation.setPincode(userLocationData.getPincode());
            userLocation.setMonthsOfOccupation(new Date());
            userLocation.setType(userLocationData.getType());
            userLocation.setLastUpdatedBy(id);
            userLocationDAO.updateAndFlush(userLocation);
            return new BaseResponse(locationId, BaseResponse.ResponseCode.UPDATED, "Location info updated.");
        } else {
            return new BaseResponse(locationId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @DELETE
    @Path("/user/{userId}/delete/location/{locationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse deleteUserLocation(@PathParam("userId") Long id, @PathParam("locationId") Long locationId) throws AppException {
        UserLocation userLocation = userLocationDAO.get(locationId);
        if (userLocation != null) {
            userLocation.setStatus(Status.DELETED);
            userLocationDAO.updateAndFlush(userLocation);
            return new BaseResponse(locationId, BaseResponse.ResponseCode.SUCCESS, "Education info removed.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }
}
