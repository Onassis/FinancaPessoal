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
/* This script filters the table based on the input in the search box */
 
$(document).ready(function() {
	
	
	$("#Search").on("keyup", function() {
		var value = removerAcentos($(this).val().toLowerCase());
		$("table tbody tr").filter(function() {
			$(this).toggle(removerAcentos($(this).text().toLowerCase()).indexOf(value) > -1);
		});

		// Show the 'No result' row if there are no matching rows
		var noResultRow = $("tr.warning.no-result");
		if ($("table tbody tr:visible").length === 0) {
			noResultRow.show();
		} else {
			noResultRow.hide();
		}
	});
});