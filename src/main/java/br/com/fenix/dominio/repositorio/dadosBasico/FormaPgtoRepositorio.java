package br.com.fenix.dominio.repositorio.dadosBasico;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.FormaPgto;

@Repository
public interface FormaPgtoRepositorio extends GenericRepository<FormaPgto> {

	
	  List<FormaPgto> findByOrderByNomeAsc();
}
