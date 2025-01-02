package br.com.fenix.fi.moeda;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface MoedaRepositorio extends JpaRepository<Moeda,String> {
	@Cacheable(value="moeda")
	List<Moeda> findAllByOrderByCodigoAsc();
	
	
	@Cacheable(value="moeda")
	Optional<Moeda> findByCodigo(String codigo);
	
	
}
