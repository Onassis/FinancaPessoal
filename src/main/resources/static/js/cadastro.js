function ShowAlert(msg_title, msg_body, msg_type) {
	var AlertMsg = $('div[role="alert"]');
	$(AlertMsg).find('strong').html(msg_title);
	$(AlertMsg).find('p').html(msg_body);
	$(AlertMsg).removeAttr('class');
	$(AlertMsg).addClass('alert alert-' + msg_type);
	$(AlertMsg).show();
}

$(document).ready(function() {
	// 1. Executa a função assim que a página carrega para definir o estado inicial correto
	jQuery(function() {
		jQuery('form').bind('submit', function() {
			jQuery(this).find(':disabled').removeAttr('disabled');
		});
	});

	$(document).delegate('#btn_deletar', 'click', function(event) {
		event.preventDefault();
		//	 				$("#deleteModal .modal-header .modal-title").text("Excluir Favorecido");
		$("#deleteModal #btnExcluirModal").attr('href', $(this).attr("href"));
		$("#deleteModal").modal("show");
	});
	$(document).delegate('#btnExcluirModal', 'click', function(event) {
		event.preventDefault();
		//	 				$("#deleteModal .modal-header .modal-title").text("Excluir Favorecido");	 				
		$("#deleteModal").modal("hide");
		url = $("#btnExcluirModal").attr('href');

		console.log(url);

		document.location.href = $("#btnExcluirModal").attr('href');
	});

	$(".mySelect").on("keyup", function() {
		var filterText = $(this).val().toLowerCase();
		console.log(filterText);

		$("#filterInput").val = $("#filterInput").val + filterText;
		filterText = $("#filterInput").val;
		$("#mySelect option").each(function() {
			var optionText = $(this).text().toLowerCase();
			if (optionText.indexOf(filterText) > -1) {
				$(this).show();
			} else {
				$(this).hide();
			}
		});
	});
	$(".alert").delay(5000).slideUp(200, function() {
		$(this).alert('close');
	});
	$('.moeda').mask('000000000000,00', {
		reverse: true,
		translation: {
			'0': { pattern: /-|\d/, recursive: true }
		},
		onChange: function(value, e) {
			e.target.value = value.replace(/^-\./, '-').replace(/^-,/, '-').replace(/(?!^)-/g, '');
		}
	});
	$('.cep').mask('00000-000');

});