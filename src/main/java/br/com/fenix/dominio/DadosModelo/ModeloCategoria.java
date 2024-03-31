package br.com.fenix.dominio.DadosModelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.fenix.abstrato.EntidadeAbstrata;
import br.com.fenix.dominio.enumerado.TipoLancamento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@EqualsAndHashCode(callSuper=true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ModeloCategoria extends EntidadeAbstrata<Long> {
    @Column(length = 40, nullable =  false)
	private String descricao;
	

    @Column(length = 2, nullable =  false, updatable = false)
    @Enumerated(EnumType.STRING)
	private TipoLancamento tipoLancamento;
    
    @JsonIgnore    
    @OneToMany(mappedBy = "ModeloCategoria", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Column(nullable = false)
    private List<ModeloSubCategoria> subModeloCategoria ;
    
}
