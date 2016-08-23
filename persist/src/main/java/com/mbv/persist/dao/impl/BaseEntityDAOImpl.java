package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.BaseEntityDAO;
import com.mbv.persist.entity.BaseEntity;
import com.mbv.persist.enums.SortTypeEnum;
import com.mbv.persist.enums.Status;
import com.mbv.persist.util.StringUtil;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BaseEntityDAOImpl<T extends BaseEntity<Long>> extends AbstractBaseSession implements BaseEntityDAO<Long,T> {

    protected Class<T> type;
    protected static final int DEFAULT_MAX_RESULT_SET = 50;
    protected static final int BATCH_SIZE = 20;
    protected final static DecimalFormat decimalFormat = new DecimalFormat("###.##");

    BaseEntityDAOImpl(Class<T> t) {
        this.type = t;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Long id) {
        return (T) this.getSession().get(type, id);
    }

    @Override
    public void bulkUpdate(Collection<T> objList) {
        Session session = getSession();
        Date date = null;
        int count = 0;
        for (T detail : objList) {
            if(detail.getLastUpdatedDate() == null){
                date = Calendar.getInstance().getTime();
                detail.setLastUpdatedDate(new Timestamp(date.getTime()));
            }
            session.saveOrUpdate(detail);
            if(++count % 50 == 0){
                session.flush();
                session.clear();
            }
            //session.evict(detail);
        }
        session.flush();
        session.clear();
    }

    @Override
    public void bulkCreate(Collection<T> objList) {
        Session session = getSession();
        Date date = null;
        int count = 0;
        for (T detail : objList) {
            date = Calendar.getInstance().getTime();
            if(detail.getCreatedDate() == null){
                detail.setCreatedDate(new Timestamp(date.getTime()));
            }
            if(detail.getLastUpdatedDate() == null){
                detail.setLastUpdatedDate(new Timestamp(date.getTime()));
            }
            session.save(detail);
            if(++count % 50 == 0){
                session.flush();
                session.clear();
            }
        }
        session.flush();
        session.clear();
    }

    @Override
    public void create(T obj) {
        Date date = Calendar.getInstance().getTime();
        if(obj.getCreatedDate() == null){
            obj.setCreatedDate(new Timestamp(date.getTime()));
        }
        if(obj.getLastUpdatedDate() == null){
            obj.setLastUpdatedDate(new Timestamp(date.getTime()));
        }
        getSession().save(obj);
        getSession().flush();
    }

    @Override
    public void delete(Long id) {
        Date date = Calendar.getInstance().getTime();
        T obj = get(id);
        if (obj != null){
            if(obj.getLastUpdatedDate() == null){
                obj.setLastUpdatedDate(new Timestamp(date.getTime()));
            }
            obj.setStatus(Status.DELETED);
        }
        getSession().saveOrUpdate(obj);
        getSession().flush();
    }

    @Override
    public T getAndEvict(Long id) {
        T obj = get(id);
        evict(obj);
        return obj;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, T> get(Collection<Long> ids) {
        Map<Long, T> result = new LinkedHashMap<Long, T>();
        Criteria criteria = getSession().createCriteria(type);
        criteria.add(Restrictions.in("id", ids));
        List<T> objects = criteria.list();
        evictCollection(objects);
        if (objects != null) {
            for (T object : objects) {
                result.put(object.getId(), object);
            }
        }
        return result;
    }

    @Override
    public void update(T obj) {
        Date date = Calendar.getInstance().getTime();
        if(obj.getLastUpdatedDate() == null){
            obj.setLastUpdatedDate(new Timestamp(date.getTime()));
        }
        getSession().saveOrUpdate(obj);
    }

    @Override
    public void updateAndFlush(T obj) {
        Date date = Calendar.getInstance().getTime();
        if(obj.getLastUpdatedDate() == null){
            obj.setLastUpdatedDate(new Timestamp(date.getTime()));
        }
        Session session = getSession();
        session.saveOrUpdate(obj);
        session.flush();
    }

    @Override
    public void mergeAndFlush(T obj) {
        Date date = Calendar.getInstance().getTime();
        if(obj.getLastUpdatedDate() == null){
            obj.setLastUpdatedDate(new Timestamp(date.getTime()));
        }
        Session session = getSession();
        session.merge(obj);
        session.flush();
    }


    protected T evict(T obj) {
        if (obj != null) {
            getSession().evict(obj);
        }
        return obj;
    }

    protected void evictCollection(Collection<T> objects) {
        if (objects != null && !objects.isEmpty()) {
            for (T object : objects) {
                if (object != null) {
                    getSession().evict(object);
                }
            }
        }
    }

    protected void applyPagination(Map<Object, Object> paginationInfo, Criteria criteria) {
        if(paginationInfo != null){
            if(paginationInfo.get("searchField") != null) {
                if (!StringUtil.isNullOrEmpty(paginationInfo.get("searchText"))) {
                    criteria.add(Restrictions.like(paginationInfo.get("searchField").toString().trim(),
                            "%" + paginationInfo.get("searchText").toString().trim() + "%").ignoreCase());
                }
            }
            if(paginationInfo.containsKey("startIndex")) {
                if ((Integer) paginationInfo.get("startIndex") >= 0) {
                    criteria.setFirstResult((Integer) paginationInfo.get("startIndex"));
                    if ((Integer) paginationInfo.get("pageSize") > 0) {
                        criteria.setMaxResults((Integer) paginationInfo.get("pageSize"));
                    }
                }
            }
            if(paginationInfo.containsKey("from") && paginationInfo.containsKey("to")) {
                criteria.add(Restrictions.between("time", paginationInfo.get("from"), paginationInfo.get("to")));
            }
            if(paginationInfo.containsKey("sortInAscOrder") && paginationInfo.containsKey("sortAttribute")) {
                criteria.addOrder(((Boolean) paginationInfo.get("sortInAscOrder") ?
                        Order.asc(SortTypeEnum.valueOf(paginationInfo.get("sortAttribute").toString()).getFieldName()) :
                        Order.desc(SortTypeEnum.valueOf(paginationInfo.get("sortAttribute").toString()).getFieldName())));
            }
            /*if(paginationInfo.get("filters") != null) {
                Map<String, Object> filterMap = (Map<String, Object>) paginationInfo.get("filters");
                if(filterMap.size() > 0) {
                    for(int i = 0; i < filterMap.size(); i++) {
                        criteria.add(Restrictions.in(filterMap.keySet().toArray()[i].toString(),
                                (List) filterMap.values().toArray()[i]));
                    }
                }
            }*/
        }
    }
}
