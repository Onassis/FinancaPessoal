package br.com.fenix.fi.emprestimo;

import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;
import br.com.fenix.fi.upload.LancAux;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EP")
public class Emprestimo extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Emprestimo() {
		super(TipoOperacao.EP);
	}
	
	public Emprestimo(LancamentoDTO dto  ) {
		super(dto);
		this.tipoOperacao = TipoOperacao.EP;
		this.total = dto.getTotal();
	}
	public Emprestimo(LancAux lancAux) {
		super(lancAux);
		this.tipoOperacao = TipoOperacao.EP;		
	}

	@Override
	public void addDatalheLancamento(DetalheLancamento detalheLac) {
		detalheLac.setLancamento(this); 
		detalheLancamento.add(detalheLac);  		
	}
}
