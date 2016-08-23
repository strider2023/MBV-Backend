package com.mbv.api.filter.pagination;

import com.mbv.api.util.StringUtil;
import com.mbv.persist.enums.SortTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaginationMapper {
	
	public static final Integer DEFAULT_OFFSET = 0;
	
	public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static final String DEFAULT_SEARCH_PARAM = null;
	
	public static final String DEFAULT_SEARCH_TEXT = null;
	
	public static final Boolean DEFAULT_SORT_IN_ASC_ORDER = false;
	
	public static final String DEFAULT_SORT_FIELD = SortTypeEnum.lastUpdateDate.toString();

    public static final String DEFAULT_FILTERS = null;
	
	public static Map<Object, Object> mapPaginationInfo(Integer offset, Integer pageSize, String searchField, String searchText, String sortAttribute, Boolean sortInAscOrder){
		
		Map<Object, Object> paginationInfo = new HashMap<Object, Object>();

		if(StringUtil.isNullOrEmpty(offset)){
			offset = DEFAULT_OFFSET;
		}
		
		if(StringUtil.isNullOrEmpty(pageSize)){
			pageSize = DEFAULT_PAGE_SIZE;
		}

        if(StringUtil.isNullOrEmpty(searchField)) {
            searchField = DEFAULT_SEARCH_PARAM;
        }
		
		if(StringUtil.isNullOrEmpty(searchText)){
			searchText = DEFAULT_SEARCH_TEXT;
		}
		
		if(StringUtil.isNullOrEmpty(sortAttribute)){
			sortAttribute = DEFAULT_SORT_FIELD;
		}
		
		if(StringUtil.isNullOrEmpty(sortInAscOrder)){
			sortInAscOrder = DEFAULT_SORT_IN_ASC_ORDER;
		}
		
		paginationInfo.put("offset", offset);
		paginationInfo.put("pageSize", pageSize);
        paginationInfo.put("searchField", searchField);
		paginationInfo.put("searchText", searchText);
		paginationInfo.put("sortAttribute", sortAttribute);
		paginationInfo.put("sortInAscOrder", sortInAscOrder);
		paginationInfo.put("startIndex", (offset * pageSize));
		return paginationInfo;		
	}

    public static Map<String, Object> getFilters(String filter){
        Map<String, Object> filters = new HashMap<>();
        if(StringUtil.isNullOrEmpty(filter)){
            return null;
        }
        //eg., advertisers:9182;8373+status:4,9
        String[] filterList = filter.split(";");
        for(String indFilter : filterList){
            String key = indFilter.split(":")[0];
            String[] values = indFilter.split(":")[1].split(",");
            filters.put(key, Arrays.asList(values));
        }
        return filters;
    }
}
