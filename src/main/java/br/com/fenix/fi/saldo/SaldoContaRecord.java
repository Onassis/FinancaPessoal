package br.com.fenix.fi.saldo;

import java.math.BigDecimal;
import java.time.LocalDate;

// Um record é uma classe final, e seus campos são 'private final' por padrão.
// O construtor, getters (sem "get"), equals, hashCode e toString são gerados automaticamente.
public record SaldoContaRecord(
    Long id,
    Long contaId,
    Integer ano,
    Integer mes,
    LocalDate data,
    Boolean flagCompensacao,
    BigDecimal saldoInicial,
    BigDecimal total, // Vem da view_totalanomes
    BigDecimal saldoAtual // Agora é calculado diretamente na query
) { }
