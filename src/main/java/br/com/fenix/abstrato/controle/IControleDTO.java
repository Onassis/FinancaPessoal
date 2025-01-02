
package br.com.fenix.abstrato.controle;

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

public interface IControleDTO<T,DTO,ID> extends IControle<T,ID>{
//	String nomeClasse();
//  	String urlCadastrar();
//	String urlListar();
//	String cadastroHtml();
//	String listarHtml();
//
//	String nomeEntidade();
//	String listarView(ModelMap model);
		
	DTO entidadeToDto(T t);
    T dtoToEntidade(DTO dto);
    DTO  buscarPorId(@PathVariable ID id);
	 
	 Iterable<DTO> listarDTO();

//	String urlEditar(ID id);
	String cadastrarDTO(DTO dto);
//	String atualizarView(ID id, ModelMap model, RedirectAttributes attr);
//	String salvar(@Valid T entidade, BindingResult result, RedirectAttributes attr);
	String excluirPorId(ID id, RedirectAttributes attr);
	String salvarDTO(@Valid DTO dto, BindingResult result, RedirectAttributes attr);
	
}
