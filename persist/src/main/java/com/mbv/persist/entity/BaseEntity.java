package com.mbv.persist.entity;

import com.mbv.persist.enums.Status;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1426174937629916180L;

    public static final long SYSTEM_USER =  -1L;

    public static final long SYSTEM_ACCOUNT =  -1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    @Id
    private T id;

    @Column(name = "create_user")
    private Long createdBy;

    @Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "update_user")
    private Long lastUpdatedBy;

    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
