package br.com.fenix.fi.modeloCategoria;


import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.dominio.DadosModelo.ModeloCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.fi.categoria.Categoria;

@Repository
public interface ModeloCategoriaRepositorio extends GenericRepository<ModeloCategoria> {

	

}
