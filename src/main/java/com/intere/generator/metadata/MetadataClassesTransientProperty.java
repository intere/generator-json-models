package com.intere.generator.metadata;

/**
 * MetadataClassesTransientProperty.java
 * 
 * Generated by JSON Model Generator v0.0.3 on Fri Feb 27 20:09:38 MST 2015.
 * https://github.com/intere/generator-json-models
 * 
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
*/

import java.io.Serializable;

@SuppressWarnings("serial")
public class MetadataClassesTransientProperty implements Serializable {
	private String className;
	private String name;
	private String type;

	/**
	 * Setter for className property
	*/
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Getter for className property
	*/
	public String getClassName() {
		return this.className;
	}

	/**
	 * Setter for name property
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for name property
	*/
	public String getName() {
		return this.name;
	}

	/**
	 * Setter for type property
	*/
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter for type property
	*/
	public String getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MetadataClassesTransientProperty other = (MetadataClassesTransientProperty)obj;
		if(className == null) {
			if(other.className != null)
				return false;
		} else if(!className.equals(other.className))
			return false;
		if(name == null) {
			if(other.name != null)
				return false;
		} else if(!name.equals(other.name))
			return false;
		if(type == null) {
			if(other.type != null)
				return false;
		} else if(!type.equals(other.type))
			return false;
		return true;
	}

}	// end MetadataClassesTransientProperty

