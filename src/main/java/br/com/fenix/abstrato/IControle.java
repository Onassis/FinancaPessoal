package br.com.fenix.abstrato;

import java.net.URI;
import java.util.Optional;

import org.springframework.data.domain.Persistable;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/*
 * Interface de controle restfull 
 */

public interface IControle<T ,ID> {
	 String nomeClasse();
	 String cadastroHtml();
	 String listarHtml();
	 String urlListar();
	 String urlCadastrar();
	 String urlEditar(ID id);
	 /*  ------------------------------------ CRUD METODOS --------------------------------------------- 
	 /**
	  * 
	  * @param entidade
	  * @return
	  */
	 String listarView(ModelMap model);

	 String cadastrar(T entidade);
	 
	 String atualizarView(@PathVariable ID id, ModelMap model,RedirectAttributes attr);
	 	 
	 String salvar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
	 String  excluirPorId(ID id,RedirectAttributes attr);
}
