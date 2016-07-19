package org.fckfaces.util;

import java.io.Serializable;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.webapp.UIComponentTag;

public class Tags {
   public static void setString(UIComponent component, String attributeName,
         String attributeValue) {
      if (attributeValue == null)
         return;
      if (UIComponentTag.isValueReference(attributeValue))
         setValueBinding(component, attributeName, attributeValue);
      else
         component.getAttributes().put(attributeName, attributeValue);
   }

   public static void setInteger(UIComponent component, 
         String attributeName, String attributeValue) {
      if (attributeValue == null) return;
      if (UIComponentTag.isValueReference(attributeValue))
         setValueBinding(component, attributeName, attributeValue);
      else 
         component.getAttributes().put(attributeName, 
               new Integer(attributeValue));
   }

   public static void setBoolean(UIComponent component, 
         String attributeName, String attributeValue) {
      if (attributeValue == null) return;
      if (UIComponentTag.isValueReference(attributeValue))
         setValueBinding(component, attributeName, attributeValue);
      else 
         component.getAttributes().put(attributeName,Boolean.valueOf(attributeValue));
   }
   
   public static void setValueBinding(UIComponent component, String attributeName,
         String attributeValue) {
      FacesContext context = FacesContext.getCurrentInstance();
      Application app = context.getApplication();
      ValueBinding vb = app.createValueBinding(attributeValue);
      component.setValueBinding(attributeName, vb);
   }

   public static void setActionListener(UIComponent component, String attributeValue) {
      setMethodBinding(component, "actionListener", attributeValue,
            new Class[] { ActionEvent.class });      
   }

   public static void setValueChangeListener(UIComponent component, 
         String attributeValue) {
      setMethodBinding(component, "valueChangeListener", attributeValue,
            new Class[] { ValueChangeEvent.class });      
   }

   public static void setValidator(UIComponent component, 
         String attributeValue) {
      setMethodBinding(component, "validator", attributeValue,
            new Class[] { FacesContext.class, UIComponent.class, Object.class });      
   }

   public static void setAction(UIComponent component, String attributeValue) {
      if (attributeValue == null) return;
      if (UIComponentTag.isValueReference(attributeValue))
      {
         setMethodBinding(component, "action", attributeValue,
               new Class[] {});
      }
      else
      {
         MethodBinding mb = new ActionMethodBinding(attributeValue);
         component.getAttributes().put("action", mb);         
      }
   }
      
   public static void setMethodBinding(UIComponent component, String attributeName,
         String attributeValue, Class[] paramTypes) {
      if (attributeValue == null)
         return;
      if (UIComponentTag.isValueReference(attributeValue)) {
         FacesContext context = FacesContext.getCurrentInstance();
         Application app = context.getApplication();
         MethodBinding mb = app.createMethodBinding(attributeValue, paramTypes);
         component.getAttributes().put(attributeName, mb);
      }
   }     
   
   private static class ActionMethodBinding 
         extends MethodBinding 
         implements Serializable 
   {      
	   /**
	    * 
	    */
	   private static final long serialVersionUID = 6027374557161316454L;
	   private String result;
   
      public ActionMethodBinding(String result) { this.result = result; }      
      public Object invoke(FacesContext context, Object params[]) {
         return result;
      }
      public String getExpressionString() { return result; }
      public Class getType(FacesContext context) { return String.class; }
   }
}
