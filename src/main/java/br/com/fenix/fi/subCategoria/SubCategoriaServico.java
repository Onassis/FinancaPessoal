package br.com.fenix.fi.subCategoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.dto.Converter;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.fi.categoria.CategoriaDTO;
import jakarta.persistence.EntityManagerFactory;
@Service
public class SubCategoriaServico extends ServicoAbstratoDTO<SubCategoria,CategoriaDTO,Long> implements IServicoDTO<SubCategoria,CategoriaDTO,Long> {

	@Autowired
	SubCategoriaRepositorio subCategoriaRP;
	@Autowired
	private SubCategoriaMapper converterSub;

	
	public SubCategoriaServico(EntityManagerFactory emf) {
		super(emf);
	}

	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
		
	}

	@Override
	public JpaRepository<SubCategoria, Long> getRp() {		
		return subCategoriaRP;
	}

	@Override
	public Converter<SubCategoria, CategoriaDTO> getConverter() {	
		return converterSub;
	}

}
