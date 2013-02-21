/**
 * ESUP-Portail Helpdesk - Copyright (c) 2004-2009 ESUP-Portail consortium.
 */
package org.esupportail.helpdesk.services.feed.imap;

import java.util.Collection;

import org.htmlcleaner.HtmlTagProvider;
import org.htmlcleaner.TagInfo;

/**
 * A tag provider for HTML CLEANER that uses only a few tags.
 */
public class MessageHtmlTagProvider extends HtmlTagProvider {

    /**
	 * The serialization id. 
	 */
	private static final long serialVersionUID = -15410477847599959L;
	
	/**
     * Singleton instance.
     */
    private static MessageHtmlTagProvider instance;

    /**
     * Constructor.
     */
    public MessageHtmlTagProvider() {
        super();
    }

    /**
     * @return singleton instance of this class.
     */
    public static synchronized MessageHtmlTagProvider getInstance() {
        if (instance == null) {
            instance = new MessageHtmlTagProvider();
        }
        return instance;
    }
    
    /**
     * Definition of all HTML tags together with rules for tag balancing.
     */
    @SuppressWarnings("unchecked")
	@Override
	protected void defineTags() {
    	super.defineTags();
    	super.remove("img");
    	super.remove("wbr");
    	super.remove("spacer");
    	for (TagInfo tagInfo : (Collection<TagInfo>) values()) {
			tagInfo.setDeprecated(true);
		}
    	getTagInfo("script").setDeprecated(false);
    	getTagInfo("meta").setDeprecated(false);
    	getTagInfo("style").setDeprecated(false);
    	getTagInfo("div").setDeprecated(false);
    	getTagInfo("br").setDeprecated(false);
    	getTagInfo("span").setDeprecated(false);
    	getTagInfo("h1").setDeprecated(false);
    	getTagInfo("h2").setDeprecated(false);
    	getTagInfo("h3").setDeprecated(false);
    	getTagInfo("h4").setDeprecated(false);
    	getTagInfo("h5").setDeprecated(false);
    	getTagInfo("h6").setDeprecated(false);
    	getTagInfo("p").setDeprecated(false);
    	getTagInfo("pre").setDeprecated(false);
    	getTagInfo("strong").setDeprecated(false);
    	getTagInfo("em").setDeprecated(false);
    	getTagInfo("blockquote").setDeprecated(false);
    	getTagInfo("code").setDeprecated(false);
    	getTagInfo("a").setDeprecated(false);
    	getTagInfo("ul").setDeprecated(false);
    	getTagInfo("ol").setDeprecated(false);
    	getTagInfo("li").setDeprecated(false);
    	getTagInfo("dl").setDeprecated(false);
    	getTagInfo("dt").setDeprecated(false);
    	getTagInfo("dd").setDeprecated(false);
    	getTagInfo("table").setDeprecated(false);
    	getTagInfo("tr").setDeprecated(false);
    	getTagInfo("td").setDeprecated(false);
    	getTagInfo("th").setDeprecated(false);
    	getTagInfo("tbody").setDeprecated(false);
    	getTagInfo("thead").setDeprecated(false);
    	getTagInfo("tfoot").setDeprecated(false);
    	getTagInfo("col").setDeprecated(false);
    	getTagInfo("colgroup").setDeprecated(false);
    	getTagInfo("caption").setDeprecated(false);
    	getTagInfo("b").setDeprecated(false);
    	getTagInfo("i").setDeprecated(false);
    	getTagInfo("u").setDeprecated(false);
    	getTagInfo("tt").setDeprecated(false);
    	getTagInfo("sub").setDeprecated(false);
    	getTagInfo("sup").setDeprecated(false);
    	getTagInfo("big").setDeprecated(false);
    	getTagInfo("small").setDeprecated(false);
    	getTagInfo("strike").setDeprecated(false);
    	getTagInfo("blink").setDeprecated(false);
    	getTagInfo("marquee").setDeprecated(false);
    	getTagInfo("s").setDeprecated(false);
    	getTagInfo("hr").setDeprecated(false);
    	getTagInfo("font").setDeprecated(false);
    	getTagInfo("basefont").setDeprecated(false);
        getTagInfo("title").setDeprecated(false);
    }

}