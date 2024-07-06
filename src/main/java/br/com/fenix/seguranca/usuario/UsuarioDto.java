package br.com.fenix.seguranca.usuario;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.fenix.seguranca.usuario.Usuario.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UsuarioDto {
	
//	@UniqueEmail
	
	@NotBlank
	@jakarta.validation.constraints.Size(min=1, max=250)
	private String email;
	
	@NotBlank
	private String firstName; 
	@NotBlank
	private String lastName;
	
//	@Password
	@NotBlank
	@jakarta.validation.constraints.Size(min=1, max=6)
	private String password;

	private int enabled=1;
	private int verificado=0;
	
    @Enumerated(EnumType.STRING)
	private Collection<Role> roles = new HashSet<Role>();

	public Usuario toUsuario() {
		
		Usuario usuario = new Usuario(this.email,this.firstName,this.lastName,this.password);
		usuario.setRoles(this.roles);
		usuario.setVerificado(this.verificado);
		usuario.setEnabled(this.enabled);
		return usuario;
	}


	public UsuarioDto() {
		super();
	}


	public UsuarioDto(@NotBlank @Size(min = 1, max = 250) String email, @NotBlank String firstName, @NotBlank String lastName,
					  @NotBlank @Size(min = 1, max = 6) String password, int enabled, int verificado, Collection<Role> roles) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.enabled = enabled;
		this.verificado = verificado;
		this.roles = roles;
	}

}
