/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.commons.web.tags.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.myfaces.portlet.PortletUtil;
import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * A bean to configure extended tags.
 */
public class TagsConfigurator extends AbstractApplicationAwareBean {

	/**
	 * The name of the bean to configure the tags.
	 */
	public static final String TAGS_CONFIGURATOR_BEAN = "tagsConfigurator"; 

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 6367237296314953333L;

	/**
	 * The default doctype.
	 */
	private static final String DEFAULT_DOCTYPE =
		 "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";

	/**
	 * The default style class for the document body.
	 */
	private static final String DEFAULT_DOCUMENT_BODY_STYLE_CLASS = "portlet-section-body"; 

	/**
	 * The default class for info messages.
	 */
	private static final String DEFAULT_MESSAGES_INFO_CLASS = "portlet-msg-info"; 

	/**
	 * The default class for warn messages.
	 */
	private static final String DEFAULT_MESSAGES_WARN_CLASS = "portlet-msg-alert"; 

	/**
	 * The default class for error messages.
	 */
	private static final String DEFAULT_MESSAGES_ERROR_CLASS = "portlet-msg-error"; 

	/**
	 * The default class for fatal messages.
	 */
	private static final String DEFAULT_MESSAGES_FATAL_CLASS = "portlet-msg-error"; 

	/**
	 * The default layout for messages.
	 */
	private static final String DEFAULT_MESSAGES_LAYOUT = "table"; 
	
	
	/**
	 * The default style class for labels.
	 */
	private static final String DEFAULT_LABEL_STYLE_CLASS = "portlet-form-field-label"; 
	/**
	 * The default style class for Button.
	 */
	private static final String DEFAULT_BUTTON_STYLE_CLASS = "portlet-form-button"; 

	/**
	 * The default style class for input Field.
	 */
	private static final String DEFAULT_INPUT_FIELD_STYLE_CLASS = "portlet-form-input-field";

	/**
	 * The default style class for datatable header.
	 */
	private static final String DEFAULT_DATATABLE_HEADER_CLASS = "portlet-table-header";

	/**
	 * The default style class for datatable footer.
	 */
	private static final String DEFAULT_DATATABLE_FOOTER_CLASS = "portlet-table-footer";
	
	/**
	 * The default style class for datatable row.
	 */
	private static final String DEFAULT_DATATABLE_ROW_CLASS = "portlet-table-text";
	
	/**
	 * The default style class for datatable row.
	 */
	private static final String DEFAULT_DATATABLE_ROW_ALTERNATE_CLASS = "portlet-table-alternate";
	
	/**
	 * The default style class for datatable column.
	 */
	private static final String DEFAULT_DATATABLE_COLUMN_CLASS = "portlet-table-text";

	/**
	 * The default style class for checkboxes.
	 */
	private static final String DEFAULT_CHECKBOX_STYLE_CLASS = "portlet-form-field";
	
	/**
	 * The default wrapping tag for sections.
	 */
	private static final String DEFAULT_SECTION_TAG = "h1";

	/**
	 * The default style class for sections.
	 */
	private static final String DEFAULT_SECTION_STYLE_CLASS = "portlet-section-header";

	/**
	 * The default wrapping tag for subsections.
	 */
	private static final String DEFAULT_SUBSECTION_TAG = "h2";

	/**
	 * The default style class for subsections.
	 */
	private static final String DEFAULT_SUBSECTION_STYLE_CLASS = "portlet-section-subheader";

	/**
	 * The default wrapping tag for paragraphs.
	 */
	private static final String DEFAULT_PARAGRAPH_TAG = "p";

	/**
	 * The default style class for paragraphs.
	 */
	private static final String DEFAULT_PARAGRAPH_STYLE_CLASS = "portlet-section-text";
	
	/**
	 * The default style class for normal text.
	 */
	private static final String DEFAULT_TEXT_STYLE_CLASS = DEFAULT_PARAGRAPH_STYLE_CLASS;
	
	/**
	 * The default layout for menu.
	 */
	private static final String DEFAULT_MENU_LAYOUT = "list"; 
	
