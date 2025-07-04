package br.com.fenix.configuracao;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.fenix.seguranca.usuario.Usuario;
import br.com.fenix.seguranca.usuario.UsuarioRepositorio;
import br.com.fenix.seguranca.usuario.UsuarioServicoImp;
import br.com.fenix.seguranca.util.UtilSerguranca;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfig  {

		class AuditorAwareImp implements AuditorAware<Usuario> {
	      @Override
		  public Optional<Usuario> getCurrentAuditor() {

		    return UtilSerguranca.currentUser();
		  }
		}
		@Bean
		public UserDetailsService userDetailsService() {
			return new UsuarioServicoImp();
			
		}
//	    @Bean
//	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//	        return authenticationConfiguration
//	        		.
//	        		.getAuthenticationManager();
//	    }
	    
//		   @Bean
//		    public AuthenticationProvider authenticationProvider(){
//		        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//		        provider.setUserDetailsService(userDetailsService());
//		        provider.setPasswordEncoder(passwordEncoder());
//		        return provider;
//
//		    }
		/* Configurando qual criptografia utilizar   */	
		@Bean
		PasswordEncoder passwordEncoder() {		
			return new BCryptPasswordEncoder();
		}

		@Bean
	     public AuditorAware<Usuario> auditorProvider() {
		    return new AuditorAwareImp();
		  }
//		 @Bean
//		 public WebSecurityCustomizer ignoringCustomizer() {
//				System.out.println("ignoringCustomizer");
//		      return ( web -> web.ignoring()
//		    		  .requestMatchers("favicon.ico",
//		    				  			"fontawresome/**",
//		    				  			"/vendor/**",
//		    				  			"/webjars/**",
//		    				  			"/assets/**",
//		    				  			"/css/**",
//		    				  			"/fonts/**",
//		    				  			"/img/**",
//		    				  			"/js/**",
//		    				  			"js/**", 	    				  				    				  			
//		    				  			"/fragments/**"));  
//		 }
	    @Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
				System.out.println("securityFilterChain");
				http
				    .csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(
							(requests) -> requests 
						//	.requestMatchers("/").permitAll()
									.requestMatchers("/login", "/verify-email","/layout","/usuario/cadastrar" ).permitAll()
									.requestMatchers("/error").permitAll()	
									.requestMatchers("/favicon.ico").permitAll()
									.requestMatchers("/fontawresome/**").permitAll()
	    				  			.requestMatchers("/vendor/**").permitAll()
	    				  			.requestMatchers("/assets/**").permitAll()
	    				  			.requestMatchers("/css/**").permitAll()
	    				  			.requestMatchers("/fonts/**").permitAll()
	    				  			.requestMatchers("/img/**").permitAll()
	    				  			.requestMatchers("/fragments/**").permitAll()									
									.requestMatchers("/fragments/**").permitAll()	
									.requestMatchers("/webjars/**").permitAll()
//									.requestMatchers("../static/css/**").permitAll()	
//									.requestMatchers("../static/js/**").permitAll()	
//									.requestMatchers("../static/img/**").permitAll()	
									.requestMatchers("/admin/**").hasRole("ADMIN")
									.requestMatchers(HttpMethod.POST, "/usuario/**").permitAll()		
									.requestMatchers(HttpMethod.GET, "/usuario/**").permitAll()	
									.requestMatchers(HttpMethod.DELETE, "/usuario/**").hasRole("ADMIN")								
									.requestMatchers(HttpMethod.PUT, "/usuario/**").hasRole("USER")								
									.anyRequest().authenticated()
							)
					.formLogin(form -> form
							.loginPage("/login").permitAll()				    
							.failureUrl("/login-error.html")
							)
				
				    .rememberMe(Customizer.withDefaults())
				    ;
					 	
					return http.build();
				}

