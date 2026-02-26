package com.oceanview.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Filter to set character encoding to UTF-8
 */
@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {
    
    private static final String ENCODING = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
    }
}