	/**
	 * The default style class for menu.
	 */
	private static final String DEFAULT_MENU_STYLE_CLASS = "portlet-menu";
	/**
	 * The default style class for menu item.
	 */
	private static final String DEFAULT_MENU_ITEM_STYLE_CLASS = "portlet-menu-item";

	/**
	 * The default style class for menu item.
	 */
	private static final String DEFAULT_MENU_ACTIVE_ITEM_STYLE_CLASS = "portlet-menu-item-selected";

	/**
	 * The default style class for footer.
	 */
	private static final String DEFAULT_FOOTER_STYLE_CLASS = "portlet-menu";

	/**
	 * The default style class for footer item.
	 */
	private static final String DEFAULT_FOOTER_ITEM_STYLE_CLASS = "portlet-menu-item";
	
	/**
	 * The default style class for paginator.
	 */
	private static final String DEFAULT_PAGINATOR_STYLE_CLASS = "paginator";

	/**
	 * The media path for portlets.
	 */
	private static final String DEFAULT_PORTLET_MEDIA_PATH = "../media";
	
	/**
	 * The media path for servlets.
	 */
	private static final String DEFAULT_SERVLET_MEDIA_PATH = "/esup-application/media";
	
	/**
	 * A logger.
	 */
	private static final Logger LOGGER = new LoggerImpl(TagsConfigurator.class);

	/**
	 * The singleton bean.
	 */
	private static TagsConfigurator singleton; 
	
	/**
	 * The class for info messages.
	 */
	private String messagesInfoClass; 

	/**
	 * The class for warn messages.
	 */
	private String messagesWarnClass; 

	/**
	 * The class for error messages.
	 */
	private String messagesErrorClass; 

	/**
	 * The class for fatal messages.
	 */
	private String messagesFatalClass; 

	/**
	 * The layout for messages.
	 */
	private String messagesLayout; 

	/**
	 * The layout for menu.
	 */
	private String menuLayout; 

	/**
	 * The style class of the document body.
	 */
	private String documentBodyStyleClass;

	/**
	 * The document title.
	 */
	private String documentTitle;

	/**
	 * The document title i18n key (overrides documentTitle if set).
	 */
	private String documentTitleI18nKey;

	/**
	 * The doctype.
	 */
	private String doctype;

	/**
	 * The scripts.
	 */
	private List<String> scripts;

	/**
	 * The stylesheets.
	 */
	private List<String> stylesheets;

	/**
	 * The label style class.
	 */
	private String labelStyleClass;

	/**
	 * The input field style class.
	 */
	private String inputFieldStyleClass;

	/**
	 * The checkbox style class.
	 */
	private String checkboxStyleClass;

	/**
	 * The button style class.
	 */
	private String buttonStyleClass;

	/**
	 * The dataTable header class.
	 */
	private String dataTableHeaderClass;

	/**
	 * The dataTable footer class.
	 */
	private String dataTableFooterClass;

	/**
	 * The dataTable row class.
	 */
	private String dataTableRowClass;
	/**
	 * The dataTable row alternate class.
	 */
	private String dataTableRowAlternateClass;
	/**
	 * The dataTable column class.
	 */
	private String dataTableColumnClass;

	/**
	 * The subSection wrapping tag.
	 */
	private String subSectionTag;
	
	/**
	 * The subSection style class.
	 */
	private String subSectionStyleClass;
	
	/**
	 * The section wrapping tag.
	 */
	private String sectionTag;
	
	/**
	 * The section style class.
	 */
	private String sectionStyleClass;
	
	/**
	 * The paragraph wrapping tag.
	 */
	private String paragraphTag;
	
	/**
	 * The paragraph style class.
	 */
	private String paragraphStyleClass;
	
	/**
	 * The normal text style class.
	 */
	private String textStyleClass;
	
	/**
	 * The menu style class.
	 */
	private String menuStyleClass;

	/**
	 * The menu item style class.
	 */
	private String menuItemStyleClass;

