package br.com.fenix.fi.emprestimo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.fi.detalheLancamento.DetalheLancamento;
import br.com.fenix.fi.detalheLancamento.DetalheLancamentoRepositorio;
import br.com.fenix.fi.lancamento.Lancamento;
import br.com.fenix.fi.lancamento.LancamentoDTO;
import br.com.fenix.fi.lancamento.LancamentoRepositorio;
import br.com.fenix.fi.saldo.SaldoContaRepositorio;
import br.com.fenix.fi.saldo.SaldoServico;
import br.com.fenix.fi.upload.LancAux;
import jakarta.persistence.EntityManagerFactory;

@Service
public class EmprestimoServico  extends ServicoAbstratoDTO<Emprestimo,LancamentoDTO,UUID> implements IServicoDTO<Emprestimo,LancamentoDTO,UUID> { 



	@Autowired
	EmprestimoRepositorio emprestimoRP;
	@Autowired
	DetalheLancamentoRepositorio DtlancamentoRP;
//	@Autowired
//	SaldoContaRepositorio saldoRP;
	@Autowired
	SaldoServico saldoSC;
	@Autowired
	EmprestimoConverter converter;

//	@Autowired
//	private ModelMapper modelMapper;


	public EmprestimoServico() {
		super();
	}

	@Override
	public EmprestimoRepositorio getRp() {
		// TODO Auto-generated method stub
		return emprestimoRP;
	}
	@Override
	public EmprestimoConverter getConverter() {

		return converter; 

	}
	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public Emprestimo antesDeSalvar  (Emprestimo entidade) throws NegocioException { 
		if (entidade.isTransferencia()) { 
			DetalheLancamento detalhe = entidade.getDetalheLancamento().get(0);
			if (detalhe.getContaLancamento().equals(detalhe.getContaTransferencia()))  {
				 throw new NegocioException("Transferência não pode ser para a mesma conta");
			}
		}
		return entidade;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void depoisDeSalvar(Emprestimo entidade) throws NegocioException {
		for (DetalheLancamento detalhe : entidade.getDetalheLancamento()) { 
			saldoSC.atualizaSaldo( detalhe.getContaLancamento(), detalhe.getDataVenc(),detalhe.getValor()) ;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluirPorId(UUID id)throws Exception {
		try {
			antesDeExcluir(id);

			DetalheLancamento detLanc = DtlancamentoRP.findById(id).orElseThrow(); 
			UUID idLanc = detLanc.getLancamento().getId(); 
//			if ( detLanc.getLancamento().tipoOperacao == TipoOperacao.CP) {
//				DtlancamentoRP.deleteById(id);				
//			}
//			else {
//				DtlancamentoRP.deleteById(id);								
//				getRp().deleteById(idLanc);
//			}

		} catch (Exception e) {
			handleException(OperacaoDB.DEL,e);
		}
	}
}