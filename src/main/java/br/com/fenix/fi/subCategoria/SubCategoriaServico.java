package br.com.fenix.fi.subCategoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.fi.categoria.CategoriaDTO;
import jakarta.persistence.EntityManagerFactory;
@Service
public class SubCategoriaServico extends ServicoAbstratoDTO<SubCategoria,SubCategoriaDTO,Long> implements IServicoDTO<SubCategoria,SubCategoriaDTO,Long> {

	@Autowired
	SubCategoriaRepositorio subCategoriaRP;

	@Autowired
	private SubCategoriaMapper converterSub;

	
	public SubCategoriaServico() {
		super();
	}


	@Override
	public JpaRepository<SubCategoria, Long> getRp() {		
		return subCategoriaRP;
	}


	@Override
	public SubCategoriaMapper getConverter() {
		// TODO Auto-generated method stub
		return converterSub;
	}

	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
		  if ( e.getMessage().contains("subcategoriadescricao")) {
			  throw new NegocioException("Já existe um subCategoria com essa descrição");				  			  
		  }
		  if (op == OperacaoDB.DEL) { 
			  throw new NegocioException("SubCategoria possui lançamento e não pode ser excluida");
		  }		  	
 	  throw e ;	
	}

}
