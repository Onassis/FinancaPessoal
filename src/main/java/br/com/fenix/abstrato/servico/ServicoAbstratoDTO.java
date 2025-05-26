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
	

	public ServicoAbstratoDTO(EntityManagerFactory emf) {
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
	    List<DTO> dtos = listar()
	    		.stream()
                .map(dado -> getConverter().ToDto(dado))
                .collect(Collectors.toList());
        return dtos;
	}

	@Override
	@Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED)
// Verifica se o usuario é o dono do registro	
//	@PreAuthorize("#entidade.criadoPor.id == principal.id")
	public T atualizar(T entidade)  throws Exception {	
//		EntityTransaction tx = geradorTransacao();
//		Session session = sessionFactory.openSession(); // (2)
		try {				
//			tx.begin();
//			session.getTransaction().begin();
			entidade = antesDeAlterar(entidade);
			entidade =  getRp().saveAndFlush(entidade);
			depoisDeSalvar(entidade);
//			session.getTransaction().commit();

//			tx.commit();
		} catch (Exception e) {
//			session.getTransaction().rollback();
//			tx.rollback();
			handleException(OperacaoDB.UPT,e);
		}
		return entidade;

	}
	@Override
	@Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED)
	public void excluirPorId(ID id)throws Exception {
//		EntityTransaction tx = geradorTransacao();
		try {				
//			tx.begin();	
			antesDeExcluir(id);
			getRp().deleteById(id);
//			tx.commit();
		} catch (Exception e) {
//			tx.rollback();
			handleException(OperacaoDB.DEL,e);
		}
	}
	@Override
	@Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED)
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

	@Override
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
	public DTO atualizarDTO(DTO dto)  throws Exception {	
//		EntityTransaction tx = geradorTransacao();
//        T entidade=null;
		try {				
//			 tx.begin();	
			ID id = (ID) dto.getId(); 
//			 Optional<T>  entidadeOp = buscarPorId(id).orElseThrow();
			 T entidade = buscarPorId(id).orElseThrow();
			 getConverter().updateEntity(dto,entidade); 
			 entidade = atualizar(entidade);		
			 depoisDeSalvar(entidade);
//			 tx.commit();
		
			 dto = getConverter().ToDto(entidade); 
			return dto;

		} catch (Exception e) {
//			tx.rollback();
			handleException(OperacaoDB.UPT,e);
		}
	  return dto;
	}

	@Override
	public DTO criarDTO(DTO dto)  throws Exception {
		System.out.println("ServicoAbstratoDTO -> Crair ");
		T entidade = getConverter().ToEntity(dto);

		entidade = criar(entidade);
		return getConverter().ToDto(entidade);
	}
	@Override
	@Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED)
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