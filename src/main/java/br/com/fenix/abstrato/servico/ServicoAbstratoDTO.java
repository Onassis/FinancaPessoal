package br.com.fenix.abstrato.servico;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public abstract class ServicoAbstratoDTO<R extends CrudRepository<T,ID>,
										 T extends Persistable<ID>,
										 DTO extends Persistable<ID> ,ID>
               //       extends ServicoAbstrato<R,T ,ID>  
					  implements IServicoDTO< T,DTO, ID>   {
	
	@Autowired
	protected EntityManagerFactory emf;
	
	protected final R repositorio;
	
	
	private final Class<T> entidadeClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

	private final Class<T> dtoClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];

	public ServicoAbstratoDTO(R repositorio) {
		super();
		this.repositorio = repositorio;		
	}
	public ServicoAbstratoDTO(R repositorio, EntityManagerFactory emf ) {
			this.repositorio = repositorio;
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
		return 	Optional.ofNullable(this.repositorio.findById(id)
				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
	}
	@Override
	public Iterable<T> listar () throws RegistroNaoExisteException {
		System.err.println("Listar");
		return repositorio.findAll();
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

	
//	public void ServicoAbstratoDto(R repositorio, Class<T> entidadeClass, Class<DTO> dtoClass) {
////		super();
//		this.repositorio = repositorio;
//		this.entidadeClass = entidadeClass;
//		this.dtoClass = dtoClass;
//	}
//    public EntityTransaction geradorTransacao() {
//    	EntityManager em = emf.createEntityManager();
//		return  em.getTransaction();
//		
//    }
//	As a workaround, you can create an object of a type parameter through reflection:
//
//		public static <E> void append(List<E> list, Class<E> cls) throws Exception {
//		    E elem = cls.newInstance();   // OK
//		    list.add(elem);
//		}
	
	public T criarInstancia() {
		  try {
			 return   entidadeClass.getDeclaredConstructor().newInstance(); 
          } catch (Exception e) {
              System.out.println(e.getMessage());
              return null;
          }
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
//	@Override
//	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
//		return 	Optional.ofNullable(this.repositorio.findById(id)
//				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
//	}
//	
	@Override
	public DTO buscaDTOPorId (ID id) throws RegistroNaoExisteException {
		T entidade = buscarPorId(id).get();
		return EntidadeToDTO(entidade);
	}
	@Override
	public  List<DTO> listarDto () throws RegistroNaoExisteException {
	       
		  Iterable<T>  dados = repositorio.findAll();
	      List<DTO> dtos = new ArrayList<>();
	        for (T dado : dados ) {
	            dtos.add(EntidadeToDTO(dado) );
	        }
	        return dtos;
//						
//	       repositorio.findAll().forEach(entidades::add);
//	   
//	        return entidades.stream().map(entity -> {
//	            DTO dto = null;
//	            try {
//	                dto = dtoClass.getDeclaredConstructor().newInstance();
//	                BeanUtils.copyProperties(entity, dto);
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	            return dto;
//	        }).collect(Collectors.toList());
//	    
//// 		  Iterable<T>  dados = this.repositorio.findAll();
////          List<DTO> dtos = new ArrayList<>();
////	        for (T dado : dados ) {
////	            dtos.add(EntidadeToDTO(dado) );
////	        }
////	        return dtos;
//								
//		
	}
	@Override
	public T criar(DTO dto)  throws Exception {
		T entidade= criarInstancia();
		entidade = DTOtoEntidade(dto,entidade);
		return criar(entidade);
//		EntityTransaction tx = geradorTransacao();
//		T entidade= criarInstancia();
//		try {				
//			tx.begin();
//			
//		 	entidade = DTOtoEntidade(dto,entidade);
//			criar(entidade);
//			entidade = antesDeSalvar(entidade);
//			entidade =  repositorio.save (entidade);
//			depoisDeSalvar(entidade);
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//			handleException(OperacaoDB.INS,e);
//		}
//		return entidade;		    
	}
////
////
//	@Override
//	//	@Transactional
//	public T atualizar(DTO dto)  throws Exception {	
//		EntityTransaction tx = geradorTransacao();
//		T entidade=null;
//		try {				
//			tx.begin();	
//			entidade = buscarPorId(dto.getId()).get() ; 
//			entidade = DTOtoEntidade(dto,entidade);
//			entidade = antesDeAlterar(entidade);
//			entidade =  repositorio.save (entidade);
//			depoisDeSalvar(entidade);
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//			handleException(OperacaoDB.UPT,e);
//		}
//		return entidade;
//
//	}
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
}