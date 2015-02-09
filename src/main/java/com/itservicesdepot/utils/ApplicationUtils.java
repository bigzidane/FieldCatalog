/**
 *  Copyright (c) 2015, ItServicesDepot.com All rights reserved.
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER. 
 *	
 *	Please contact ItServicesDepot.com or NghiaDo (dothanhtrongnghia@yahoo.com) 
 *	if you need	additional information or have any questions. 
 */

package com.itservicesdepot.utils;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.primefaces.component.editor.Editor;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.tabview.Tab;

import com.itservicesdepot.constant.ApplicationConstant;
import com.itservicesdepot.model.CustField;
import com.itservicesdepot.model.Event;
import com.itservicesdepot.model.ProductCustField;

public class ApplicationUtils {
	static Logger logger = Logger.getLogger(ApplicationUtils.class);

	public static String bool2YNIndicator(boolean value) {
		return value ? ApplicationConstant.YES_INDICATOR
				: ApplicationConstant.NO_INDICATOR;
	}

	public static boolean ny2booleanIndicator(String value) {
		if (ApplicationConstant.YES_INDICATOR.equalsIgnoreCase(value)) {
			return true;
		}
		else if (ApplicationConstant.YES_STRING_INDICATOR.equalsIgnoreCase(value)) {
			return true;
		}
		else if (ApplicationConstant.TRUE_INDICATOR.equalsIgnoreCase(value)) {
			return true;
		}

		return false;
	}

	public static List<UIComponentBase> dynamicBuildEventsComponent(String beanName, String mapName, List<Event> events, boolean hasInSufficientRoles) {
		List<UIComponentBase> list = new ArrayList<UIComponentBase>();

		for (Event event : events) {
			Tab tab = new Tab();
			String id = String.format("%s%s%s", event.getName(), ApplicationConstant.CUST_FIELD_SEPARATOR, event.getId());
            tab.setTitle(WordUtils.capitalize(event.getName()));
            
            // create PanelGrid
            PanelGrid pnlGrid = new PanelGrid();
            pnlGrid.setId("pnl" + event.getName());
            pnlGrid.setColumns(2);
            pnlGrid.setStyleClass("permissionPanel");
            pnlGrid.setStyle("width:100%");
            pnlGrid.setColumnClasses("eventLabel,eventValue");
            
            // create Business Rule label & editor
            OutputLabel labelBusinessRule = new OutputLabel();
            labelBusinessRule.setValue("Business Rule");
            labelBusinessRule.setStyleClass("vAlignTop textBold");

			InputTextarea inputBusinessRule = new InputTextarea();
			inputBusinessRule.setId("txt" + event.getName() + ApplicationConstant.BUSINESS_RULE_SUFFIX);
			inputBusinessRule.setWidgetVar("widget" + event.getName() + ApplicationConstant.BUSINESS_RULE_SUFFIX);
			inputBusinessRule.setRows(12);
			inputBusinessRule.setStyle("width:100%");
			inputBusinessRule.setReadonly(hasInSufficientRoles);
			
			String bindingBusinessRule = String.format("#{%s.%s['%s'].%s}", beanName, mapName, id, ApplicationConstant.BUSINESS_RULE_SUFFIX);
			inputBusinessRule.setValueExpression("value", ApplicationUtils.createValueExpression(bindingBusinessRule, String.class));
			
			pnlGrid.getChildren().add(labelBusinessRule);
			pnlGrid.getChildren().add(inputBusinessRule);
			
			// create Business Rule label & editor
            OutputLabel labelCodeRule = new OutputLabel();
            labelCodeRule.setValue("Code Rule");
            labelCodeRule.setStyleClass("vAlignTop textBold");

            InputTextarea inputCodeRule = new InputTextarea();
			inputCodeRule.setId("txt" + event.getName() + ApplicationConstant.CODE_RULE_SUFFIX);
			inputCodeRule.setWidgetVar("widget" + event.getName() + ApplicationConstant.CODE_RULE_SUFFIX);
			inputCodeRule.setRows(12);
			inputCodeRule.setStyle("width:100%");
			inputCodeRule.setReadonly(hasInSufficientRoles);
			
			String bindingCodeRule = String.format("#{%s.%s['%s'].%s}", beanName, mapName, id, ApplicationConstant.CODE_RULE_SUFFIX);
			inputCodeRule.setValueExpression("value", ApplicationUtils.createValueExpression(bindingCodeRule, String.class));
			
			pnlGrid.getChildren().add(labelCodeRule);
			pnlGrid.getChildren().add(inputCodeRule);
			
            tab.getChildren().add(pnlGrid);
            
            list.add(tab);
		}
		
		return list;
	}
	
