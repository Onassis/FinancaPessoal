package br.com.fenix.abstrato.base;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericDTO<T> {

	// Método abstrato para converter uma entidade em DTO
	public abstract T toEntity();

	// Método abstrato para criar um DTO a partir de uma entidade
	public abstract GenericDTO<T> fromEntity(T entity);

	// Método para converter uma lista de entidades em uma lista de DTOs
	public static <T, D extends GenericDTO<T>> List<D> fromEntityList(List<T> entityList, Class<D> dtoClass) {
		return (List<D>) entityList.stream().map(entity -> {
			try {
				D dto = dtoClass.getDeclaredConstructor().newInstance();
				return dto.fromEntity(entity);
			} catch (Exception e) {
				throw new RuntimeException("Erro ao criar DTO", e);
			}
		}).collect(Collectors.toList());
	}
}
