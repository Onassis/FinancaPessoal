package br.com.fenix.abstrato.servico;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import br.com.fenix.abstrato.base.AbstrataDTO;
import br.com.fenix.dominio.enumerado.OperacaoDB;

public class ServicoDTO <R extends CrudRepository<T,ID>,T, 
				DTO ,ID> extends ServicoAbstrato<R,T,ID> implements IServicoDTO<T,DTO,ID> {

//	private final Class<T> DTOClass = 
//			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
	private final Class<DTO> dtoClass = 
			(Class<DTO>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
	
	public ServicoDTO(R repositorio) {
		super(repositorio);
	}

	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<DTO> listarDto() {
		// TODO Auto-generated method stub
		return null;
	}


	
	public List<DTO> listarDto2() {
//		// TODO Auto-generated method stub
		Iterable<T> lista = repositorio.findAll();
		List<DTO> listaDto = StreamSupport.stream(lista.spliterator(), false).map(entidade -> {
			try {
				// Invoca o construtor do DTO passando a entidade
				return dtoClass.getConstructor(entidade.getClass()).newInstance(entidade);
			} catch (Exception e) {
				throw new RuntimeException("Erro ao converter entidade para DTO", e);
			}
		}).collect(Collectors.toList());
		return listaDto;
	}		
//		return  
//				((Collection<DTO>) StreamSupport.stream(lista.spliterator(), false))
//				
//				
//				.stream()
//				.map(Class<DTO>::new) // Construtor do DTO recebe a entidade
//	            .collect(Collectors.toList());
////				.stream()
////				.map(entidade -> {
////                    try {
////                        // Invoca o construtor do DTO passando a entidade
////                        return dtoClass.getConstructor().newInstance(entidade);
////                    } catch (Exception e) {
////                        throw new RuntimeException("Erro ao converter entidade para DTO", e);
////                    }
////                })
////                     
////                .collect(Collectors.toList()));
//		
				

//    public <T extends Persistable<ID>, ID, D extends AbstrataDTO<T, ID>> List<D> listarTodos(Class<D> dtoClass) {
//        return repositorio.findAll()
//                .stream().spliterator()
//				.forEach(entidade -> {
//					try {
//						// Invoca o construtor do DTO passando a entidade
//						return dtoClass.getConstructor(entidade.getClass()).newInstance(entidade);
//					} catch (Exception e) {
//						throw new RuntimeException("Erro ao converter entidade para DTO", e);
//					}
//				})
//                .collect(Collectors.toList());
//    }


}