	public static List<UIComponentBase> dynamicBuildCustFieldsComponent(String beanName, String mapName, List<CustField> custFields, boolean hasInSufficientRoles) {
		List<UIComponentBase> list = new ArrayList<UIComponentBase>();

		for (CustField custField : custFields) {
			String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
			String binding = String.format("#{%s.%s['%s']}", beanName, mapName, id);

			OutputLabel label = new OutputLabel();
			label.setValue(custField.getLabel());
			label.setFor(id);

			if (custField.getType().equalsIgnoreCase(ApplicationConstant.INPUT_TEXT)) {
				InputText inputText = new InputText();
				inputText.setId(id);
				inputText.setRequired(ApplicationUtils.ny2booleanIndicator(custField.getIsRequired()));
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				inputText.setPlaceholder(custField.getPreface());
				inputText.setWidgetVar(id);
				inputText.setReadonly(hasInSufficientRoles);
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.INPUT_TEXT_AREA)) {
				InputTextarea inputText = new InputTextarea();
				inputText.setId(id);
				inputText.setRequired(ApplicationUtils.ny2booleanIndicator(custField.getIsRequired()));
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				inputText.setPlaceholder(custField.getPreface());
				inputText.setWidgetVar(id);
				inputText.setReadonly(hasInSufficientRoles);
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.CHECK_BOX)) {
				SelectBooleanCheckbox inputText = new SelectBooleanCheckbox();
				inputText.setId(id);
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				inputText.setWidgetVar(id);
				inputText.setReadonly(hasInSufficientRoles);
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.RADIO)) {
				SelectOneRadio inputText = new SelectOneRadio();
				UISelectItems items = new UISelectItems();
				inputText.setId(id);
				inputText.setWidgetVar(id);
				inputText.setDisabled(hasInSufficientRoles);
				
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				String[] itemsDB = split(custField.getValue(), ApplicationConstant.ITEM_SEPARATOR);
				
				if (ValidateUtils.isObjectNotEmpty(itemsDB)) {
					for (String itemDB : itemsDB) {
						Map.Entry<String, String> pair = createPair(itemDB, ApplicationConstant.PAIR_SEPARATOR);
						if (ValidateUtils.isObjectEmpty(pair)) continue;
						
						SelectItem selectItem = new SelectItem(pair.getKey(), pair.getValue());
						selectItems.add(selectItem);
					}
				}
				

				items.setValue(selectItems);
				inputText.getChildren().add(items);
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.SELECT)) {
				SelectOneMenu inputText = new SelectOneMenu();
				UISelectItems items = new UISelectItems();
				inputText.setId(id);
				inputText.setWidgetVar(id);
				inputText.setDisabled(hasInSufficientRoles);
				
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				String[] itemsDB = split(custField.getValue(), ApplicationConstant.ITEM_SEPARATOR);
				
				if (ValidateUtils.isObjectNotEmpty(itemsDB)) {
					for (String itemDB : itemsDB) {
						Map.Entry<String, String> pair = createPair(itemDB, ApplicationConstant.PAIR_SEPARATOR);
						if (ValidateUtils.isObjectEmpty(pair)) continue;
						
						SelectItem selectItem = new SelectItem(pair.getKey(), pair.getValue());
						selectItems.add(selectItem);
					}
				}
				

				items.setValue(selectItems);
				inputText.getChildren().add(items);
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(
					ApplicationConstant.EDITOR)) {
				Editor inputText = new Editor();
				inputText.setId(id);
				inputText.setWidgetVar(id);
				inputText.setRequired(ApplicationUtils
						.ny2booleanIndicator(custField.getIsRequired()));
				inputText.setValueExpression("value", ApplicationUtils
						.createValueExpression(binding, String.class));
				inputText.setDisabled(hasInSufficientRoles);
				list.add(label);
				list.add(inputText);
			}
		}

		return list;
	}
	
	public static List<UIComponentBase> dynamicBuildComponentEdit(String beanName, String mapName, List<ProductCustField> productCustFields) {
		List<UIComponentBase> list = new ArrayList<UIComponentBase>();
		for (ProductCustField productCustField : productCustFields) {
			
			CustField custField = productCustField.getCustField();
			
			String id = String.format("%s%s%s", custField.getFieldId(), ApplicationConstant.CUST_FIELD_SEPARATOR, custField.getId());
			String binding = String.format("#{%s.%s['%s']}", beanName, mapName, id);

			OutputLabel label = new OutputLabel();
			label.setValue(custField.getLabel());
			label.setFor(id);

			if (custField.getType().equalsIgnoreCase(ApplicationConstant.INPUT_TEXT)) {
				InputText inputText = new InputText();
				inputText.setId(id);
				inputText.setRequired(ApplicationUtils.ny2booleanIndicator(custField.getIsRequired()));
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				inputText.setWidgetVar(id);
				inputText.setValue(productCustField.getValue());

				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.INPUT_TEXT_AREA)) {
				InputTextarea inputText = new InputTextarea();
				inputText.setId(id);
				inputText.setRequired(ApplicationUtils.ny2booleanIndicator(custField.getIsRequired()));
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				inputText.setWidgetVar(id);
				inputText.setValue(productCustField.getValue());
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.CHECK_BOX)) {
				SelectBooleanCheckbox inputText = new SelectBooleanCheckbox();
				inputText.setId(id);
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				inputText.setWidgetVar(id);
				inputText.setSelected(ApplicationUtils.ny2booleanIndicator(productCustField.getValue()));
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.RADIO)) {
				SelectOneRadio inputText = new SelectOneRadio();
				UISelectItems items = new UISelectItems();
				inputText.setId(id);
				inputText.setWidgetVar(id);
				
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				String[] itemsDB = split(custField.getValue(), ApplicationConstant.ITEM_SEPARATOR);
				
				if (ValidateUtils.isObjectNotEmpty(itemsDB)) {
					for (String itemDB : itemsDB) {
						Map.Entry<String, String> pair = createPair(itemDB, ApplicationConstant.PAIR_SEPARATOR);
						if (ValidateUtils.isObjectEmpty(pair)) continue;
						
						SelectItem selectItem = new SelectItem(pair.getKey(), pair.getValue());
						if (productCustField.getValue().equalsIgnoreCase(pair.getKey())) {
							System.out.println("this is a selected one");
						}
						selectItems.add(selectItem);
					}
				}
				

				items.setValue(selectItems);
				inputText.getChildren().add(items);
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(ApplicationConstant.SELECT)) {
				SelectOneMenu inputText = new SelectOneMenu();
				UISelectItems items = new UISelectItems();
				inputText.setId(id);
				inputText.setWidgetVar(id);
				
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				String[] itemsDB = split(custField.getValue(), ApplicationConstant.ITEM_SEPARATOR);
				
				if (ValidateUtils.isObjectNotEmpty(itemsDB)) {
					for (String itemDB : itemsDB) {
						Map.Entry<String, String> pair = createPair(itemDB, ApplicationConstant.PAIR_SEPARATOR);
						if (ValidateUtils.isObjectEmpty(pair)) continue;
						
						SelectItem selectItem = new SelectItem(pair.getKey(), pair.getValue());
						selectItems.add(selectItem);
					}
				}
				

				items.setValue(selectItems);
				inputText.getChildren().add(items);
				inputText.setValueExpression("value", ApplicationUtils.createValueExpression(binding, String.class));
				inputText.setTitle(custField.getTitle());
				
				list.add(label);
				list.add(inputText);
			} else if (custField.getType().equalsIgnoreCase(
					ApplicationConstant.EDITOR)) {
				Editor inputText = new Editor();
				inputText.setId(id);
				inputText.setWidgetVar(id);
				inputText.setRequired(ApplicationUtils
						.ny2booleanIndicator(custField.getIsRequired()));
				inputText.setValueExpression("value", ApplicationUtils
						.createValueExpression(binding, String.class));
				inputText.setValue(productCustField.getValue());
				list.add(label);
				list.add(inputText);
			}
		}

		return list;
	}

	public static String[] split(String input, String separator) {
		if (ValidateUtils.isObjectEmpty(input))
			return null;

		String[] items = input.split(separator);

		if (ValidateUtils.isObjectEmpty(items))
			return null;

		return items;
	}

	public static Map.Entry<String, String> createPair(String input,
			String separator) {

		String[] pair = split(input, separator);
		if (ValidateUtils.isObjectEmpty(pair)) return null;
		if (pair.length != 2) return null;
		
		return new AbstractMap.SimpleEntry<String, String>(pair[0], pair[1]);
	}
	
	public static String getApplicationURL() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url = req.getRequestURL().toString();
		return url = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath();
	}

	public static ValueExpression createValueExpression(String binding,
			Class<String> clazz) {
		final ValueExpression ve = getExpressionFactory()
				.createValueExpression(getELContext(), binding, String.class);
		return ve;
	}
	
	public static ELContext getELContext() {
		return FacesContext.getCurrentInstance().getELContext();
	}

	public static ExpressionFactory getExpressionFactory() {
		return getApplication().getExpressionFactory();
	}

	public static Application getApplication() {
		return FacesContext.getCurrentInstance().getApplication();
	}
	
	/**
	 * Compares two version strings.
	 * 
	 * Use this instead of String.compareTo() for a non-lexicographical
	 * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
	 * 
	 * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
	 * 
	 * @param str1
	 *            a string of ordinal numbers separated by decimal points.
	 * @param str2
	 *            a string of ordinal numbers separated by decimal points.
	 * @return The result is a negative integer if str1 is _numerically_ less than str2. 
	 * 		   The result is a positive integer if str1 is _numerically_ greater than str2. 
	 * 		    The result is zero if the strings are _numerically_ equal.
	 */
	public static Integer versionCompare(String str1, String str2) {
		String[] vals1 = str1.split("\\.");
		String[] vals2 = str2.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version
		// string
		while (i < vals1.length && i < vals2.length
				&& vals1[i].equals(vals2[i])) {
			i++;
		}
		// compare first non-equal ordinal number
		if (i < vals1.length && i < vals2.length) {
			int diff = Integer.valueOf(vals1[i]).compareTo(
					Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		else {
			return Integer.signum(vals1.length - vals2.length);
		}
	}


}