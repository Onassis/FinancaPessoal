package br.com.fenix.configuracao;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 * Usado para identificar requisições duplicadas
 */
public class DuplicateRequestInterceptor implements HandlerInterceptor {
    // ... implementation to check for duplicate requests ...
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ... logic to check for duplicates and return false if found ...
//        String requestId = request.getHeader("requestId");

//        if (requestIds.contains(requestId))
//            throw new IllegalArgumentException("Violation Request; Reason requestId already registered");
//
//        requestIds.add(requestId);
//        return super.preHandle(request, response, handler);
        
        return true; // Allow request to proceed
    }
}
