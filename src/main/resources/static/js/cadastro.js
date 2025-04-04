 function ShowAlert(msg_title, msg_body, msg_type) {
    var AlertMsg = $('div[role="alert"]');
    $(AlertMsg).find('strong').html(msg_title);
    $(AlertMsg).find('p').html(msg_body);
    $(AlertMsg).removeAttr('class');
    $(AlertMsg).addClass('alert alert-' + msg_type);
    $(AlertMsg).show();
  }

  $(document).ready(function() 
	{
		 jQuery(function() {
			    	jQuery('form').bind('submit', function() {
		        	jQuery(this).find(':disabled').removeAttr('disabled');
    				});
    		});
		 
		 	 $(document).delegate('#btn_deletar','click',function(event) 
	 	 		{
	 				event.preventDefault();
//	 				$("#deleteModal .modal-header .modal-title").text("Excluir Favorecido");
	 				$("#deleteModal #btnExcluirModal").attr('href', $(this).attr("href"));
	 				$("#deleteModal").modal("show");
	 			});
			 $(document).delegate('#btnExcluirModal','click',function(event)
			 { 
	 				event.preventDefault();
//	 				$("#deleteModal .modal-header .modal-title").text("Excluir Favorecido");	 				
	 				$("#deleteModal").modal("hide");
	 				url = $("#btnExcluirModal").attr('href');
	 				
	 				console.log (url); 
	 				
	 				document.location.href = $("#btnExcluirModal").attr('href');
	 			});
	 	$(".alert").delay(5000).slideUp(200, function () {
        	$(this).alert('close');
    	});		
	 	})