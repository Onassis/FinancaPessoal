package br.com.fenix.abstrato;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.fenix.api.exceptionhandle.NegocioException;
import br.com.fenix.api.exceptionhandle.RegistroNaoExisteException;
import br.com.fenix.fi.conta.Conta;
import jakarta.validation.Valid;

public abstract class ServicoAbstrato<R extends CrudRepository<T,ID>,T ,ID> implements IServico< T,ID>   {

	
//	private final Class<? extends R> repository;
	 protected R repositorio  ;

	 public ServicoAbstrato(R repositorio) {
	        this.repositorio = repositorio;
	    }


    
	 @Override
	public Page<T> listarPagina(Pageable pageable) {
		return null;
	}




//	    @GetMapping("/")
//	    public String findAllPage(
//	            @RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber,
//	            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
//	            Model model,
//	            @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
//	        Page<Student> studentPage = studentRepo.findByNameContains(keyWord,PageRequest.of(pageNumber, size));
//	        model.addAttribute("students", studentPage.getContent());
//	        model.addAttribute("pages", new int[studentPage.getTotalPages()]);
//	        model.addAttribute("currentPage", pageNumber);
//	        model.addAttribute("keyWord", keyWord);
//	        return "index";
//	    }
	    @Override
	    public 	Optional<T>  buscarPorId (ID id) throws RegistroNaoExisteException {
	    	   return 	Optional.ofNullable(this.repositorio.findById(id)
	    			   .orElseThrow( () -> new RegistroNaoExisteException("Registro não encontrato") )) ;
	    }
	    @Override
    	public Iterable<T> listar () throws RegistroNaoExisteException {
			return this.repositorio.findAll();
		}
	    @Override
	    @Transactional
	    public T criar( T entidade ) throws NegocioException {
	    	antesDeSalvar(entidade);
		    entidade =  repositorio.save (entidade);
		    depoisDeSalvar(entidade);
		    return entidade;		    
	    }


	    @Override
	    @Transactional
	    public T atualizar(T entidade)  throws NegocioException {	    	
	    	antesDeSalvar(entidade);
		    entidade =  repositorio.save (entidade);
		    depoisDeSalvar(entidade);
		    return entidade;		    
	    }
	    @Override
	    @Transactional
	    public void excluirPorId(ID id){
	        antesDeExcluir(id);
	    	repositorio.deleteById(id);
	    }
	    @Override
	    @Transactional
	    public void excluirTodos(){
	       	repositorio.deleteAll();
	    }
	}
