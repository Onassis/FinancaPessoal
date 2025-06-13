package br.com.fenix.fi.saldo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

// @Component
@RequiredArgsConstructor
public class SaldoEventListener {

    private final SaldoServico saldoService;

    // Ouve o evento APÓS a transação do lançamento ser comitada com sucesso.
    @TransactionalEventListener
    public void handleLancamentoAlteradoEvent(LancamentoAlteradoEvent event) {
        // A lógica principal está no SaldoService para manter a coesão.
        // A atualização é propagada para os dias seguintes.
 //       saldoService.recalcularSaldosAPartirDe(event.getContaId(), event.getData());
    }
}