	/**
	 * The active item style class for menu.
	 */
	private String menuActiveItemStyleClass;

	/**
	 * The footer text.
	 */
	private String footerText;
	
	/**
	 * The footer style class.
	 */
	private String footerStyleClass;
	
	/**
	 * The footer item style class.
	 */
	private String footerItemStyleClass;
	
	/**
	 * The paginator style class.
	 */
	private String paginatorStyleClass;
	
	/**
	 * The media path for portlets.
	 */
	private String portletMediaPath;
	
	/**
	 * The media path for servlets.
	 */
	private String servletMediaPath;
	
	/**
	 * Constructor.
	 */
	public TagsConfigurator() {
		super();
	}

	/**
	 * @return the default document title.
	 */
	protected String getDefaultDocumentTitle() {
		return getApplicationService().getName() + " v" + getApplicationService().getVersion();
	}

	/**
	 * @return the default footer text.
	 */
	protected String getDefaultFooterText() {
		return getApplicationService().getName() 
		+ " v" + getApplicationService().getVersion() 
		+ " - " + getApplicationService().getCopyright();
	}

	/**
	 * @see org.esupportail.commons.beans.AbstractApplicationAwareBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (!StringUtils.hasText(messagesInfoClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no messagesInfoClass set, using default [" 
					+ DEFAULT_MESSAGES_INFO_CLASS + "]");
			}
			messagesInfoClass = DEFAULT_MESSAGES_INFO_CLASS;
		}
		if (!StringUtils.hasText(messagesWarnClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no messagesWarnClass set, using default [" 
					+ DEFAULT_MESSAGES_WARN_CLASS + "]");
			}
			messagesWarnClass = DEFAULT_MESSAGES_WARN_CLASS;
		}
		if (!StringUtils.hasText(messagesErrorClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no messagesErrorClass set, using default [" 
					+ DEFAULT_MESSAGES_ERROR_CLASS + "]");
			}
			messagesErrorClass = DEFAULT_MESSAGES_ERROR_CLASS;
		}
		if (!StringUtils.hasText(messagesFatalClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no messagesFatalClass set, using default [" 
					+ DEFAULT_MESSAGES_FATAL_CLASS + "]");
			}
			messagesFatalClass = DEFAULT_MESSAGES_FATAL_CLASS;
		}
		if (!StringUtils.hasText(messagesLayout)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no messagesLayout set, using default [" 
					+ DEFAULT_MESSAGES_LAYOUT + "]");
			}
			messagesLayout = DEFAULT_MESSAGES_LAYOUT;
		}
		if (!StringUtils.hasText(documentBodyStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no documentBodyStyleClass set, using default [" 
					+ DEFAULT_DOCUMENT_BODY_STYLE_CLASS + "]");
			}
			documentBodyStyleClass = DEFAULT_DOCUMENT_BODY_STYLE_CLASS;
		}
		if (!StringUtils.hasText(documentTitle)) {
			documentTitle = getDefaultDocumentTitle();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no documentTitle set, using default [" 
						+ documentTitle + "]");
			}
		}
		if (!StringUtils.hasText(doctype)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no doctype set, using default [" + DEFAULT_DOCTYPE + "]");
			}
			doctype = DEFAULT_DOCTYPE;
		}
		if (CollectionUtils.isEmpty(scripts)) {
			scripts = new ArrayList<String>();
		}
		if (CollectionUtils.isEmpty(stylesheets)) {
			stylesheets = new ArrayList<String>();
		}
		if (!StringUtils.hasText(labelStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no labelStyleClass set, using default [" 
					+ DEFAULT_LABEL_STYLE_CLASS + "]");
			}
			labelStyleClass = DEFAULT_LABEL_STYLE_CLASS;
		}
		if (!StringUtils.hasText(inputFieldStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no inputFieldStyleClass set, using default [" 
					+ DEFAULT_INPUT_FIELD_STYLE_CLASS + "]");
			}
			inputFieldStyleClass = DEFAULT_INPUT_FIELD_STYLE_CLASS;
		}
		if (!StringUtils.hasText(buttonStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no buttonStyleClass set, using default [" 
					+ DEFAULT_BUTTON_STYLE_CLASS + "]");
			}
			buttonStyleClass = DEFAULT_BUTTON_STYLE_CLASS;
		}
		if (!StringUtils.hasText(dataTableHeaderClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no dataTableHeaderClass set, using default [" 
					+ DEFAULT_DATATABLE_HEADER_CLASS + "]");
			}
			dataTableHeaderClass = DEFAULT_DATATABLE_HEADER_CLASS;
		}
		if (!StringUtils.hasText(dataTableFooterClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no dataTableFooterClass set, using default [" 
					+ DEFAULT_DATATABLE_FOOTER_CLASS + "]");
			}
			dataTableFooterClass = DEFAULT_DATATABLE_FOOTER_CLASS;
		}
		if (!StringUtils.hasText(dataTableRowClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no dataTableRowClass set, using default [" 
					+ DEFAULT_DATATABLE_ROW_CLASS + "]");
			}
			dataTableRowClass = DEFAULT_DATATABLE_ROW_CLASS;
		}
		if (!StringUtils.hasText(dataTableRowAlternateClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no dataTableRowAlternateClass set, using default [" 
					+ DEFAULT_DATATABLE_ROW_ALTERNATE_CLASS + "]");
			}
			dataTableRowAlternateClass = DEFAULT_DATATABLE_ROW_ALTERNATE_CLASS;
		}
		if (!StringUtils.hasText(dataTableColumnClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no dataTableColumnClass set, using default [" 
					+ DEFAULT_DATATABLE_COLUMN_CLASS + "]");
			}
			dataTableColumnClass = DEFAULT_DATATABLE_COLUMN_CLASS;
		}
		if (!StringUtils.hasText(checkboxStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no checkboxStyleClass set, using default [" 
					+ DEFAULT_CHECKBOX_STYLE_CLASS + "]");
			}
			checkboxStyleClass  = DEFAULT_CHECKBOX_STYLE_CLASS;
		}
		if (!StringUtils.hasText(sectionTag)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no sectionTag set, using default [" 
					+ DEFAULT_SECTION_TAG + "]");
			}
			sectionTag = DEFAULT_SECTION_TAG;
		}
		if (!StringUtils.hasText(sectionStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no sectionStyleClass set, using default [" 
					+ DEFAULT_SECTION_STYLE_CLASS + "]");
			}
			sectionStyleClass = DEFAULT_SECTION_STYLE_CLASS;
		}
		if (!StringUtils.hasText(subSectionTag)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no subSectionTag set, using default [" 
					+ DEFAULT_SUBSECTION_TAG + "]");
			}
			subSectionTag = DEFAULT_SUBSECTION_TAG;
		}
		if (!StringUtils.hasText(subSectionStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no subSectionStyleClass set, using default [" 
					+ DEFAULT_SUBSECTION_STYLE_CLASS + "]");
			}
			subSectionStyleClass = DEFAULT_SUBSECTION_STYLE_CLASS;
		}
		if (!StringUtils.hasText(paragraphTag)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no paragraphTag set, using default [" 
					+ DEFAULT_PARAGRAPH_TAG + "]");
			}
			paragraphTag = DEFAULT_PARAGRAPH_TAG;
		}
		if (!StringUtils.hasText(paragraphStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no paragraphStyleClass set, using default [" 
					+ DEFAULT_PARAGRAPH_STYLE_CLASS + "]");
			}
			paragraphStyleClass = DEFAULT_PARAGRAPH_STYLE_CLASS;
		}
		if (!StringUtils.hasText(textStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no textStyleClass set, using default [" 
					+ DEFAULT_TEXT_STYLE_CLASS + "]");
			}
			textStyleClass = DEFAULT_TEXT_STYLE_CLASS;
		}
		if (!StringUtils.hasText(menuLayout)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no menuLayout set, using default [" 
					+ DEFAULT_MENU_LAYOUT + "]");
			}
			menuLayout = DEFAULT_MENU_LAYOUT;
		}
		if (!StringUtils.hasText(menuStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no menuStyleClass set, using default [" 
					+ DEFAULT_MENU_STYLE_CLASS + "]");
			}
			menuStyleClass = DEFAULT_MENU_STYLE_CLASS;
		}
		if (!StringUtils.hasText(menuItemStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no menuItemStyleClass set, using default [" 
					+ DEFAULT_MENU_ITEM_STYLE_CLASS + "]");
			}
			menuItemStyleClass = DEFAULT_MENU_ITEM_STYLE_CLASS;
		}
		if (!StringUtils.hasText(menuActiveItemStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no menuActiveItemStyleClass set, using default [" 
					+ DEFAULT_MENU_ACTIVE_ITEM_STYLE_CLASS + "]");
			}
			menuActiveItemStyleClass = DEFAULT_MENU_ACTIVE_ITEM_STYLE_CLASS;
		}
		if (!StringUtils.hasText(footerText)) {
			footerText = getDefaultFooterText();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no footerText set, using default [" 
					+ footerText + "]");
			}
		}
		if (!StringUtils.hasText(footerStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no footerStyleClass set, using default [" 
					+ DEFAULT_FOOTER_STYLE_CLASS + "]");
			}
			footerStyleClass = DEFAULT_FOOTER_STYLE_CLASS;
		}
		if (!StringUtils.hasText(footerItemStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no footerItemStyleClass set, using default [" 
					+ DEFAULT_FOOTER_ITEM_STYLE_CLASS + "]");
			}
			footerItemStyleClass = DEFAULT_FOOTER_ITEM_STYLE_CLASS;
		}
		if (!StringUtils.hasText(paginatorStyleClass)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no paginatorStyleClass set, using default [" 
					+ DEFAULT_PAGINATOR_STYLE_CLASS + "]");
			}
			paginatorStyleClass = DEFAULT_PAGINATOR_STYLE_CLASS;
		}
		if (!StringUtils.hasText(portletMediaPath)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no portletMediaPath set, using default [" 
					+ DEFAULT_PORTLET_MEDIA_PATH + "]");
			}
			portletMediaPath = DEFAULT_PORTLET_MEDIA_PATH;
		}
		if (!StringUtils.hasText(servletMediaPath)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(getClass() + ": no servletMediaPath set, using default [" 
					+ DEFAULT_SERVLET_MEDIA_PATH + "]");
			}
			servletMediaPath = DEFAULT_SERVLET_MEDIA_PATH;
		}
	}

	/**
	 * @return a TagsConfigurator instance.
	 */
	public static TagsConfigurator getInstance() {
		if (singleton == null) {
			singleton = (TagsConfigurator) BeanUtils.getBean(TAGS_CONFIGURATOR_BEAN);
		}
		return singleton;
	}
	
