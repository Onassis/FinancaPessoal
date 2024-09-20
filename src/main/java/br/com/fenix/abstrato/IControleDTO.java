
package br.com.fenix.abstrato;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Interface de controle restfull 
 */

public interface IControleDTO<T,DTO,ID> {
  	String urlCadastrar();
	String urlListar();
	String cadastroHtml();
	String listarHtml();

	String nomeEntidade();
		
	 ModelAndView cadastrar(DTO dto);
	 
	 ModelAndView atualizarView(@PathVariable ID id);

	 
	 DTO  buscarPorId(@PathVariable ID id);
	 
	 Iterable<T> listar();
	 String   criar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 T atualizar(@RequestBody T entidade);
	 void excluirPorId(@PathVariable ID id);
	 void excluirTodos();
	ModelAndView listarView(ModelMap model);
	
}
