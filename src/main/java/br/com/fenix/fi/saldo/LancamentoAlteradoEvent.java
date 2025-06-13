package br.com.fenix.fi.saldo;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class LancamentoAlteradoEvent {
    private final Long contaId;
    private final LocalDate data;

    public LancamentoAlteradoEvent(Long contaId, LocalDate data) {
        this.contaId = contaId;
        this.data = data;
    }
}