	/**
	 * @return the class for info messages.
	 */
	public String getMessagesInfoClass() {
		return messagesInfoClass; 
	}

	/**
	 * @return the class for warn messages.
	 */
	public String getMessagesWarnClass() {
		return messagesWarnClass; 
	}

	/**
	 * @return the class for error messages.
	 */
	public String getMessagesErrorClass() {
		return messagesErrorClass; 
	}

	/**
	 * @return the class for fatal messages.
	 */
	public String getMessagesFatalClass() {
		return messagesFatalClass; 
	}

	/**
	 * @return the messagesLayout
	 */
	public String getMessagesLayout() {
		return messagesLayout;
	}

	/**
	 * @param messagesErrorClass the messagesErrorClass to set
	 */
	public void setMessagesErrorClass(final String messagesErrorClass) {
		this.messagesErrorClass = messagesErrorClass;
	}

	/**
	 * @param messagesFatalClass the messagesFatalClass to set
	 */
	public void setMessagesFatalClass(final String messagesFatalClass) {
		this.messagesFatalClass = messagesFatalClass;
	}

	/**
	 * @param messagesInfoClass the messagesInfoClass to set
	 */
	public void setMessagesInfoClass(final String messagesInfoClass) {
		this.messagesInfoClass = messagesInfoClass;
	}

