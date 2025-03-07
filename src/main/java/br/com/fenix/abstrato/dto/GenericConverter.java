package br.com.fenix.abstrato.dto;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class GenericConverter<E, D> implements Converter<E, D> {

    private final Class<D> dtoClass;
    private final Class<E> entityClass;

    public GenericConverter(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public D convertToDto(E entity) {
        try {
            D dto = createDto();
            BeanUtils.copyProperties(dto, entity);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting to DTO", e);
        }
    }

    @Override
    public E convertToEntity(D dto)  {
		   try {    	
            E entity = createEntity();
            BeanUtils.copyProperties(entity,dto);
            return entity;
	        } catch (Exception e) {
	            throw new RuntimeException("Error converting to DTO", e);
	        }
            
    }

	@Override
	public D createDto() {
		   try {
	            D dto = dtoClass.getDeclaredConstructor().newInstance();	        
	            return dto;
	        } catch (Exception e) {
	            throw new RuntimeException("Error converting to DTO", e);
	        }
	}

	@Override
	public E createEntity() {
	try {
	     E entity = entityClass.getDeclaredConstructor().newInstance();
     
         return entity;
     } catch (Exception e) {
         throw new RuntimeException("Error converting to Entity", e);
     }
	}

	@Override
	public E updateEntity(E entity, D dto) {
		   try {    	
	            BeanUtils.copyProperties(entity,dto);
	            return entity;
		   } catch (Exception e) {
		       throw new RuntimeException("Error converting to DTO", e);
		 }
	}
}
