package br.com.fenix.fi.conta;

import java.time.LocalDate;

public interface IServicoContaCorrente {
	void depositar(Conta conta, LocalDate data, Double amount);
    void sacar(Conta conta,  LocalDate data,Double amount);
    void transferir(Conta contaOrigem, Conta contaDestino,  LocalDate data,Double amout);
    
}