	/**
	 * @param messagesWarnClass the messagesWarnClass to set
	 */
	public void setMessagesWarnClass(final String messagesWarnClass) {
		this.messagesWarnClass = messagesWarnClass;
	}

	/**
	 * @param messagesLayout the messagesLayout to set
	 */
	public void setMessagesLayout(final String messagesLayout) {
		this.messagesLayout = messagesLayout;
	}

	/**
	 * @return the doctype
	 */
	public String getDoctype() {
		return doctype;
	}

	/**
	 * @param doctype the doctype to set
	 */
	public void setDoctype(final String doctype) {
		this.doctype = doctype;
	}

	/**
	 * @return the documentTitle
	 */
	public String getDocumentTitle() {
		return documentTitle;
	}

	/**
	 * @param documentTitle the documentTitle to set
	 */
	public void setDocumentTitle(final String documentTitle) {
		this.documentTitle = documentTitle;
	}

	/**
	 * @return the scripts
	 */
	public List<String> getScripts() {
		return scripts;
	}

	/**
	 * @param scripts the scripts to set
	 */
	public void setScripts(final List<String> scripts) {
		this.scripts = scripts;
	}

	/**
	 * @return the stylesheets
	 */
	public List<String> getStylesheets() {
		return stylesheets;
	}

	/**
	 * @param stylesheets the stylesheets to set
	 */
	public void setStylesheets(final List<String> stylesheets) {
		this.stylesheets = stylesheets;
	}

