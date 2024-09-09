package br.com.fenix.abstrato;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Persistable;
import org.springframework.ui.ModelMap;
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
	 String nomeEntidade();
	 String nomeCadastro();
	 String nomeListar();
	 String urlListar();
	 String urlCadastrar();
	 T      novaInstacia();
	 String cadastrar(T entidade);
	 String listarView(ModelMap model);
	 
	 String atualizarView(@PathVariable ID id, ModelMap model);
//	 ModelAndView listarView();

	 String excluirPorId(@PathVariable ID id, ModelMap model);
//	 RedirectView excluirPorId(@PathVariable ID id);
//	 void excluirPorId(@PathVariable ID id);
	 
	 
	 
	 String salvar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
		// RedirectView salvar2(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
	// ModelAndView   criar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
//	 ModelAndView salvar(T entidade);
//	 ModelAndView cadastrar(T entidade);
	 	 
//	 Optional<T> buscarPorId(@PathVariable ID id);	 
//	 Iterable<T> listar();

//	 T atualizar(@RequestBody T entidade);
//	void excluirTodos();
}
