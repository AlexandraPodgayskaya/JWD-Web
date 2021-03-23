package by.epam.payment_system.service.util.jsp_tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CopyrightTag extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	private static final String FOOTER = "Copyright by Aleksandra Podgayskaya 2021 ";
	private static final String FOOTER_TAG_START = "<footer>";
	private static final String FOOTER_TAG_END = "</footer>";

	    @Override
	    public int doStartTag() throws JspTagException {
	        try {
	            JspWriter out = pageContext.getOut();
	            out.write(FOOTER_TAG_START);
	            out.write(FOOTER);

	        } catch (IOException e) {
	            throw new JspTagException(e);
	        }
	        return EVAL_BODY_INCLUDE;
	    }


	    @Override
	    public int doEndTag() throws JspTagException {
	        try {
	            pageContext.getOut().write(FOOTER_TAG_END);
	        } catch (IOException e) {
	            throw new JspTagException(e);
	        }
	        return EVAL_PAGE;
	    }
	}



