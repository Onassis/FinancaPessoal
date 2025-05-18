package br.com.fenix.fi.lancamento.operacao;

import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.lancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TR")
public class Transferencia extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Transferencia() {
		super(TipoOperacao.TR);
	}
	
	public Transferencia(LancamentoDTO dto) {
		super(dto);
		this.tipoOperacao = TipoOperacao.TR;
		this.nroInicialPrestacao  = 1 ; 
		this.nroPrestacao = 1;
	}
	@Override
	public void addDatalheLancamento(DetalheLancamento detalheLac) {
		detalheLac.setLancamento(this); 
		detalheLancamento.add(detalheLac);  		
	}
}