	/**
	 * @return the buttonStyleClass
	 */
	public String getButtonStyleClass() {
		return buttonStyleClass;
	}

	/**
	 * @param buttonStyleClass the buttonStyleClass to set
	 */
	public void setButtonStyleClass(final String buttonStyleClass) {
		this.buttonStyleClass = buttonStyleClass;
	}


	/**
	 * @return the inputFieldStyleClass
	 */
	public String getInputFieldStyleClass() {
		return inputFieldStyleClass;
	}

	/**
	 * @param inputFieldStyleClass the inputFieldStyleClass to set
	 */
	public void setInputFieldStyleClass(final String inputFieldStyleClass) {
		this.inputFieldStyleClass = inputFieldStyleClass;
	}

	/**
	 * @return the labelStyleClass
	 */
	public String getLabelStyleClass() {
		return labelStyleClass;
	}

	/**
	 * @param labelStyleClass the labelStyleClass to set
	 */
	public void setLabelStyleClass(final String labelStyleClass) {
		this.labelStyleClass = labelStyleClass;
	}

	/**
	 * @return the dataTableColumnClass
	 */
	public String getDataTableColumnClass() {
		return dataTableColumnClass;
	}

	/**
	 * @param dataTableColumnClass the dataTableColumnClass to set
	 */
	public void setDataTableColumnClass(final String dataTableColumnClass) {
		this.dataTableColumnClass = dataTableColumnClass;
	}

	/**
	 * @return the dataTableFooterClass
	 */
	public String getDataTableFooterClass() {
		return dataTableFooterClass;
	}

	/**
	 * @param dataTableFooterClass the dataTableFooterClass to set
	 */
	public void setDataTableFooterClass(final String dataTableFooterClass) {
		this.dataTableFooterClass = dataTableFooterClass;
	}

	/**
	 * @return the dataTableHeaderClass
	 */
	public String getDataTableHeaderClass() {
		return dataTableHeaderClass;
	}

	/**
	 * @param dataTableHeaderClass the dataTableHeaderClass to set
	 */
	public void setDataTableHeaderClass(final String dataTableHeaderClass) {
		this.dataTableHeaderClass = dataTableHeaderClass;
	}

	/**
	 * @return the dataTableRowClass
	 */
	public String getDataTableRowClass() {
		return dataTableRowClass;
	}

