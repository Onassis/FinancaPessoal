package br.com.fenix.fi.categoria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fenix.abstrato.dto.GenericConverter;
import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.dto.Node;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Option;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoDTO;
import br.com.fenix.fi.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.modeloCategoria.ModeloCategoria;
import br.com.fenix.fi.modeloCategoria.ModeloCategoriaRepositorio;
import br.com.fenix.fi.modeloCategoria.ModeloSubCategoria;
import br.com.fenix.fi.subCategoria.SubCategoria;
import br.com.fenix.fi.subCategoria.SubCategoriaMapper;
import br.com.fenix.fi.subCategoria.SubCategoriaRepositorio;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;


@Service
public class CategoriaServico extends ServicoAbstratoDTO<Categoria,CategoriaDTO,Long> implements IServicoDTO<Categoria,CategoriaDTO,Long> {

	@Autowired
	ModeloCategoriaRepositorio modeloCategoriaRP;
	@Autowired
	SubCategoriaRepositorio subCategoriaRP;
	@Autowired
	CategoriaRepositorio repositorio;
	@Autowired
	CategoriaMapper converter;
	@Autowired
	private SubCategoriaMapper converterSub;
	
    public CategoriaServico(EntityManagerFactory emf) {
    	super(emf);
	}
    @Override
    public JpaRepository<Categoria, Long> getRp() {
    	return repositorio;
    }
	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
	}
	
	@Override
	public CategoriaMapper getConverter() {
	
		return converter;
	}
 
	public List<Option>  listaDeCategoriasOpt(TipoLancamento tipoLancamento) {
		   List<Option> options =  listaDeCategorias(tipoLancamento)
		   		.stream()    
				.map(categoria  -> new Option(categoria.getId(), categoria.getDescricao()))
	            .collect(Collectors.toList());
			return options;			
	}	
    public List<CategoriaDTO> listaToDto ( Iterable<Categoria> categorias){
	   List<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>(); 
	   CategoriaDTO categoriaDTO = new CategoriaDTO();
		 	 
		 for(Categoria categoria : categorias) {      
			categoriaDTO = converter.convertToDto(categoria); 
			categoriasDTO.add(categoriaDTO);
	   	 	for(SubCategoria subCategoria : categoria.getSubCategoria()) { 
	   	 		 CategoriaDTO subCategoriaDTO = converterSub.convertToDto(subCategoria); 
	 	    	 		if (!categoriasDTO.contains(subCategoriaDTO )) { 
	 	    	 			categoriasDTO.add(subCategoriaDTO );
	 	    	 		}
	 		 }	 
		}		 			 
		 return categoriasDTO ; 
   }
//   public CategoriaDTO buscarCategoriaPorId (@PathVariable  long id){
//       
//	   Optional<Categoria>  entidadeOp  = Optional.ofNullable(repositorio.findById(id)
//			   .orElseThrow ( () -> new RegistroNaoExisteException("Registro não encontrado Id:" + id)));
//   	   	   
//		   CategoriaDTO dto = converter.convertToDto(entidadeOp.get()); 
//         return dto;       		    			       		    	
//   }
   public List<CategoriaDTO> listaDeCategorias(TipoLancamento tipoLancamento) {

	 Iterable<Categoria> categorias ; 
	 if (tipoLancamento == null) 
		  categorias = repositorio.findByAllOrderByDescricaoAsc(); 
	 else
		  categorias = repositorio.findByTipoLancamentoOrderByDescricaoAsc(tipoLancamento);
	 	 
	 return listaToDto(categorias); 
	}

   public List<CategoriaDTO> listaPorTipoCategorias(TipoCategoria tipo) {

	 List<Categoria> categorias ; 
	 
	 if (tipo == null) 
		  categorias = repositorio.findByAllOrderByDescricaoAsc(); 
	 else
		  categorias = repositorio.findByTipoCategoriaOrderByDescricaoAsc(tipo);
	 	 
	 return listaToDto(categorias); 
	}


	@Transactional(isolation = Isolation.DEFAULT)
	public  void  CriarPorModelo(TipoLancamento tipoLancamento) {
		System.out.println("Criar por modelo");
		
		List<Categoria> categorias = new ArrayList<>();

		 Iterable<ModeloCategoria> modeloCategorias = modeloCategoriaRP.findAll(); 
	 	 
		 for(ModeloCategoria modeloCategoria : modeloCategorias) {
		
			Categoria categoria =  new Categoria(modeloCategoria);
			
		    List<SubCategoria> subCategoriaLista = new ArrayList<>();
	    	   
	         for (ModeloSubCategoria modeloSubCat: modeloCategoria.getSubModeloCategoria()) {
	        	   subCategoriaLista.add( new SubCategoria(categoria, modeloSubCat)); 
	           }
		     System.out.println("Salvar categorias");
		     repositorio.save(categoria);
	         subCategoriaRP.saveAll(subCategoriaLista); 
		 }
		 
}	
 
//	public void excluirPorIdSub(Long id)throws Exception {
//		EntityTransaction tx = geradorTransacao();
//		try {				
//			tx.begin();	
//			antesDeExcluir(id);
//			repositorio.deleteSubCategoria(id);
//			tx.commit();
//		} catch (Exception e) {
//			tx.rollback();
//			handleException(OperacaoDB.DEL,e);
//		}
//	}
/*	
	public ArrayList<CategoriaDTO> listaDeSubCategorias(TipoLancamento tipoLancamento) {

		 ArrayList<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>(); 
		 CategoriaDTO categoriaDTO;
		 
		 Iterable<Categoria> categorias = categoriaRP.findByTipoLancamentoOrderByDescricaoAsc(tipoLancamento);

		 for(Categoria categoria : categorias) {       	 	
				 for(SubCategoria subCategoria : categoria.getSubCategoria()) { 
					 
					 categoriaDTO =  new CategoriaDTO(subCategoria.getId(),
							 				 						categoria.getDescricao()
							 				 						.concat( " ->" )
							 				 						.concat(subCategoria.getDescricao()) ,
							 				 						subCategoria.getTipoLancamento(),
							 				 						categoria.getId())	;
				  if (!categoriasDTO.contains(categoriaDTO)) { 
					  categoriasDTO.add(categoriaDTO);
				  }

			 }
		 
		 }		 
		 return categoriasDTO ; 
		}
*/



}
