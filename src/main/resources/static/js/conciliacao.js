$(document).ready(function() {

    // Função para verificar o estado do checkbox e mostrar/esconder o campo de data
    function toggleDataPgto() {
        // Verifica se o checkbox 'conciliado' está marcado    
    	if ($('#input_conciliado').is(':checked')) {		
	        $('#input_datapgto').prop("readonly", false) // Mostra com animação	        
			$('#input_datapgto').attr('required', true);
			$('#input_valorpgto').prop("readonly", false) // Mostra com animação	        
			$('#input_valorpgto').attr('required', true);
    	} else {
        	$('#input_datapgto').prop("readonly", true); // Esconde com animação        	
			$('#input_datapgto').attr('required', false);
			$('#input_valorpgto').prop("readonly", true) // Mostra com animação	        
			$('#input_valorpgto').attr('required', false);

    	}
    }

    // 1. Executa a função assim que a página carrega para definir o estado inicial correto
    toggleDataPgto();

    // 2. Adiciona um "ouvinte" para o evento 'change' no checkbox
    //    Isso vai disparar a função toda vez que o checkbox for marcado ou desmarcado
    $('#input_conciliado').on('change', function() {
		toggleDataPgto();
		if ($(this).is(':checked')) {

            // Atualiza o valor do campo de data
            var data = $('#input_datavenc').val();
            var total = $('#input_total').val();

            $('#input_datapgto').val(data);
            $('#input_valorpgto').val(total);
        }
        else {
            $('#input_datapgto').val('');
            $('#input_valorpgto').val(0);			
		};   
 
    });

});