package br.com.fenix.abstrato.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.fi.conta.Conta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;

public abstract class ServicoAbstrato<R extends CrudRepository<T,ID>,T ,ID> implements IServico< T,ID>   {
	
	@Autowired
	protected EntityManagerFactory emf;
	
	protected final  R repositorio  ;
	
	
//	abstract CrudRepository<T,ID> getRp();
	

	public ServicoAbstrato(R repositorio) {
		this.repositorio = repositorio;
	}
    @Override
    public EntityTransaction geradorTransacao() {
    	EntityManager em = emf.createEntityManager();
		return  em.getTransaction();
		
    }
    public R getRepositorio() {
    	return this.repositorio;
    }
	@Override
	public Page<T> listarPagina(Pageable pageable) {
		return null;
	}


	@Override
	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
		return 	Optional.ofNullable(this.repositorio.findById(id)
				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
	}
	@Override
	public Iterable<T> listar () throws RegistroNaoExisteException {
		return this.repositorio.findAll();
	}
	@Override
	public T criar( T entidade ) throws Exception {
		EntityTransaction tx = geradorTransacao();
		try {				
			tx.begin();
			entidade = antesDeSalvar(entidade);
			entidade =  repositorio.save (entidade);
			depoisDeSalvar(entidade);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			handleException(OperacaoDB.INS,e);
		}
		return entidade;		    
	}


	@Override
	public T atualizar(T entidade)  throws Exception {	
		EntityTransaction tx = geradorTransacao();
		try {				
			tx.begin();	
			entidade = antesDeAlterar(entidade);
			entidade =  repositorio.save (entidade);
			depoisDeSalvar(entidade);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			handleException(OperacaoDB.UPT,e);
		}
		return entidade;

	}
	@Override
	public void excluirPorId(ID id)throws Exception {
		EntityTransaction tx = geradorTransacao();
		try {				
			tx.begin();	
			antesDeExcluir(id);
			repositorio.deleteById(id);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			handleException(OperacaoDB.DEL,e);
		}
	}
	@Override
	@Transactional
	public void excluirTodos(){
		repositorio.deleteAll();
	}
	
	@Override
	public T  antesDeSalvar(T entidade) throws NegocioException {
		return entidade;
	}
	@Override
	public T antesDeAlterar(T entidade) throws NegocioException {		
		return entidade;
	}

	@Override
	public void depoisDeSalvar(T entidade) throws NegocioException {	
	}

	@Override
	public void depoisDeAlterar(T entidade) throws NegocioException {	
	}

	@Override
	public void antesDeExcluir(ID id) throws NegocioException {		
	}
}
