package by.epam.payment_system.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class CharsetFilter implements Filter {

    private static final String INIT_PARAMETER = "characterEncoding";

    private String encoding;
    private ServletContext context;

    @Override
    public void destroy(){
        context = null;
    }

  
    @Override
    public void init(FilterConfig fConfig){
        encoding = fConfig.getInitParameter(INIT_PARAMETER);
        context = fConfig.getServletContext();
        context.log("CharsetFilter is initialized.");
    }

  
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        context.log("Charset was set.");

        chain.doFilter(request, response);
    }

}
