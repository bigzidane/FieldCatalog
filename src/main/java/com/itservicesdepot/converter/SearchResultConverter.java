package com.itservicesdepot.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.ConfigurableCustomConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

import com.itservicesdepot.model.Field;
import com.itservicesdepot.model.Product;
import com.itservicesdepot.model.Screen;
import com.itservicesdepot.model.SearchResult;
import com.itservicesdepot.utils.ValidateUtils;

public class SearchResultConverter  implements ConfigurableCustomConverter, MapperAware {

	private Logger log = Logger.getLogger(SearchResultConverter.class);
	private Mapper mapper;
	
	@Override
	public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
		List<SearchResult> dest = null;
		if (ValidateUtils.isObjectNotEmpty(sourceFieldValue)) {
			try {
				if (sourceFieldValue instanceof ArrayList){
					@SuppressWarnings("rawtypes")
					ArrayList srcList = (ArrayList) sourceFieldValue;
					
					if (srcList.size() > 0) {
						dest = new ArrayList<SearchResult>();
						for (Object o : srcList) {
							
							SearchResult searchResult = this.mapper.map(o, SearchResult.class);
							if (o instanceof Screen) {
								searchResult.setType("Screen");
							}
							else if (o instanceof Field) {
								searchResult.setType("Field");
							}
							else if (o instanceof Product) {
								searchResult.setType("Product");
							}
							dest.add(searchResult);
						}
						
					}
				}
				
			}
			catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return dest;
	}

	@Override
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void setParameter(String parameter) {
	}

	public Mapper getMapper() {
		return mapper;
	}
	
}