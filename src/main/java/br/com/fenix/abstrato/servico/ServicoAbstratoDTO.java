package br.com.fenix.abstrato.servico;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.fi.categoria.Categoria;
import br.com.fenix.fi.categoria.CategoriaDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;


public abstract class ServicoAbstratoDTO< T   extends Persistable,
										  DTO extends Persistable,
										 ID> 
					  					implements IServicoDTO< T,DTO, ID>   {
	
	private final Class<T> entidadeClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	private final Class<T> dtoClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

	@PersistenceUnit
	protected final EntityManagerFactory emf;
	

	public abstract  CrudRepository<T,ID> getRp();
	
    public abstract   Converter<T, DTO> getConverter(); 
	

	public ServicoAbstratoDTO(EntityManagerFactory emf) {
		super();
		this.emf = emf;
	
	}
    @Override
    public EntityTransaction geradorTransacao() {
    	EntityManager em = emf.createEntityManager();
		return  em.getTransaction();		
	  }
	@Override
	public Page<T> listarPagina(Pageable pageable) {
		return null;
	}


	@Override
	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
		return 	Optional.ofNullable(getRp().findById(id)
				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
	}
	@Override
	public Iterable<T> listar () throws RegistroNaoExisteException {
		System.err.println("Listar");
		return getRp().findAll();
	}
	@Override
	public  List<DTO> listarDto () throws RegistroNaoExisteException {
	    List<DTO> dtos = StreamSupport.stream(listar().spliterator(), false)
                .map(dado -> getConverter().convertToDto(dado))
                .collect(Collectors.toList());
        return dtos;
	}

	@Override
	public T atualizar(T entidade)  throws Exception {	
		EntityTransaction tx = geradorTransacao();
		try {				
			tx.begin();	
			entidade = antesDeAlterar(entidade);
			entidade =  getRp().save (entidade);
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
			getRp().deleteById(id);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			handleException(OperacaoDB.DEL,e);
		}
	}
	@Override
	@Transactional
	public void excluirTodos(){
		getRp().deleteAll();
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

	
	public T criarInstancia() {
		  try {
			 return   entidadeClass.getDeclaredConstructor().newInstance(); 
          } catch (Exception e) {
              System.out.println(e.getMessage());
              return null;
          }
	 }
	public DTO buscaDTOPorId (ID id) throws RegistroNaoExisteException {
		   Optional<T>  entidadeOp = buscarPorId(id);
		    DTO dto =  getConverter().convertToDto(entidadeOp.get()); 
		return dto;
	}

	@Override
	public DTO atualizarDTO(DTO dto)  throws Exception {	
		EntityTransaction tx = geradorTransacao();
        T entidade=null;
		try {				
			 tx.begin();	
			 Optional<T>  entidadeOp = buscarPorId((ID) dto.getId());
			 entidade = getConverter().updateEntity(entidadeOp.get(),dto); 
			 entidade = atualizar(entidade);		
			 depoisDeSalvar(entidade);
			 tx.commit();
		} catch (Exception e) {
			tx.rollback();
			handleException(OperacaoDB.UPT,e);
		}
		dto = getConverter().convertToDto(entidade); 
		return dto;

	}

	@Override
	public DTO criarDTO(DTO dto)  throws Exception {
		System.out.println("ServicoAbstratoDTO -> Crair ");
		T entidade = getConverter().convertToEntity(dto);

		entidade = criar(entidade);
		return getConverter().convertToDto(entidade);
	}
	@Override
	public T criar( T entidade ) throws Exception {
		EntityTransaction tx = geradorTransacao();
		try {				
			tx.begin();
			entidade = antesDeSalvar(entidade);
			entidade = getRp().save (entidade);
			depoisDeSalvar(entidade);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			handleException(OperacaoDB.INS,e);
		}
		return entidade;		    
	}
}