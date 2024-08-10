/**
 * Modal controle para formulario de cadastro
 */
 function ShowAlert(msg_title, msg_body, msg_type) {
    var AlertMsg = $('div[role="alert"]');
    $(AlertMsg).find('strong').html(msg_title);
    $(AlertMsg).find('p').html(msg_body);
    $(AlertMsg).removeAttr('class');
    $(AlertMsg).addClass('alert alert-' + msg_type);
    $(AlertMsg).show();
  }


ajaxPost = function (tipo, recurso, dados, id) {
	console.log("ajaxPost");
	console.log(recurso);
	$.ajax(
		{
			type : tipo,			
			contentType : "application/json; charset=utf-8",
			url : recurso,
			data : dados,
			cache : false,
		    success: function(data, textStatus, xhr) 
		    {   		    	
				$("#myModal").modal("hide"); 
				console.log(xhr.status);
		    	if ( xhr.status == 201 ) 
		    	{		 
		    		    $("#myModal").modal("hide");
	            		 ShowAlert("Inclusão", 'Registro incluido com sucesso', "success");
/*	            		 setTimeout( function () { 
										$(".alert").fadeOut("slow");
										$(".alert").alert("close");									
									}, 2000);
*/									
						 window.setTimeout(function() {	location.reload()},	1000);
			             console.log(xhr.status);
			    } else if ( xhr.status == 200 )  {
					$("#myModal").modal("hide");
					 ShowAlert("Alteração", 'Resgistro alterado com sucesso', "success");
					 window.setTimeout(function() {	location.reload()},	1000);					 
				} else if ( xhr.status == 204 )  { 
						$("#deleteModal").modal("hide");						
						ShowAlert("Exclusão", 'Resgistro excluído com sucesso', "info");
						window.setTimeout(function() {	location.reload()},	1000);	
					}           						 
				},	            	
			error : function(err)  {
				// Status 401: não autorizado, 403 - proibido, 404 - não encontrado
				 if (tipo == 'DELETE' ) {
				 	$("#deleteModal").modal("hide");
				 } else {
					 $("#myModal").modal("hide");
				 }
				
				 ShowAlert("Erro", 'Atenção: Houve um erro -  ' + err.status , "danger");	
				 window.setTimeout(function() {	location.reload()},	1000);
			}
		});
}
	
$(document).ready(function() 
	{
		 
	 	 $(document).delegate('#btnEditarModal','click',function(event) 
	 		{
				console.log($(this).attr("href"));
				event.preventDefault();
				$("#myModal").modal("show").find(".modal-content").load( $(this).attr("href"));
			});
	 	 $(document).delegate('#btn_deletar','click',function(event) 
	 	 		{
	 				event.preventDefault();
//	 				$("#deleteModal .modal-header .modal-title").text("Excluir Favorecido");
	 				$("#deleteModal #btnExcluirModal").attr('href', $(this).attr("href"));
	 				$("#deleteModal").modal("show");
	 			});
	 	 
	 	 $(document).delegate('#btnExcluirModal','click',	function(event) 
	 		{
	 			event.preventDefault();
	 			ajaxPost('DELETE', $(this).attr("href"),'',0);
	 	 	});
		 
		 $(document).delegate('#btnAddModal','click',function(event) 
					{
						event.preventDefault();
						console.log("click add");
						console.log($(this).attr("href"));
						$("#myModal").modal("show").find(".modal-content").load($(this).attr("href"));
					});

		 $(document).delegate('#btnSalvarModal','click',
				 function(event) {
					event.preventDefault();
					const myForm = document.getElementById('editForm');
					var myData = new FormData(myForm);
					const formJSON = Object.fromEntries(myData.entries());
					var myJon = JSON.stringify(	formJSON, null, 2);
					if (document.getElementById('id').value == "") 
						ajaxPost('POST', $(this).attr("href"),myJon,0);
					else
						ajaxPost('PUT', $(this).attr("href"),myJon,0);
		 		}		
		 );
		 
	});	 
