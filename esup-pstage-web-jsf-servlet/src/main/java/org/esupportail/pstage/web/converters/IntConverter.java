/**
 * ESUP-Portail Commons - Copyright (c) 2006-2009 ESUP-Portail consortium.
 */
package org.esupportail.pstage.web.converters;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.esupportail.pstage.utils.Utils;
import org.springframework.util.StringUtils;

/**
 * @author Matthieu Manginot : matthieu.manginot@univ-nancy2.fr
 *
 */
public class IntConverter implements Serializable, Converter {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Bean constructor.
	 */
	public IntConverter() {
		super();
	}
	/**
	 * @see javax.faces.convert.Converter#getAsString(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(
			final FacesContext context, 
			final UIComponent component, 
			final Object value) {
		if (value == null || !StringUtils.hasText(value.toString())) {
			return "";
		}
		if (!Utils.isNumber(value+"")) {
			throw new UnsupportedOperationException(
					"object " + value + " is not a Integer.");
		}
		return ""+value;
	}
	
	/**
	 * @see javax.faces.convert.Converter#getAsObject(
	 * javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(
			final FacesContext context, 
			final UIComponent component, 
			final String value) {
		if (!StringUtils.hasText(value) 
				|| !Utils.isNumber(value)) {
			return null;
		}
		
		return Utils.convertStringToInt(value);
	}
}
