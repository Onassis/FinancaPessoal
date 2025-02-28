package br.com.fenix.fi.favorecido;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fenix.abstrato.servico.IServico;
import br.com.fenix.abstrato.servico.IServicoDTO;
import br.com.fenix.abstrato.servico.ServicoAbstrato;
import br.com.fenix.abstrato.servico.ServicoAbstratoDTO;
import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.dominio.dto.Option;
import br.com.fenix.dominio.enumerado.OperacaoDB;
import jakarta.persistence.EntityTransaction;

@Service
public class FavorecidoServico  extends ServicoAbstratoDTO<FavorecidoRepositorio,Favorecido,FavorecidoDTO, Long> implements IServicoDTO<Favorecido,FavorecidoDTO,Long> {


	@Autowired
	public FavorecidoServico(FavorecidoRepositorio repositorio) {
		super(repositorio);
		
	}

	public List<Option>  listaDeFavorecido() {
		   List<Option> options = repositorio.findAll()
		   		.stream()    
				.map(favorecido  -> new Option(favorecido.getId(), favorecido.getNome()))
	            .collect(Collectors.toList());
			return options;
			
	}	

	@Override
	public void handleException(OperacaoDB op, Exception e) throws Exception {
	
		  if ( e instanceof 	DataIntegrityViolationException) { 
			  throw new NegocioException("Favorecido possui lançamento e não pode ser excluida");
		  }		  
	
 	  throw e ;	
	}
//	@Override
//	public Favorecido antesDeSalvar(Favorecido entidade) throws NegocioException {
//		// TODO Auto-generated method stub
//		return entidade;
//	}
//	@Override
//	public void depoisDeSalvar(Favorecido entidade) throws NegocioException {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public Favorecido antesDeAlterar(Favorecido entidade) throws NegocioException {
//		// TODO Auto-generated method stub
//		return entidade;
//	}
//	@Override
//	public void depoisDeAlterar(Favorecido entidade) throws NegocioException {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void antesDeExcluir(Long id) throws NegocioException {
//		// TODO Auto-generated method stub
//		
//	}
	@Override
	public EntityTransaction geradorTransacao() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional<Favorecido> buscarPorId(Long id) throws RegistroNaoExisteException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	@Override
	public Iterable<Favorecido> listar() throws RegistroNaoExisteException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Page<Favorecido> listarPagina(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Favorecido criar(Favorecido entidade) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Favorecido atualizar(Favorecido entidade) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void excluirPorId(Long id) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void excluirTodos() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Favorecido antesDeSalvar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void depoisDeSalvar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Favorecido antesDeAlterar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void depoisDeAlterar(Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void antesDeExcluir(Long id) throws NegocioException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<FavorecidoDTO> listarDto() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public FavorecidoDTO EntidadeToDTO(Favorecido entidade) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Favorecido DTOtoEntidade(FavorecidoDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Favorecido DTOtoEntidade(FavorecidoDTO dto, Favorecido entidade) throws NegocioException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public FavorecidoDTO buscaDTOPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Favorecido criar(FavorecidoDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}