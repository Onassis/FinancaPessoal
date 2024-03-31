package br.com.fenix.dominio.repositorio.dadosBasico;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.fenix.dominio.modelo.DadoBasico.Moeda;


@Repository

public interface MoedaRepositorio extends JpaRepository<Moeda,String> {
	@Cacheable(value="moeda")
	List<Moeda> findAllByOrderByCodigoAsc();
	
	
	@Cacheable(value="moeda")
	Optional<Moeda> findByCodigo(String codigo);
	
	
}
