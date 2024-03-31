package br.com.fenix.dominio.servico;

import java.io.DataInput;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.dominio.dto.CategoriaDTO;
import br.com.fenix.dominio.dto.LancamentoDTO;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.dominio.modelo.DetalheLancamento;
import br.com.fenix.dominio.modelo.Lancamento;
import br.com.fenix.dominio.modelo.LancAux;
import br.com.fenix.dominio.modelo.DadoBasico.Automacao;
import br.com.fenix.dominio.modelo.DadoBasico.Categoria;
import br.com.fenix.dominio.modelo.DadoBasico.Conta;
import br.com.fenix.dominio.repositorio.DetalheLancamentoRepositorio;
import br.com.fenix.dominio.repositorio.LancAuxRepositorio;
import br.com.fenix.dominio.repositorio.LancamentoRepositorio;
import br.com.fenix.dominio.repositorio.dadosBasico.AutomacaoRepositorio;
import br.com.fenix.util.Tag;

@Service
public class AutomacaoServico {

	@Autowired
	AutomacaoRepositorio autoRP;
	
	Iterable <Automacao> automacoes; 
	


	public void automatizar ( LancAux lancDTO) {  
		automacoes = autoRP.findAll();
		
		for (Automacao auto  : automacoes) {
			
			if (lancDTO.getLancamentoInformacao().contains(auto.getCriterio().toUpperCase())) {	
				System.out.println(auto);
				lancDTO.setLancamentoFavorecido(  auto.getFavorecido());
				lancDTO.setLancamentoSubCategoria(auto.getSubCategoria());
				lancDTO.setContaDestino(auto.getContaTransferencia());
				lancDTO.setLancamentoTipoOperacao( auto.getTipoOperacao());
				break;
			}
		}
	
	}

	}
