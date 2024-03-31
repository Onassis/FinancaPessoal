	package br.com.fenix.dominio.servico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.DadosModelo.ModeloSubCategoria;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.Node;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.MasterCategoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.ModeloCategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.SubCategoriaRepositorio;

@Service
public class CategoriaServico {

    @Repository
    public interface ModeloSubCategoriaRepositorio extends GenericRepository<ModeloSubCategoria> {

    }

	private final CategoriaRepositorio categoriaRP;
	private final ModeloCategoriaRepositorio modeloCategoriaRP;
	private final SubCategoriaRepositorio subCategoriaRP;


    CategoriaServico(CategoriaRepositorio categoriaRP, ModeloCategoriaRepositorio modeloCategoriaRP , SubCategoriaRepositorio modSubCat ) {
    	this.modeloCategoriaRP = modeloCategoriaRP;
        this.categoriaRP = categoriaRP;
		this.subCategoriaRP = modSubCat;
    } 

	@Transactional(isolation = Isolation.DEFAULT)
//	public  ArrayList<CategoriaDTO>  CriarPorModelo(TipoLancamento tipoLancamento) {
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
	         categoriaRP.save(categoria);
	         subCategoriaRP.saveAll(subCategoriaLista); 
		 }
	 
//		return listaDeCategorias(tipoLancamento) ; 
		 
		 
}	

    public ArrayList<CategoriaDTO> listaDeCategorias(TipoLancamento tipoLancamento) {

	 ArrayList<CategoriaDTO> categoriasDTO = new ArrayList<CategoriaDTO>(); 
	 Iterable<Categoria> categorias ; 
	 
	 CategoriaDTO categoriaDTO = new CategoriaDTO();
	 if (tipoLancamento == null) 
		  categorias = categoriaRP.findByAllOrderByDescricaoAsc(); 
	 else
		  categorias = categoriaRP.findByTipoLancamentoOrderByDescricaoAsc(tipoLancamento);
	 	 
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
}
