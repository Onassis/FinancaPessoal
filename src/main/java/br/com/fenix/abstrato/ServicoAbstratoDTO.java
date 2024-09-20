package br.com.fenix.abstrato;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
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

public abstract class ServicoAbstratoDto<R extends CrudRepository<T,ID>,
										 T extends Persistable<ID>,
										 DTO extends Persistable<ID> ,ID> implements IServicoDTO< T,DTO, ID>   {
	
	@Autowired
	private EntityManagerFactory emf;
	
	
	   
	protected R repositorio  ;
	

	public ServicoAbstratoDto(R repositorio) {
		this.repositorio = repositorio;
	}
    @Override
    public EntityTransaction geradorTransacao() {
    	EntityManager em = emf.createEntityManager();
		return  em.getTransaction();
		
    }
 //   BeanUtils.copyProperties(car, carDto);
//
    @Override
    public T DTOtoEntidade (DTO dto) throws NegocioException {
    	T entidade = criarInstancia(); 
    	BeanUtils.copyProperties(dto,entidade);
    	return entidade; 
    }; 
    @Override
    public T DTOtoEntidade (DTO dto, T entidade) throws NegocioException {    	
    	BeanUtils.copyProperties(dto,entidade);
    	return entidade; 
    }; 
	@Override
	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
		return 	Optional.ofNullable(this.repositorio.findById(id)
				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
	}
	@Override
	public DTO buscaDTOPorId (ID id) throws RegistroNaoExisteException {
		T entidade = buscarPorId(id).get();
		return EntidadeToDTO(entidade);
	}
//	@Override
//	public Iterable<DTO> listar () throws RegistroNaoExisteException {
//		return null; //  this.repositorio.findAll() ;
//	}
	@Override
	public T criar(DTO dto)  throws Exception {
		EntityTransaction tx = geradorTransacao();
		T entidade= criarInstancia();
		try {				
			tx.begin();
			
		 	entidade = DTOtoEntidade(dto,entidade);
			
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
//
//
	@Override
	//	@Transactional
	public T atualizar(DTO dto)  throws Exception {	
		EntityTransaction tx = geradorTransacao();
		T entidade=null;
		try {				
			tx.begin();	
			entidade = buscarPorId(dto.getId()).get() ; 
			entidade = DTOtoEntidade(dto,entidade);
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
//	@Override
//	@Transactional
//	public void excluirTodos(){
//		repositorio.deleteAll();
//	}
}