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
@DiscriminatorValue("CR")
public class Credito extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Credito() {
		super(TipoOperacao.CR);
	}
	
	public Credito(LancamentoDTO dto) {
		super(dto);
		this.tipoOperacao = TipoOperacao.CP;
		this.nroInicialPrestacao  = 1 ; 
		this.nroPrestacao = 1;
	}
	public Credito(LancAux lancAux) {
		super(lancAux);
		this.tipoOperacao = TipoOperacao.CP;
		this.nroInicialPrestacao  = 1 ; 
		this.nroPrestacao = 1;
	}

	@Override
	public void addDatalheLancamento(DetalheLancamento detalheLac) {
		detalheLac.setTipoLancamento(TipoLancamento.C);	
		detalheLac.setLancamento(this); 
		detalheLancamento.add(detalheLac);  		
	}
}