	/**
	 * @param dataTableRowClass the dataTableRowClass to set
	 */
	public void setDataTableRowClass(final String dataTableRowClass) {
		this.dataTableRowClass = dataTableRowClass;
	}

	/**
	 * @return the checkboxStyleClass
	 */
	public String getCheckboxStyleClass() {
		return checkboxStyleClass;
	}

	/**
	 * @param checkboxStyleClass the checkboxStyleClass to set
	 */
	public void setCheckboxStyleClass(final String checkboxStyleClass) {
		this.checkboxStyleClass = checkboxStyleClass;
	}

	/**
	 * @return the subSectionStyleClass
	 */
	public String getSubSectionStyleClass() {
		return subSectionStyleClass;
	}

	/**
	 * @param subSectionStyleClass the subSectionStyleClass to set
	 */
	public void setSubSectionStyleClass(final String subSectionStyleClass) {
		this.subSectionStyleClass = subSectionStyleClass;
	}

	/**
	 * @return the subSectionTag
	 */
	public String getSubSectionTag() {
		return subSectionTag;
	}

	/**
	 * @param subSectionTag the subSectionTag to set
	 */
	public void setSubSectionTag(final String subSectionTag) {
		this.subSectionTag = subSectionTag;
	}

	/**
	 * @return the sectionStyleClass
	 */
	public String getSectionStyleClass() {
		return sectionStyleClass;
	}

	/**
	 * @param sectionStyleClass the sectionStyleClass to set
	 */
	public void setSectionStyleClass(final String sectionStyleClass) {
		this.sectionStyleClass = sectionStyleClass;
	}

	/**
	 * @return the sectionTag
	 */
	public String getSectionTag() {
		return sectionTag;
	}

	/**
	 * @param sectionTag the sectionTag to set
	 */
	public void setSectionTag(final String sectionTag) {
		this.sectionTag = sectionTag;
	}

	/**
	 * @return the documentBodyStyleClass
	 */
	public String getDocumentBodyStyleClass() {
		return documentBodyStyleClass;
	}

	/**
	 * @param documentBodyStyleClass the documentBodyStyleClass to set
	 */
	public void setDocumentBodyStyleClass(final String documentBodyStyleClass) {
		this.documentBodyStyleClass = documentBodyStyleClass;
	}

	/**
	 * @return the paragraphTag
	 */
	public String getParagraphTag() {
		return paragraphTag;
	}

	/**
	 * @param paragraphTag the paragraphTag to set
	 */
	public void setParagraphTag(final String paragraphTag) {
		this.paragraphTag = paragraphTag;
	}

	/**
	 * @return the paragraphStyleClass
	 */
	public String getParagraphStyleClass() {
		return paragraphStyleClass;
	}

	/**
	 * @param paragraphStyleClass the paragraphStyleClass to set
	 */
	public void setParagraphStyleClass(final String paragraphStyleClass) {
		this.paragraphStyleClass = paragraphStyleClass;
	}

	/**
	 * @return the textStyleClass
	 */
	public String getTextStyleClass() {
		return textStyleClass;
	}

	/**
	 * @param textStyleClass the textStyleClass to set
	 */
	public void setTextStyleClass(final String textStyleClass) {
		this.textStyleClass = textStyleClass;
	}

	/**
	 * @return the menuLayout
	 */
	public String getMenuLayout() {
		return menuLayout;
	}

	/**
	 * @param menuLayout the menuLayout to set
	 */
	public void setMenuLayout(final String menuLayout) {
		this.menuLayout = menuLayout;
	}

	/**
	 * @return the menuStyleClass
	 */
	public String getMenuStyleClass() {
		return menuStyleClass;
	}

	/**
	 * @param menuStyleClass the menuStyleClass to set
	 */
	public void setMenuStyleClass(final String menuStyleClass) {
		this.menuStyleClass = menuStyleClass;
	}

	/**
	 * @return the menuItemStyleClass
	 */
	public String getMenuItemStyleClass() {
		return menuItemStyleClass;
	}

