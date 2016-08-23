package com.mbv.persist.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractBaseSession {

    private SessionFactory sessionFactory;

    private int jdbcBatchSize = 100;

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public int getJdbcBatchSize() {
        return jdbcBatchSize;
    }

    public void setJdbcBatchSize(int jdbcBatchSize) {
        this.jdbcBatchSize = jdbcBatchSize;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
