package br.com.fenix.abstrato.dto;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;


public class GenericConverter<T, D> implements Converter<T, D> {

    private final Class<D> dtoClass;
    private final Class<T> entityClass;

    public GenericConverter(Class<T> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public D ToDto(T entity) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();;
            BeanUtils.copyProperties(dto, entity);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting to DTO", e);
        }
    }

    @Override
    public T ToEntity(D dto)  {
		   try {    	
            T entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity,dto);
            return entity;
	        } catch (Exception e) {
	            throw new RuntimeException("Error converting to DTO", e);
	        }
            
    }

//	@Override
//	public D createDto() {
//		   try {
//	            D dto = dtoClass.getDeclaredConstructor().newInstance();	        
//	            return dto;
//	        } catch (Exception e) {
//	            throw new RuntimeException("Error converting to DTO", e);
//	        }
//	}
//
//	@Override
//	public T createEntity() {
//	try {
//	     T entity = entityClass.getDeclaredConstructor().newInstance();
//     
//         return entity;
//     } catch (Exception e) {
//         throw new RuntimeException("Error converting to Entity", e);
//     }
//	}

	@Override
	public void updateEntity(D dto , T entity ) {
		   try {    	
	            BeanUtils.copyProperties(entity,dto);
//	            return entity;
		   } catch (Exception e) {
		       throw new RuntimeException("Error converting to DTO", e);
		 }
	}
}
