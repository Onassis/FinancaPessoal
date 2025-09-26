package br.com.fenix.abstrato.servico;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

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
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.relational.SchemaManager;
import org.hibernate.stat.Statistics;
import org.hibernate.Session;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManagerFactory;

public abstract class ServicoAbstratoDTO< T   extends Persistable,
										  DTO extends Persistable,
										 ID> 
					  					implements IServicoDTO< T,DTO, ID>   {
	
	private final Class<T> entidadeClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	private final Class<T> dtoClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

//	@PersistenceUnit
//	protected final EntityManagerFactory emf;
// 
//
//	 @PersistenceContext
//	  EntityManager entityManager;
//
//	 @Autowired
//	 private SessionFactory sessionFactory; 

	public abstract  JpaRepository<T,ID> getRp();
	
	
    public abstract   Converter<T, DTO> getConverter(); 
	

	public ServicoAbstratoDTO() {
		super();
//		this.emf = emf;
	
	}
    @Override
    public EntityTransaction geradorTransacao() {
//    	EntityManager em = emf.createEntityManager();
//		return  entityManager.getTransaction();
		return null;
	  }
	@Override
	public Page<T> listarPagina(Pageable pageable) {
		return null;
	}


	@Override
	@Transactional(readOnly = true)
	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
		return 	Optional.ofNullable(getRp().findById(id)
				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
	}
	@Override
	@Transactional(readOnly = true)
	public List<T> listar () throws RegistroNaoExisteException {
		System.err.println("Listar");
		return getRp().findAll();
	}
	@Override
	public  List<DTO> listarDto () throws RegistroNaoExisteException {
//	    List<DTO> dtos = listar()
//	    		.stream()
//                .map(dado -> getConverter().ToDto(dado))
//                .collect(Collectors.toList());
        return listarDto(listar()); 
	}
	@Override
	public  List<DTO> listarDto (List<T> dados) throws RegistroNaoExisteException {
	    List<DTO> dtos = dados 
	    		.stream()
                .map(dado -> getConverter().ToDto(dado))
                .collect(Collectors.toList());
        return dtos;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED)
//	@PreAuthorize("#entidade.criadoPor.id == principal.id")
	public T atualizar(T entidade)  throws Exception {	
		try {				
			entidade = antesDeAlterar(entidade);
			entidade =  getRp().saveAndFlush(entidade);
			depoisDeSalvar(entidade);
		} catch (Exception e) {

			handleException(OperacaoDB.UPT,e);
		}
		return entidade;

	}
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public void excluirPorId(ID id)throws Exception {
		try {				
			antesDeExcluir(id);
			getRp().deleteById(id);
			
		} catch (Exception e) {
			handleException(OperacaoDB.DEL,e);
		}
	}
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public void excluirTodos(){
		getRp().deleteAll();
	}
	
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T  antesDeSalvar(T entidade) throws NegocioException {
		return entidade;
	}
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T antesDeAlterar(T entidade) throws NegocioException {		
		return entidade;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void depoisDeSalvar(T entidade) throws NegocioException {	
	}
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void depoisDeAlterar(T entidade) throws NegocioException {	
		
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void depoisDeAlterar(T entidade, DTO dto) throws NegocioException {	
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void antesDeExcluir(ID id) throws NegocioException {		
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleException(OperacaoDB op,Exception e) throws Exception {
 	  throw e ;		
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
		   T  entidade = buscarPorId(id).orElseThrow(() -> new RegistroNaoExisteException("Registro  não encontrato:" + id) );
		    DTO dto =  getConverter().ToDto(entidade); 
		return dto;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED)
	public DTO atualizarDTO(DTO dto)  throws Exception {	
//		EntityTransaction tx = geradorTransacao();
//        T entidade=null;
		try {				
			ID id = (ID) dto.getId(); 
			 T entidade = buscarPorId(id).orElseThrow();
			 getConverter().updateEntity(dto,entidade); 
			 entidade = atualizar(entidade);
			 depoisDeAlterar(entidade, dto);
			 depoisDeSalvar(entidade);
		
			 dto = getConverter().ToDto(entidade); 
			return dto;

		} catch (Exception e) {
//			tx.rollback();
			handleException(OperacaoDB.UPT,e);
		}
	  return dto;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED)
	public DTO criarDTO(DTO dto)  throws Exception {
		System.out.println("ServicoAbstratoDTO -> Crair ");
		T entidade = getConverter().ToEntity(dto);

		entidade = criar(entidade);
		return getConverter().ToDto(entidade);
	}
	@Override
	@Transactional(propagation = Propagation.NESTED)
	public T criar( T entidade ) throws Exception {
//		EntityTransaction tx = geradorTransacao();
		try {				
//			tx.begin();
			entidade = antesDeSalvar(entidade);
			entidade = getRp().saveAndFlush(entidade);
			depoisDeSalvar(entidade);
//			tx.commit();
		} catch (Exception e) {
//			tx.rollback();
			handleException(OperacaoDB.INS,e);
		}
		return entidade;		    
	}
}