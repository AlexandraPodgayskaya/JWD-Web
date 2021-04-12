package by.epam.payment_system.view;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Custom tag copyright
 * 
 * @author Aleksandra Podgayskaya
 * @see TagSupport
 */
public class CopyrightTag extends TagSupport {

	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 1L;
	private static final String FOOTER = "Copyright by Aleksandra Podgayskaya 2021 ";
	private static final String FOOTER_TAG_START = "<footer>";
	private static final String P_TAG_START = "<p>";
	private static final String FOOTER_TAG_END = "</footer>";
	private static final String P_TAG_END = "</p>";

	@Override
	public int doStartTag() throws JspTagException {
		try {
			JspWriter out = pageContext.getOut();
			out.write(FOOTER_TAG_START);
			out.write(P_TAG_START);
			out.write(FOOTER);
			out.write(P_TAG_END);

		} catch (IOException e) {
			logger.error(e);
			throw new JspTagException(e);
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspTagException {
		try {
			pageContext.getOut().write(FOOTER_TAG_END);
		} catch (IOException e) {
			logger.error(e);
			throw new JspTagException(e);
		}
		return EVAL_PAGE;
	}
}
