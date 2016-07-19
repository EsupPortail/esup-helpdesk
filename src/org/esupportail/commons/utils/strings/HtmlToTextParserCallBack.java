/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.utils.strings; 

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * A class to convert HTML to text using Swing.
 */
public final class HtmlToTextParserCallBack extends HTMLEditorKit.ParserCallback {   
	
	/**
	 * A StringBuffer to store the result of the parsing.
	 */
	private StringBuffer textResult = new StringBuffer();

	/**
	 * Private constructor.
	 */
	private HtmlToTextParserCallBack() {
		super();
	}

	/**
	 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleText(char[], int)
	 */
	@Override
	public void handleText(
			final char[] data, 
			@SuppressWarnings("unused") final int unusedPos) {      
		textResult.append(data);
		}    
	
	/**
	 * Parse HTML and return text.
	 * @param htmlString an HTML string.
	 * @return a String.
	 * @throws IOException
	 */
	static String convert(final String htmlString) throws IOException {
		Reader r = new StringReader(htmlString);      
		ParserDelegator parser = new ParserDelegator();      
		HtmlToTextParserCallBack callback = new HtmlToTextParserCallBack();
		parser.parse(r, callback, false);
		return callback.getTextResult().toString();
	}

	/**
	 * @return Returns the textResult.
	 */
	StringBuffer getTextResult() {
		return textResult;
	}
}
