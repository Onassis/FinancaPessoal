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
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.dto.Node;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.favorecido.Favorecido;
import br.com.fenix.fi.favorecido.FavorecidoRepositorio;
import br.com.fenix.fi.modeloCategoria.ModeloCategoria;
import br.com.fenix.fi.modeloCategoria.ModeloCategoriaRepositorio;
import br.com.fenix.fi.modeloCategoria.ModeloSubCategoria;


@Service
public class CategoriaServico extends ServicoAbstratoDTO<CategoriaRepositorio,Categoria,CategoriaDTO,Long> implements IServicoDTO<Categoria,CategoriaDTO,Long> {

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


//	@Override
//	public EntityTransaction geradorTransacao() {
//		// TODO Auto-generated method stub
//		return null;
//	}


	@Override
	public Optional<Categoria> buscarPorId(Long id) throws RegistroNaoExisteException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


	@Override
	public Iterable<Categoria> listar() throws RegistroNaoExisteException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Page<Categoria> listarPagina(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Categoria criar(Categoria entidade) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Categoria atualizar(Categoria entidade) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void excluirPorId(Long id) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void excluirTodos() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Categoria antesDeSalvar(Categoria entidade) throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void depoisDeSalvar(Categoria entidade) throws NegocioException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Categoria antesDeAlterar(Categoria entidade) throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void depoisDeAlterar(Categoria entidade) throws NegocioException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void antesDeExcluir(Long id) throws NegocioException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<CategoriaDTO> listarDto() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CategoriaDTO EntidadeToDTO(Categoria entidade) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Categoria DTOtoEntidade(CategoriaDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}			
}
