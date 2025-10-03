package br.com.fenix.fi.emprestimo;

import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;

public class EmprestimoDTO extends LancamentoDTO {

	public EmprestimoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmprestimoDTO(DetalheLancamento detLanc) {
		super(detLanc);
	}

	public EmprestimoDTO(Emprestimo lancamento) {
		super(lancamento);	
	}

}
