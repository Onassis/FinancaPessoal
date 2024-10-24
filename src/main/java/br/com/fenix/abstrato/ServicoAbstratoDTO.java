package br.com.fenix.abstrato;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.CrudRepository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public abstract class ServicoAbstratoDTO<R extends CrudRepository<T,ID>,
										 T extends Persistable<ID>,
										 DTO extends Persistable<ID> ,ID>
implements IServicoDTO< T,DTO, ID>   {
	
	@Autowired
	private EntityManagerFactory emf;
	

	protected R repositorio  ;
	
	private Class<T> entidadeClass;
	
	private Class<DTO> dtoClass;
	

	public ServicoAbstratoDTO(R repositorio) {
		this.repositorio = repositorio;
	}
	
	public void ServicoAbstratoDto(R repositorio, Class<T> entidadeClass, Class<DTO> dtoClass) {
//		super();
		this.repositorio = repositorio;
		this.entidadeClass = entidadeClass;
		this.dtoClass = dtoClass;
	}
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
//	@Override
//	public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
//		return 	Optional.ofNullable(this.repositorio.findById(id)
//				.orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
//	}
//	
//	@Override
//	public DTO buscaDTOPorId (ID id) throws RegistroNaoExisteException {
//		T entidade = buscarPorId(id).get();
//		return EntidadeToDTO(entidade);
//	}
//	@Override
//	public  List<DTO> listar () throws RegistroNaoExisteException {
//	       List<T> entidades  = new ArrayList<>();
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
//	}
//	@Override
//	public T criar(DTO dto)  throws Exception {
//		EntityTransaction tx = geradorTransacao();
//		T entidade= criarInstancia();
//		try {				
//			tx.begin();
//			
//		 	entidade = DTOtoEntidade(dto,entidade);
//			
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