package br.com.fenix.abstrato;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepositoryDTO<T,DTO, ID>  extends CrudRepository<T, ID> {
	
//	  <T> Collection<T> findByLastname(String lastname, Class<T> type); 
//
//	  Collection<Person> aggregates =
//	    people.findByLastname("Matthews", Person.class);
//
//	  Collection<NamesOnly> aggregates =
//	    people.findByLastname("Matthews", NamesOnly.class);
//	  
	<DTO> Iterable<DTO>  findByIdDTO(ID id); 
	
	<DTO> Iterable<DTO>  findAllDTO(); 
	
	
}
