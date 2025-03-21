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

import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public abstract class ServicoAbstratoDTO< T   extends Persistable,
										  DTO extends Persistable,
										 ID> 
					  					implements IServicoDTO< T,DTO, ID>   {
	
	private final Class<T> entidadeClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	private final Class<T> dtoClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

	@Autowired
	protected EntityManagerFactory emf;
	

	public abstract  CrudRepository<T,ID> getRp();
	
    public abstract  GenericConverter<T, DTO> getConverter(); 
	
//	public abstract  GenericConverter<T, DTO> getConverter(); 

	public ServicoAbstratoDTO() {
		super();
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
//		  Iterable<T>  dados = getRp().findAll();
//	      List<DTO> dtos = new ArrayList<>();
//	        for (T dado : dados ) {
//	            dtos.add(EntidadeToDTO(dado) );
//	        }
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
	

//    @Override
//    public T DTOtoEntidade (DTO dto) throws NegocioException {
//    	T entidade = criarInstancia(); 
//    	BeanUtils.copyProperties(dto,entidade);
//    	return entidade; 
//    }; 
//    @Override
//    public T DTOtoEntidade (DTO dto, T entidade) throws NegocioException {    	
//    	BeanUtils.copyProperties(dto,entidade);
//    	return entidade; 
//    }; 

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
			
			//T entidade = (T) entidadeOp.get(); 
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
		T entidade= criarInstancia();
//		entidade = DTOtoEntidade(dto,entidade);
//		return criar(entidade);
		return dto;
	}
//	@Override
//	public T criar( T entidade ) throws Exception {
//		EntityTransaction tx = geradorTransacao();
//		try {				
//			tx.begin();
//			entidade = antesDeSalvar(entidade);
//			entidade =  repositorio.save (entidade);
//			depoisDeSalvar(entidade);
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//			handleException(OperacaoDB.INS,e);
//		}
//		return entidade;		    
//	}

//	@Override
//	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
//		return 	Optional.ofNullable(this.repositorio.findById(id)
//				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
//	}
//	
////
////
//	@Override
//	public void excluirPorId(ID id)throws Exception {
//		EntityTransaction tx = geradorTransacao();
//		try {				
//			tx.begin();	
//			antesDeExcluir(id);
//			repositorio.deleteById(id);
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//			handleException(OperacaoDB.DEL,e);
//		}
//	}
//	@Override
//	@Transactional
//	public void excluirTodos(){
//		repositorio.deleteAll();
//	}
//  public EntityTransaction geradorTransacao() {
//	EntityManager em = emf.createEntityManager();
//	return  em.getTransaction();
//	
//}
//As a workaround, you can create an object of a type parameter through reflection:
//
//	public static <E> void append(List<E> list, Class<E> cls) throws Exception {
//	    E elem = cls.newInstance();   // OK
//	    list.add(elem);
//	}


}