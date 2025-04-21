package br.com.fenix.fi.subCategoria;

import br.com.fenix.dominio.enumerado.TipoCategoria;
import br.com.fenix.dominio.enumerado.TipoLancamento;

public record CategoriaDtoId(Long id, String descricao, String classe,TipoLancamento tipoLancamento, TipoCategoria tipoCategoria) {

}


