package com.intere.generator.metadata;

/**
 * ModelClassImports.java
 * 
 * Generated by JSON Model Generator v0.0.4 on Thu Apr 09 07:15:11 MDT 2015.
 * https://github.com/intere/generator-json-models
 * 
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
*/

import java.io.Serializable;

@SuppressWarnings("serial")
public class ModelClassImports implements Serializable {
	private String importName;
	private String targetClassName;

	/**
	 * Setter for importName property
	*/
	public void setImportName(String importName) {
		this.importName = importName;
	}

	/**
	 * Getter for importName property
	*/
	public String getImportName() {
		return this.importName;
	}

	/**
	 * Setter for targetClassName property
	*/
	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	/**
	 * Getter for targetClassName property
	*/
	public String getTargetClassName() {
		return this.targetClassName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((importName == null) ? 0 : importName.hashCode());
		result = prime * result + ((targetClassName == null) ? 0 : targetClassName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelClassImports other = (ModelClassImports)obj;
		if(importName == null) {
			if(other.importName != null)
				return false;
		} else if(!importName.equals(other.importName))
			return false;
		if(targetClassName == null) {
			if(other.targetClassName != null)
				return false;
		} else if(!targetClassName.equals(other.targetClassName))
			return false;
		return true;
	}

}	// end ModelClassImports

