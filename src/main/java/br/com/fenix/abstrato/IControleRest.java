package br.com.fenix.abstrato;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

/*
 * Interface de controle restfull 
 */

public interface IControleRest<T> {
	 String nomeEntidade(T entidade);
	 String nomeCadastro(T entidade);
	 String nomeListar(T entidade);	 
	 ModelAndView cadastrar(T entidade);
	 ModelAndView atualizarView(@PathVariable long ID);
	 ModelAndView listarView(T entidade);
	 T buscarPorId(@PathVariable long ID);	 
	 Iterable<T> listar();
	 T criar(@RequestBody T entidade);
	 T atualizar(@RequestBody T entidade);
	 void excluirPorId(@PathVariable long ID);
	void excluirTodos();
}
