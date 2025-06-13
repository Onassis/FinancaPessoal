// ======================================================================
// FUNÇÃO AUXILIAR PARA REMOVER ACENTOS
// Esta função é a chave para a nova funcionalidade.
// ======================================================================
function removerAcentos(str) {
    if (str === null || str === undefined) {
        return "";
    }
    // O método normalize('NFD') decompõe os caracteres acentuados.
    // Ex: "São" vira "S" + "a" + "̃" (acento til) + "o"
    // O regex /[\u0300-\u036f]/g remove todos os diacríticos (acentos) combinados.
    return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}


$(document).ready(function() {

    // Transforma qualquer select com a classe '.seletOpt' em um select filtrável
    $('.seletOpt').each(function() {
        const $selectOriginal = $(this);
        if ($selectOriginal.is(':disabled')) {
			return;
		}
        $selectOriginal.hide(); // Esconde o select original

        // 1. CRIA OS NOVOS ELEMENTOS
        const $container = $('<div class="filtro-select-container"></div>');
        const placeholder = $selectOriginal.attr('placeholder') || 'Digite para filtrar...';
        const $inputFiltro = $(`<input type="text" class="filtro-select-input" placeholder="${placeholder}">`);
        const $listaOpcoes = $('<ul class="filtro-select-opcoes"></ul>');

        $selectOriginal.after($container);
        $container.append($inputFiltro).append($listaOpcoes);

        // 2. POPULA A NOVA LISTA COM AS OPÇÕES DO SELECT ORIGINAL
        $selectOriginal.find('option').each(function() {
            const $opcaoOriginal = $(this);
            if ($opcaoOriginal.val() !== "") {
                const $novaOpcao = $('<li></li>');
                $novaOpcao.text($opcaoOriginal.text());
                $novaOpcao.attr('data-value', $opcaoOriginal.val());
                $listaOpcoes.append($novaOpcao);
            }
        });
        
        const textoInicial = $selectOriginal.find('option:selected').text();
        if ($selectOriginal.val() !== "") {
             $inputFiltro.val(textoInicial);
        }

        // 3. ADICIONA OS EVENTOS
        
        $inputFiltro.on('focus', function(e) {
            e.stopPropagation();
            $('.filtro-select-opcoes').hide();
            $listaOpcoes.show();
        });

        // ======================================================================
        // EVENTO DE FILTRO (KEYUP) - ALTERADO
        // Aqui aplicamos a função removerAcentos
        // ======================================================================
        $inputFiltro.on('keyup', function() {
            // Pega o valor digitado, converte para minúsculas e REMOVE OS ACENTOS
            const filtro = removerAcentos($(this).val().toLowerCase());

            $listaOpcoes.find('li').each(function() {
                // Pega o texto da opção, converte para minúsculas e REMOVE OS ACENTOS
                const textoOpcao = removerAcentos($(this).text().toLowerCase());

                // Compara as duas strings "limpas" (sem acentos e em minúsculas)
                if (textoOpcao.includes(filtro)) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        });

        // Ao clicar em uma opção da lista (nenhuma alteração aqui)
        $listaOpcoes.on('click', 'li', function() {
            const valorSelecionado = $(this).attr('data-value');
            const textoSelecionado = $(this).text();

            $inputFiltro.val(textoSelecionado);
            $selectOriginal.val(valorSelecionado).trigger('change');
            $listaOpcoes.hide();
        });

        // Fecha a lista de opções se clicar fora do componente
        $(document).on('click', function(e) {
            if (!$(e.target).closest('.filtro-select-container').length) {
                $listaOpcoes.hide();
            }
        });
    });

});