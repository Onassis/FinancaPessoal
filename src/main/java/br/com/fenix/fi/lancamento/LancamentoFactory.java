package br.com.fenix.fi.lancamento;

import java.util.Objects;

import br.com.fenix.dominio.enumerado.TipoOperacao;
import br.com.fenix.fi.lancamento.operacao.CartaoCredito;
import br.com.fenix.fi.lancamento.operacao.CompraParcelada;
import br.com.fenix.fi.lancamento.operacao.Credito;
import br.com.fenix.fi.lancamento.operacao.Debito;
import br.com.fenix.fi.lancamento.operacao.Transferencia;
import br.com.fenix.fi.upload.LancAux;

/**
 * Factory para criar instâncias de Lancamento com base no TipoOperacao.
 * A versão refatorada utiliza um enum, garantindo type-safety e robustez.
 */
public class LancamentoFactory {

    /**
     * Cria e retorna uma instância de uma subclasse de Lancamento.
     * A classe concreta é escolhida com base no enum 'tipoOperacao' do objeto LancAux.
     *
     * @param lancAux O objeto de dados auxiliares contendo as informações para a criação do lançamento.
     * @return Uma instância de Lancamento (Credito, Debito, etc.).
     * @throws IllegalArgumentException se o lancAux ou o tipoOperacao forem nulos.
     * @throws UnsupportedOperationException se o tipo de operação for válido mas ainda não suportado pela factory.
     */
    public static Lancamento criar(LancAux lancAux) {
        Objects.requireNonNull(lancAux, "O objeto LancAux não pode ser nulo.");
        TipoOperacao tipo = lancAux.getTipoOperacao();
        Objects.requireNonNull(tipo, "O campo 'tipoOperacao' em LancAux não pode ser nulo.");

        switch (tipo) {
            case CP:
                return new CompraParcelada(lancAux);
            case CD:
                return new CartaoCredito(lancAux);
            case CR:
                return new Credito(lancAux);                
            case DB:
                return new Debito(lancAux);
            case TR:
                return new Transferencia(lancAux);
            
            // Caso para tipos de operação que existem no enum mas não têm uma classe de Lançamento correspondente ainda.
            case EP:
            case ES:
            case PG:
            default:
                throw new UnsupportedOperationException("A criação de lançamentos do tipo '" + tipo.getDescricao() + "' não é suportada.");
        }
    }
}