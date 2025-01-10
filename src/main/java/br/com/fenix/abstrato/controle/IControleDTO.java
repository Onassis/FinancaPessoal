
package br.com.fenix.abstrato.controle;

import org.springframework.data.domain.Persistable;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/*
 * Interface de controle restfull 
 */

public interface IControleDTO<T,DTO extends Persistable<ID>,ID> extends IControle<T,ID>{

	String cadastrarDTO(DTO dto);

	String excluirPorId(ID id, RedirectAttributes attr);
	String salvarDTO(@Valid DTO dto, BindingResult result, RedirectAttributes attr);
	
}
