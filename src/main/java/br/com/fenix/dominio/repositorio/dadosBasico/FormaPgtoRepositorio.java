package br.com.fenix.dominio.repositorio.dadosBasico;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.FormaPgto;
import br.com.fenix.fi.categoria.Categoria;

@Repository
public interface FormaPgtoRepositorio extends GenericRepository<FormaPgto> {

	
	  List<FormaPgto> findByOrderByNomeAsc();
}
