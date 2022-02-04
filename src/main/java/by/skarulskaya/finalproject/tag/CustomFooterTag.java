package by.skarulskaya.finalproject.tag;

import jakarta.servlet.jsp.JspTagException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

public class CustomFooterTag extends TagSupport {
    @Override
    public int doStartTag() throws JspTagException {
        JspWriter writer = pageContext.getOut();
        String content = "<footer><p class='footer'>© 2022 BLACKPINK | SHOP. ALL RIGHTS RESERVED.</p></footer>";
        try {
            writer.write(content);
        } catch (IOException e) {
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }
}
