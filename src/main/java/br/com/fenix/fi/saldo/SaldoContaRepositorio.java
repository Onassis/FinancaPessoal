package br.com.fenix.fi.saldo;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.dominio.enumerado.TipoConta;
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

	
	@Query(value= " select * FROM saldo_conta as a where a.criado_por_id = :usuario and  a.conta_id = :conta and a.data in \\"
		  + " ( SELECT  max(data) FROM saldo_conta as b \\"
		  + "where b.criado_por_id = a.criado_por_id and  b.conta_id = a.conta_id and data < :data  )" , nativeQuery = true)
	Optional<SaldoConta> findByContaDataSaldoAnterior( Long usuario,Long conta,LocalDate data);
	
	Optional<SaldoConta> findTopByContaAndDataLessThanOrderByDataDesc(Conta conta, LocalDate data);
	
/*
 *  Consultas para calcular o saldo anterior a uma data específica. 
 */
	
	  /**
     * Encontra a maior data (a mais recente) de um SaldoConta para uma determinada conta,
     * que seja menor ou igual à data de referência fornecida.
     *
     * @param conta A conta pela qual filtrar.
     * @param data A data de referência. O método buscará a maior data existente até este dia (inclusive).
     * @return um Optional contendo a maior data encontrada, ou Optional.empty() se não houver saldos para a conta até a data especificada.
     */
    @Query("SELECT MAX(s.data) FROM SaldoConta s WHERE s.conta = :conta AND s.data < :data")
    Optional<LocalDate> findUltimaDataSaldo(@Param("conta") Conta conta, @Param("data") LocalDate data);
    
    
    @Query(value = "SELECT " +
            "    a.id, " +
            "    a.conta_id AS contaId, " +
            "    a.ano, " +
            "    a.mes, " +
            "    a.data, " +
            "    a.flag_compensacao AS flagCompensacao, " +
            "    a.saldo_inicial AS saldoInicial, " +
            "    b.total, " +
            "    a.saldo_inicial AS saldoInicial " +
            "   FROM view_saldomes a " + 
            "   where a.conta_id = :conta and data = :data " ,            
            nativeQuery = true)
	Optional<ISaldoMes> findSaldosByContaByData(Conta conta, LocalDate data);
    
    
//	@Query(value= "SELECT  max(data) as data FROM saldo_conta where criado_por_id = :usuario and conta_id = :conta  and data < :data")   
//	LocalDate findMaxData(Long usuario, Long conta,LocalDate data);
    
	//	@Query(value= "SELECT * FROM saldo_conta where criado_por_id = :usuario and conta_id = :conta and data = :data")
	//	public List<SaldoContaRecord> findbyContaGtData(Long usuario,Long conta,LocalDate data);
	
/******************************************************************************************************
	@Query(value = "select * from f_atualiza_saldo(:usuario,:conta,:datasaldo,:saldoinicial)", nativeQuery = true)
	boolean f_atualiza_saldo(
			@Param("usuario") Long usuario,
			@Param("conta") Long conta,
			@Param("datasaldo") LocalDate datasaldo,
			@Param("saldoinicial") BigDecimal saldoinicial); 
			

    
    /**
     * Solução com Query Nativa e Record
     * Para queries nativas, o Spring Data JPA pode mapear colunas para o construtor do record
     * se os aliases das colunas corresponderem aos nomes dos campos do record.
     */


 
//	Optional<SaldoConta> findByCriadoPorAndContaAndDataLessThanOrderByDateDesc(Usuario usuario,Conta conta, LocalDate data) ; 

	
//	@Query("from SaldoConta s where s.conta = ?1  and s.data >= ?2 and s.criadoPor.id = ?#{ principal.id} ")
//	public List<SaldoConta> findByContaGeData(); 
    
    @Transactional(propagation = Propagation.MANDATORY) // Operações de modificação devem ser transacionais.
    @Modifying // Essencial para indicar que esta é uma query de UPDATE, DELETE ou INSERT.
    @Query("UPDATE SaldoConta sc " +
           "SET sc.saldoInicial = sc.saldoInicial + :valorAdicional " +
           "WHERE sc.conta.id = :contaId AND sc.data > :dataReferencia and flag_compensacao = false" )
    int atualizaContaGeDataSaldo(
            @Param("contaId") Long contaId,
            @Param("dataReferencia") LocalDate dataReferencia,
            @Param("valorAdicional") BigDecimal valorAdicional
            );
	    
//    @Modifying
//    @Transactional(propagation = Propagation.MANDATORY)
//	@Query("update SaldoConta set saldoInicial = saldoInicial + :valor, versao = versao + 1 "  
////			" and data > :data "
//			 ,nativeQuery = true)	
//	public void atualizaContaGeDataSaldo(Long conta, LocalDate data, BigDecimal valor);
////@Param("conta") Long conta,@Param("data") LocalDate data,			
}
 