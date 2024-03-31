	package br.com.fenix.dominio.servico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.DadosModelo.ModeloSubCategoria;
import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.Node;
import br.com.fenix.dominio.dto.OrcamentoDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.Orcamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;
import br.com.fenix.dominio.repositorio.OrcamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.CategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.ModeloCategoriaRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.SubCategoriaRepositorio;

@Service
public class OrcamentoServico {


    @Autowired
    private  CategoriaRepositorio categoriaRP;
    
    @Autowired	
	private  OrcamentoRepositorio orcamentoRP;

    ArrayList<OrcamentoDTO> orcamentoDTO = new ArrayList<OrcamentoDTO>();


	public ArrayList<OrcamentoDTO> listaOrcamentoPorAno(int ano) {
		 List<Orcamento> orcamentos = orcamentoRP.findAllbyAno(ano);
		 
	 	 if (orcamentos.isEmpty()) 
	 		 return criarOrcamentoPorCategorias(ano); 
	 	 else 
	 		 return orcamentoToDTO(orcamentos); 	 
	}

	public  ArrayList<OrcamentoDTO> orcamentoToDTO(List<Orcamento> orcamentos ) {
		 for(Orcamento orcamento : orcamentos) {  
			 orcamentoDTO.add(new OrcamentoDTO(orcamento));
		 }
		return orcamentoDTO;
	}

	public  ArrayList<OrcamentoDTO> criarOrcamentoPorCategorias(int ano){  
	
		 Iterable<Categoria> categorias ;
		 orcamentoDTO.clear();
		 
		 categorias = categoriaRP.findByAllOrderByDescricaoAsc(); 
	
		 for(Categoria categoria : categorias) {      
		 
			 if (categoria.getSubCategoria().isEmpty()) 
				 orcamentoDTO.add(new OrcamentoDTO(ano, categoria));
			 else 
				 for(SubCategoria subCategoria : categoria.getSubCategoria())  
 	    	 		orcamentoDTO.add(new OrcamentoDTO(ano, subCategoria));
 	    }

		 return orcamentoDTO;
	}

	public List<Node> getSampleNodeList(int ano, TipoLancamento tipoLancamento) {
	    	
	        List<Node> nodes = new ArrayList<>();
	        Iterable<Categoria> categorias ; 
	        categorias = categoriaRP.findByTipoLancamentoOrderByDescricaoAsc(tipoLancamento);
	        for(Categoria categoria : categorias) {
	        	nodes.add(new Node(categoria.getDescricao(), "0"  ,categoria.getDescricao(),  "Financeiro/orcamento/" + ano + "/" + categoria.getId() )); 
	       	 	
	       	 	for(SubCategoria subCategoria : categoria.getSubCategoria()) { 
	       	 		nodes.add(new Node(subCategoria.getDescricao(), categoria.getDescricao(),subCategoria.getDescricao(),  "Financeiro/orcamento/" + ano + "/" + subCategoria.getId() )); 
	       	 		 
	       	 	}
	        }	
	        return nodes;
	    }	
}
		 
