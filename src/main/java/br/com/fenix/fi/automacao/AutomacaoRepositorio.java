package br.com.fenix.fi.automacao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.fenix.abstrato.repositorio.GenericRepository;
import br.com.fenix.abstrato.repositorio.JpaRepositoryAuditavel;
import br.com.fenix.fi.favorecido.Favorecido;


@Repository
public interface AutomacaoRepositorio extends JpaRepositoryAuditavel<Automacao,Long> {
	
	
}
