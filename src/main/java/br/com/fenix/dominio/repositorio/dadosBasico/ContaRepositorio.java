package br.com.fenix.dominio.repositorio.dadosBasico;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.hibernate.type.TrueFalseConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.abstrato.GenericRepositoryAutenticado;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;

@Repository

public interface ContaRepositorio extends GenericRepository<Conta> {
	
	@Query("from Conta o where o.id = ?1 and o.criadoPor.id = ?#{ principal.id}")
	Optional<Conta> findById (Long id);

	@Query("from Conta o where o.tipoConta = ?1 and o.criadoPor.id = ?#{ principal.id}")
	List<Conta> findByTipoContaOrderByApelidoAsc(TipoConta tipoConta);

	@Query("from Conta o where  o.criadoPor.id = ?#{ principal.id} order by o.apelido")
	List<Conta> findByOrderByApelidoAsc();
	
	
	@Query("from Conta o where o.apelido = ?1 and o.criadoPor.id = ?#{ principal.id}")
	@Cacheable(value="conta", sync = true)
	Optional<Conta> findByApelido (String apelido);
	
	@Override
	@Query("from Conta o where o.criadoPor.id = ?#{ principal.id}")
	public List<Conta> findAll();  
	
	@Query("select COALESCE(sum(o.saldo),0) from Conta o where o.tipoConta = ?1 and o.criadoPor.id = ?#{ principal.id}")
	double TotalConta(TipoConta tipoConta);
	
	

}
 