	/**
	 * @param menuItemStyleClass the menuItemStyleClass to set
	 */
	public void setMenuItemStyleClass(final String menuItemStyleClass) {
		this.menuItemStyleClass = menuItemStyleClass;
	}

	/**
	 * @return the menuActiveItemStyleClass
	 */
	public String getMenuActiveItemStyleClass() {
		return menuActiveItemStyleClass;
	}

	/**
	 * @param menuActiveItemStyleClass the menuActiveItemStyleClass to set
	 */
	public void setMenuActiveItemStyleClass(final String menuActiveItemStyleClass) {
		this.menuActiveItemStyleClass = menuActiveItemStyleClass;
	}

	/**
	 * @return the footerText
	 */
	public String getFooterText() {
		return footerText;
	}

	/**
	 * @param footerText the footerText to set
	 */
	public void setFooterText(final String footerText) {
		this.footerText = footerText;
	}

	/**
	 * @return the footerItemStyleClass
	 */
	public String getFooterItemStyleClass() {
		return footerItemStyleClass;
	}

	/**
	 * @param footerItemStyleClass the footerItemStyleClass to set
	 */
	public void setFooterItemStyleClass(final String footerItemStyleClass) {
		this.footerItemStyleClass = footerItemStyleClass;
	}

	/**
	 * @return the footerStyleClass
	 */
	public String getFooterStyleClass() {
		return footerStyleClass;
	}

	/**
	 * @param footerStyleClass the footerStyleClass to set
	 */
	public void setFooterStyleClass(final String footerStyleClass) {
		this.footerStyleClass = footerStyleClass;
	}

	/**
	 * @param locale
	 * @return the strings that correspond to a locale
	 */
	@Override
	public Map<String, String> getStrings(final Locale locale) {
		return super.getStrings(locale);
	}

	/**
	 * @return the portletMediaPath
	 */
	protected String getPortletMediaPath() {
		return portletMediaPath;
	}

	/**
	 * @param portletMediaPath the portletMediaPath to set
	 */
	public void setPortletMediaPath(final String portletMediaPath) {
		this.portletMediaPath = portletMediaPath;
	}

	/**
	 * @return the servletMediaPath
	 */
	protected String getServletMediaPath() {
		return servletMediaPath;
	}

	/**
	 * @param servletMediaPath the servletMediaPath to set
	 */
	public void setServletMediaPath(final String servletMediaPath) {
		this.servletMediaPath = servletMediaPath;
	}

	/**
	 * @return the servletMediaPath
	 */
	public String getMediaPath() {
	    FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean isPortlet = PortletUtil.isPortletRequest(facesContext);
        if (isPortlet) {
        	return portletMediaPath;
        }
		return servletMediaPath;
	}

	/**
	 * @return the dataTableRowAlternateClass
	 */
	public String getDataTableRowAlternateClass() {
		return dataTableRowAlternateClass;
	}

	/**
	 * @param dataTableRowAlternateClass the dataTableRowAlternateClass to set
	 */
	public void setDataTableRowAlternateClass(final String dataTableRowAlternateClass) {
		this.dataTableRowAlternateClass = dataTableRowAlternateClass;
	}

	/**
	 * @return the paginatorStyleClass
	 */
	public String getPaginatorStyleClass() {
		return paginatorStyleClass;
	}

	/**
	 * @param paginatorStyleClass the paginatorStyleClass to set
	 */
	public void setPaginatorStyleClass(final String paginatorStyleClass) {
		this.paginatorStyleClass = paginatorStyleClass;
	}

	/**
	 * @return the documentTitleI18nKey
	 */
	public String getDocumentTitleI18nKey() {
		return documentTitleI18nKey;
	}

	/**
	 * @param documentTitleI18nKey the documentTitleI18nKey to set
	 */
	public void setDocumentTitleI18nKey(final String documentTitleI18nKey) {
		this.documentTitleI18nKey = org.esupportail.commons.utils.strings.StringUtils.nullIfEmpty(
				documentTitleI18nKey);
	}

}
