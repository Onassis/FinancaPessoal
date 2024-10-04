
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
	 	})