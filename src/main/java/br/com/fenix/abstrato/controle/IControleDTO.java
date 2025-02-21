
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

public interface IControleDTO<T,DTO,ID> {

	 String nomeClasse();
	 String nomeClasseDTO();
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
	 	 
//	 String salvar(@RequestBody T entidade,BindingResult result, RedirectAttributes attr);
	 
	 
	 String inserir(T entidade,RedirectAttributes attr);
	 String alterar(T entidade,RedirectAttributes attr) ;
		 
//	 String  excluirPorId(ID id,RedirectAttributes attr);


	String excluirPorId(ID id, RedirectAttributes attr);
	
	String salvarDTO(@Valid DTO dto, BindingResult result, RedirectAttributes attr);
	String cadastrarDTO(DTO dto,ModelMap model);


}
