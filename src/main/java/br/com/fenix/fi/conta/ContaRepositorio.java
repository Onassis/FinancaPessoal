package br.com.fenix.fi.conta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.hibernate.type.TrueFalseConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.abstrato.GenericRepositoryAutenticado;
import br.com.fenix.dominio.enumerado.TipoConta;

@Repository
public interface ContaRepositorio extends CrudRepository<Conta,Long> {
	
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
	
	@Query("select count(distinct c.id) = 1 from Conta c inner join DetalheLancamento d on d.contaLancamento = c.id where c.id = ?1")
	boolean existsByContaLancamento (Long id);
}
 