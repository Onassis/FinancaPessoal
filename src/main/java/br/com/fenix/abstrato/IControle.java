package br.com.fenix.abstrato;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Interface de controle restfull 
 */

public interface IControle<T,ID> {
	 String nomeEntidade(T entidade);
	 String nomeCadastro(T entidade);
	 String nomeListar(T entidade);	 
	 ModelAndView cadastrar(T entidade);
	 ModelAndView atualizarView(@PathVariable ID id);
	 ModelAndView listarView(T entidade);
	 T buscarPorId(@PathVariable ID id);	 
	 Iterable<T> listar();
	 ModelAndView   criar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 T atualizar(@RequestBody T entidade);
	 void excluirPorId(@PathVariable ID id);
	void excluirTodos();
}
