package br.com.fenix.fi.moeda;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.dominio.modelo.AjudaSelect;
import br.com.fenix.dominio.modelo.Option;


@Repository

public interface MoedaRepositorio extends JpaRepository<Moeda,String> {
	@Cacheable(value="moeda")
	List<Moeda> findAllByOrderByCodigoAsc();
	
	
	@Cacheable(value="moeda")
	Optional<Moeda> findByCodigo(String codigo);
	
   
	 @Query("select  u.codigo as id, u.moeda as ajuda from Moeda u")
	 List<AjudaSelect> findAjudaSelectAll();
	 
	 @Query("select new br.com.fenix.dominio.modelo.Option(u.codigo, u.moeda) from Moeda u")
	 List<Option> findOptionAll();
	
}
