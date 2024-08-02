package br.com.fenix.dominio.servico;

import java.time.LocalDate;

import br.com.fenix.fi.conta.Conta;

public interface IServicoContaCorrente {
	void depositar(Conta conta, LocalDate data, Double amount);
    void sacar(Conta conta,  LocalDate data,Double amount);
    void transferir(Conta contaOrigem, Conta contaDestino,  LocalDate data,Double amout);
    
}
