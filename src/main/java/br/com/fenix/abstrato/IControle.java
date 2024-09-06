package br.com.fenix.abstrato;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Persistable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/*
 * Interface de controle restfull 
 */

public interface IControle<T ,ID> {
	 String nomeEntidade(T entidade);
	 String nomeCadastro(T entidade);
	 String nomeListar(T entidade);
	 String urlListar(T entidade);
	 String urlCadastrar(T entidade);
	 ModelAndView cadastrar(T entidade);
	 ModelAndView atualizarView(@PathVariable ID id);
	 ModelAndView listarView(T entidade);
//	 ModelAndView listarView2(T entidade, RedirectAttributes attr);
	 ModelAndView excluirPorId(@PathVariable ID id, RedirectAttributes attr);
//	 RedirectView excluirPorId(@PathVariable ID id);
//	 void excluirPorId(@PathVariable ID id);
	 
	 
	 RedirectView salvar2(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
	 ModelAndView salvar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
	// ModelAndView   criar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
//	 ModelAndView salvar(T entidade);
//	 ModelAndView cadastrar(T entidade);
	 	 
//	 Optional<T> buscarPorId(@PathVariable ID id);	 
//	 Iterable<T> listar();

//	 T atualizar(@RequestBody T entidade);
//	void excluirTodos();
}
