//
//  MetadataClassesPropertyMap
//
//  Generated by JSON Model Generator v0.0.3 on Wed Feb 25 07:37:58 MST 2015.
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

package com.intere.generator.metadata;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;


@SuppressWarnings("serial")
public class MetadataClassesPropertyMap implements Serializable {
	private String property;
	private String mapClassName;
	private String mapClassProperty;

	/**
	 * Setter for property property.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Getter for property property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Setter for mapClassName property.
	 */
	public void setMapClassName(String mapClassName) {
		this.mapClassName = mapClassName;
	}

	/**
	 * Getter for mapClassName property.
	 */
	public String getMapClassName() {
		return mapClassName;
	}

	/**
	 * Setter for mapClassProperty property.
	 */
	public void setMapClassProperty(String mapClassProperty) {
		this.mapClassProperty = mapClassProperty;
	}

	/**
	 * Getter for mapClassProperty property.
	 */
	public String getMapClassProperty() {
		return mapClassProperty;
	}

}
