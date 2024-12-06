package br.com.fenix.fi.categoria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.DadosModelo.ModeloSubCategoria;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.Node;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.modeloCategoria.ModeloCategoriaRepositorio;

@Service
public class CategoriaServico extends ServicoAbstrato<CategoriaRepositorio,Categoria,Long> implements IServico<Categoria,Long> {


	@Repository
    public interface ModeloSubCategoriaRepositorio extends GenericRepository<ModeloSubCategoria> {

    }
	

	@Autowired
	ModeloCategoriaRepositorio modeloCategoriaRP;
	@Autowired
	SubCategoriaRepositorio subCategoriaRP;

    public CategoriaServico(CategoriaRepositorio repositorio) {
		super(repositorio);
	}
    
 
	public List<Option>  listaDeCategoriasOpt(TipoLancamento tipoLancamento) {
		   List<Option> options =  listaDeCategorias(tipoLancamento)
		   		.stream()    
				.map(categoria  -> new Option(categoria.getId(), categoria.getDescricao()))
	            .collect(Collectors.toList());
			return options;			
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

    public ArrayList<CategoriaDTO> listaDeCategorias(TipoLancamento tipoLancamento) {

	 ArrayList<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>(); 
	 Iterable<Categoria> categorias ; 
	 
	 CategoriaDTO categoriaDTO = new CategoriaDTO();
	 if (tipoLancamento == null) 
		  categorias = repositorio.findByAllOrderByDescricaoAsc(); 
	 else
		  categorias = repositorio.findByTipoLancamentoOrderByDescricaoAsc(tipoLancamento);
	 	 
	 for(Categoria categoria : categorias) {      
		 
		 if (categoria.getSubCategoria().isEmpty()) {
			 categoriasDTO.add(categoria.categoria_DTO());
		 }
		 else {
 	    	 	for(MasterCategoria subCategoria : categoria.getSubCategoria()) { 
				 
 	    	 		categoriaDTO =  subCategoria.categoria_DTO();
 	    	 		if (!categoriasDTO.contains(categoriaDTO)) { 
 	    	 			categoriasDTO.add(categoriaDTO);
 	    	 		}
 	    	 	}	 
		 	}
	 	}
	 
	 return categoriasDTO ; 
	}
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
	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
		// TODO Auto-generated method stub
		
	}			
}
