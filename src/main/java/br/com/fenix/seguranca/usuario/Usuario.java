package br.com.fenix.seguranca.usuario;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(indexes = { @Index(name = "idx_email",columnList = "email", unique=true),@Index(name = "idx_cpf", columnList = "cpf", unique=true) })
@Getter
@Setter
@ToString
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;


	public static enum Role {
		UNVERIFIED, ADMIN, USER,  PREMIO, BLOCKED
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false, length=250)

	@NotBlank(message = "{email.notbank}")
	@NotNull
	@Email
	private String email;
	
	@Column(nullable=false, length=40)
	@NotEmpty
	private String firstName; 
	
	@Column(nullable=false, length=80)
	@NotBlank
	private String lastName;
	
	@Column(nullable=false, length=250)
	@NotBlank(message = "O cpf é obrigatório")
	@CPF
	private String cpf; 

	@Column(nullable=false)
	@NotBlank
	private String password;
	 
	private String verificationToken;
	
	@Column(nullable=false)
	private int enabled;
	@Column(nullable=false)
	private int verificado;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
	private Collection<Role> roles = new HashSet<Role>();
	
	public Usuario() {
		super();
	}
	
	public Usuario(String email, String firstName, String lastName, String password) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.roles.add(Role.UNVERIFIED);
		this.enabled = 1;
	}
	
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
				.collect(Collectors.toSet()); 
	}

	@Override
	public String getUsername() {
		
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled==1;
	}

	



}
