package br.com.fenix.dominio.repositorio.dadosBasico;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoConta;
import br.com.fenix.dominio.modelo.DadoBasico.SaldoConta;
import br.com.fenix.fi.conta.Conta;
import br.com.fenix.seguranca.usuario.Usuario;

@Repository
public interface SaldoContaRepositorio extends GenericRepository<SaldoConta> {
	
	@Override
	@Query("from SaldoConta s where s.criadoPor.id = ?#{ principal.id}")
	public List<SaldoConta> findAll(); 
	
	@Query("from SaldoConta s where s.conta = ?1  and s.data = ?2 and  s.criadoPor.id = ?#{ principal.id} ")
	Optional<SaldoConta> findByContaAndData(Conta conta,LocalDate data);

	@Query("from SaldoConta s where s.conta = ?1  and s.data > ?2 and s.criadoPor.id = ?#{ principal.id} ")
	List<SaldoConta> findByContaAndGtData(Conta conta,LocalDate data);

	@Query(value= "SELECT  max(data) FROM saldo_conta where conta_id = :conta  and data < :data and criado_por_id = :usuario ", nativeQuery = true)   
	Date ultimoDataSaldo(Long usuario, Long conta,LocalDate data);
	
	@Query(value= " select * FROM saldo_conta as a where a.criado_por_id = :usuario and  a.conta_id = :conta and a.data in \\"
		  + " ( SELECT  max(data) FROM saldo_conta as b \\"
		  + "where b.criado_por_id = a.criado_por_id and  b.conta_id = a.conta_id and data < :data  )" , nativeQuery = true)
	Optional<SaldoConta> findByContaDataSaldoAnterior( Long usuario,Long conta,LocalDate data);
	
	

	@Query(value = "select * from f_atualiza_saldo(:usuario,:conta,:datasaldo,:saldoinicial)", nativeQuery = true)
	boolean f_atualiza_saldo(
			@Param("usuario") Long usuario,
			@Param("conta") Long conta,
			@Param("datasaldo") LocalDate datasaldo,
			@Param("saldoinicial") BigDecimal saldoinicial); 
			

//	Optional<SaldoConta> findByCriadoPorAndContaAndDataLessThanOrderByDateDesc(Usuario usuario,Conta conta, LocalDate data) ; 

	
//	@Query("from SaldoConta s where s.conta = ?1  and s.data >= ?2 and s.criadoPor.id = ?#{ principal.id} ")
//	public List<SaldoConta> findByContaGeData(); 
	
//	/@Modifying
//	@Query("update SaldoConta set saldo = saldo + ?1 where conta_id = ?2  and data > ?3 and criado_por_id = ?#{ principal.id} ")
//	void atualizaContaGeDataSaldo(BigDecimal vlr_lancamento,Conta conta,LocalDate data);
			
}
 