//		@Bean 
//		public AuthenticationManager authenticationManager(HttpSecurity http,
//								PasswordEncoder  passwordEncoder, 
//								UsuarioServicoImp userDetailServer) throws Exception {
//			System.out.println("authenticationManager");
//			return http
//					.getSharedObject(AuthenticationManagerBuilder.class)
//					.userDetailsService(new UsuarioServicoImp())
//					.passwordEncoder(passwordEncoder)
//					.and()
//					.build()
//					;
//		}
//	    @Bean
//	    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//	        return configuration.getAuthenticationManager();
//	    }
//		private AuthenticationManager authenticationManager() {
//			DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//			authenticationProvider.setUserDetailsService(userDetailsService());
//			authenticationProvider.setPasswordEncoder(passwordEncoder());
//
//			ProviderManager providerManager = new ProviderManager(authenticationProvider);
//			providerManager.setEraseCredentialsAfterAuthentication(false);
//
//			return providerManager;
//		}

	    /*
	     * 
	     *  Bean securityExtension para usar   @Query ?#{ principal.id}") 
	     *  
	     *  */
	    
		@Bean
		EvaluationContextExtension securityExtension() {
		    return new EvaluationContextExtension() {

		        @Override
		        public String getExtensionId() {
		            return "security";
		        }

		        @Override
		        public SecurityExpressionRoot getRootObject() {
		            return new SecurityExpressionRoot(SecurityContextHolder.getContext().getAuthentication()) {
		            };
		        }
		    };
		}
	



		
		
		
	
	/* Retorna objeto de autenticação e verifica o usuario e senha   */
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(userDetailsService());
//		authenticationProvider.setPasswordEncoder(passwordEncoder());
//		return authenticationProvider;
//	}
	
//	@Bean 
//	public AuthenticationManager authenticationManager(HttpSecurity http,
//							PasswordEncoder  passwordEncoder, 
//							UsuarioServicoImp userDetailServer) throws Exception {
//		System.out.println("authenticationManager");
//		return http.getSharedObject(AuthenticationManagerBuilder.class)
//				.userDetailsService(userDetailsService())
//				.passwordEncoder(passwordEncoder)
//				.and()
//				.build()
//				;
//	}




}	
							
/**					.and()
							.formLogin( form -> {
								try {
									form
										.loginPage("/login")
										.defaultSuccessUrl("/", true)
								        .failureUrl("/login-error")
										.permitAll()
						.and()
						 	.logout();
									} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
**/							
							
/**				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				)
**/				
		
/*		
		http
//		 	.httpBasic().disable()
//			.csrf().disable()
			.authorizeHttpRequests(
				(authz) -> authz				    					
//					.requestMatchers(HttpMethod.GET,"/admin/**").hasAnyRole("ADMIN")
				     .requestMatchers(HttpMethod.GET,"/").permitAll()
				     .requestMatchers(HttpMethod.GET,"/usuario/cadastrar").permitAll()
/*						.requestMatchers(HttpMethod.POST, "/usuario").permitAll()					
				     
				     .requestMatchers(HttpMethod.GET, "/home").permitAll()
					.requestMatchers(HttpMethod.GET, "/fragments/**").permitAll()		

					.requestMatchers(HttpMethod.GET, "/","/layout").permitAll()
					.requestMatchers(HttpMethod.GET, "/usuario/cadastrar").permitAll()
					.requestMatchers(HttpMethod.POST, "/usuario").permitAll()					
					.requestMatchers(HttpMethod.GET,"/fragments").permitAll()
						
					.anyRequest().authenticated()									
			)
			
			.formLogin((form) -> form
				    .loginPage("/login")
			           .defaultSuccessUrl("/", true)
			           .failureUrl("/login-error")
			           .permitAll()
			      ).logout( (logout) -> logout
			           .logoutSuccessUrl("/")
			           .deleteCookies("JSESSIONID")
			      ).exceptionHandling( (ex) -> ex
			           .accessDeniedPage("/negado")
			      );					
/*				.loginPage("/login")
				.permitAll()
//				.failureForwardUrl("/login-error.html")
			)
			.logout((logout) -> logout.permitAll());
			
			
		
		return http.build();
*/		

