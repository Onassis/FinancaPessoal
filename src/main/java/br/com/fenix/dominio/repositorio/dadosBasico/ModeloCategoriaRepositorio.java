package br.com.fenix.dominio.repositorio.dadosBasico;


import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;

@Repository
public interface ModeloCategoriaRepositorio extends GenericRepository<ModeloCategoria> {

	

}
