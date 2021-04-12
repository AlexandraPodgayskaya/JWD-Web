package by.epam.payment_system.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filter for setting the encoding
 * 
 * @author Aleksandra Podgayskaya
 * @see Filter
 */
public class CharsetFilter implements Filter {

	private static final String INIT_PARAMETER = "characterEncoding";

	private String encoding;
	private ServletContext context;

	/**
	 * ServletContext destroy
	 */
	@Override
	public void destroy() {
		context = null;
	}

	/**
	 * Initialization of the encoding
	 * 
	 * @param fConfig {@link FilterConfig}
	 */
	@Override
	public void init(FilterConfig fConfig) {
		encoding = fConfig.getInitParameter(INIT_PARAMETER);
		context = fConfig.getServletContext();
		context.log("CharsetFilter is initialized.");
	}

	/**
	 * Set encoding
	 * 
	 * @param request  {@link ServletRequest}
	 * @param response {@link ServletResponse}
	 * @param chain    {@link FilterChain}
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		context.log("Charset was set.");

		chain.doFilter(request, response);
	}

}
