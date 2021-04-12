package by.epam.payment_system.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Filter for setting the encoding
 * 
 * @author Aleksandra Podgayskaya
 * @see Filter
 */
public class CharsetFilter implements Filter {

	private static final Logger logger = LogManager.getLogger();

	private static final String INIT_PARAMETER = "characterEncoding";
	private static final String DEFAULT_ENCODING = "UTF-8";

	private String encoding;

	/**
	 * Initialization of the encoding
	 * 
	 * @param fConfig {@link FilterConfig}
	 */
	@Override
	public void init(FilterConfig fConfig) {
		encoding = fConfig.getInitParameter(INIT_PARAMETER);
		if (encoding == null) {
			encoding = DEFAULT_ENCODING;
		}
		logger.info("CharsetFilter is initialized.");
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
		logger.info("Charset was set.");

		chain.doFilter(request, response);
	}

	/**
	 * Encoding destroy
	 */
	@Override
	public void destroy() {
		encoding = null;
	}
}
