package br.com.fenix.fi.lancamento.operacao;

import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;
import br.com.fenix.fi.upload.LancAux;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CP")
public class CompraParcelada extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CompraParcelada() {
		super(TipoOperacao.CP);
	}
	
	public CompraParcelada(LancamentoDTO dto  ) {
		super(dto);
		this.tipoOperacao = TipoOperacao.CP;
	}
	public CompraParcelada(LancAux lancAux) {
		super(lancAux);
		this.tipoOperacao = TipoOperacao.CP;		
	}

	@Override
	public void addDatalheLancamento(DetalheLancamento detalheLac) {
		detalheLac.setTipoLancamento(TipoLancamento.D);
	
		detalheLac.setLancamento(this); 
		detalheLancamento.add(detalheLac);  		
	}
}
