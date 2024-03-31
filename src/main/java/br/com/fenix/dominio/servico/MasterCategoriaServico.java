package br.com.fenix.dominio.servico;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fenix.dominio.modelo.DadoBasico.SubCategoria;

@Service
public class MasterCategoriaServico {
	
	@PersistenceContext
	private EntityManager em;
	
	public SubCategoria findById(Long id) {
		SubCategoria categoria = null;
		   try {
			      Query query = em.createQuery("SELECT c FROM SubCategoria c WHERE c.id = :id");			   
			      query.setParameter("id", id);
			      categoria =  (SubCategoria) query.getSingleResult();    
			      
			   } catch (NoResultException e) {
			      return null;
			   } catch (RuntimeException e) {
			      e.printStackTrace();
			   }
		return categoria;
	}
}
