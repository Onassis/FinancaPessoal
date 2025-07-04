package br.com.fenix.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import jakarta.servlet.ServletContext;

//@Configuration
//@EnableWebMvc
public class WebConfig 
//implements WebMvcConfigurer 
{
//	@Autowired
//	private ServletContext servletContext;
//
//	@Override
//	public void configureViewResolvers(ViewResolverRegistry registry) {
//		registry.freeMarker();
//
//	}
//	@Bean 
//	public FreeMarkerConfigurer freemarkerConfig() { 
//	    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer(); 
//	    freeMarkerConfigurer.setServletContext(this.servletContext) ;
//	    freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views/ftl/");
//	    return freeMarkerConfigurer; 
//	}
//	// Configure FreeMarker...
////
//	@Bean 
//	public FreeMarkerViewResolver freemarkerViewResolver() { 
//	    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver(); 
//	    resolver.setCache(true); 
//	    resolver.setPrefix(""); 
//	    resolver.setSuffix(".ftl"); 
//	    return resolver; 
//	}


//	@Bean 
//	public FreeMarkerConfigurer freemarkerConfig() { 
//	    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer(); 
////	    TaglibFactory taglibFactory = new TaglibFactory((javax.servlet.ServletContext)  servletContext);
//	    
//	    freeMarkerConfigurer.setServletContext( servletContext) ;
////	    List<String> tldPaths = new ArrayList<>();
////        tldPaths.add("/META-INF/security.tld"); // ¡La ruta debe empezar con /!
////
////        // 5. Asignar la lista de TLDs a la fábrica
//////        taglibFactory.
//////        .setClasspathTlds(tldPaths);
//////
//////        // 6. Asignar la TaglibFactory al FreeMarkerConfigurer
////        freeMarkerConfigurer.setTaglibFactory(taglibFactory);
////        
//	    
//	    
////	    .put(FreemarkerServlet.KEY_JSP_TAGLIBS, new TaglibFactory((javax.servlet.ServletContext) servletContext)) ;
//////	    .put(FreemarkerServlet.KEY_JSP_TAGLIBS, (javax.servlet.ServletContext) new TaglibFactory(servletContext)) ;
//	    freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views/ftl/");
//	    return freeMarkerConfigurer; 
//	}
//	@Bean
//	public FreeMarkerConfigurer freeMarkerConfigurer() {
//		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
////		configurer.setFreemarkerVariables(FreemarkerServlet.KEY_JSP_TAGLIBS, (Object)new TaglibFactory(servletContext)) ;
////		    return fmModel;
//		configurer.setTemplateLoaderPath("/freemarker");
//		configurer.setDefaultCharset(StandardCharsets.UTF_8);
//		return configurer;
//	}
}