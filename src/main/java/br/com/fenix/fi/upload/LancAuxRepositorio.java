package br.com.fenix.fi.upload;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;


@Repository
public interface LancAuxRepositorio extends GenericRepository<LancAux> {
	@Override
	@Query("from LancAux o where o.criadoPor.id = ?#{ principal.id} order by o.Id")
	public ArrayList<LancAux> findAll(); 
	
	@Modifying
	@Query("delete from LancAux o where o.criadoPor.id = ?#{ principal.id}")
	public void deleteAll(); 
	 
}
