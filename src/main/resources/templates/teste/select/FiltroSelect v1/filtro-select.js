$(document).ready(function() {

    // Transforma qualquer select com o ID 'estados-brasil' em um select filtrável
    $('#estados-brasil').each(function() {
        const $selectOriginal = $(this);
        $selectOriginal.hide(); // Esconde o select original

        // 1. CRIA OS NOVOS ELEMENTOS
        const $container = $('<div class="filtro-select-container"></div>');
        const $inputFiltro = $('<input type="text" class="filtro-select-input" placeholder="Digite para filtrar...">');
        const $listaOpcoes = $('<ul class="filtro-select-opcoes"></ul>');

        // Posiciona os novos elementos depois do select original
        $selectOriginal.after($container);
        $container.append($inputFiltro).append($listaOpcoes);

        // 2. POPULA A NOVA LISTA COM AS OPÇÕES DO SELECT ORIGINAL
        $selectOriginal.find('option').each(function() {
            const $opcaoOriginal = $(this);
            // Ignora a primeira opção se ela for um placeholder vazio
            if ($opcaoOriginal.val() !== "") {
                const $novaOpcao = $('<li></li>');
                $novaOpcao.text($opcaoOriginal.text());
                $novaOpcao.attr('data-value', $opcaoOriginal.val());
                $listaOpcoes.append($novaOpcao);
            }
        });
        
        // Seta o valor inicial do input com o texto da opção selecionada (se houver)
        const textoInicial = $selectOriginal.find('option:selected').text();
        if ($selectOriginal.val() !== "") {
             $inputFiltro.val(textoInicial);
        }

        // 3. ADICIONA OS EVENTOS
        
        // Ao clicar no input, mostra a lista de opções
        $inputFiltro.on('click', function(e) {
            e.stopPropagation();
            $listaOpcoes.show();
        });

        // Ao digitar no input, filtra a lista
        $inputFiltro.on('keyup', function() {
            const filtro = $(this).val().toLowerCase();
            $listaOpcoes.find('li').each(function() {
                const textoOpcao = $(this).text().toLowerCase();
                if (textoOpcao.includes(filtro)) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        });

        // Ao clicar em uma opção da lista
        $listaOpcoes.on('click', 'li', function() {
            const valorSelecionado = $(this).attr('data-value');
            const textoSelecionado = $(this).text();

            // Atualiza o valor do input visível
            $inputFiltro.val(textoSelecionado);
            
            // ATUALIZA O VALOR DO SELECT ORIGINAL (MUITO IMPORTANTE!)
            $selectOriginal.val(valorSelecionado).trigger('change');
            
            // Esconde a lista de opções
            $listaOpcoes.hide();
        });

        // Fecha a lista de opções se clicar fora do componente
        $(document).on('click', function() {
            $listaOpcoes.hide();
        });
    });

});