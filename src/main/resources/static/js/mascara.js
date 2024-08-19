$(document).ready(function () {
   $('.moeda_input').maskMoney();
    $('.numberOnly').ForceNumericOnly();
    $('.numbers').keyup(function () {
   		this.value = this.value.replace(/[^0-9\.]/g,'');
	});
 	$('.cep_input').inputmask ('999999-999');
 	
 	$("input[id*='cpfcnpj']").inputmask({
   		 mask: ['999.999.999-99', '99.999.999/9999-99'],	
    		keepStatic: true
	});
});

// Numeric only control handler
jQuery.fn.ForceNumericOnly =
function()
{
    return this.each(function()
    {
        $(this).keydown(function(e)
        {
            var key = e.charCode || e.keyCode || 0;
            // allow backspace, tab, delete, enter, arrows, numbers and keypad numbers ONLY
            // home, end, period, and numpad decimal
            return (
                key == 8 || 
                key == 9 ||
                key == 13 ||
                key == 46 ||
                key == 110 ||
                key == 190 ||
                (key >= 35 && key <= 40) ||
                (key >= 48 && key <= 57) ||
                (key >= 96 && key <= 105));
        });
    });
};