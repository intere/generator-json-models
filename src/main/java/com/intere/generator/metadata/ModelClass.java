package com.intere.generator.metadata;

/**
 * ModelClass.java
 * 
 * Generated by JSON Model Generator v0.0.3 on Fri Feb 27 20:09:38 MST 2015.
 * https://github.com/intere/generator-json-models
 * 
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
*/

import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ModelClass implements Serializable {
	private String uniqueId;
	private String className;
	private String fileName;
	private String testClassName;
	private Boolean readonly;
	private String namespace;
	private Boolean hasSubClasses;
	private List<ModelClassProperty> property = new ArrayList<ModelClassProperty>();
	private String restUrl;
	private List<ModelClassImports> imports = new ArrayList<ModelClassImports>();

	/**
	 * Setter for uniqueId property
	*/
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Getter for uniqueId property
	*/
	public String getUniqueId() {
		return this.uniqueId;
	}

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
	 * Setter for fileName property
	*/
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Getter for fileName property
	*/
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * Setter for testClassName property
	*/
	public void setTestClassName(String testClassName) {
		this.testClassName = testClassName;
	}

	/**
	 * Getter for testClassName property
	*/
	public String getTestClassName() {
		return this.testClassName;
	}

	/**
	 * Setter for readonly property
	*/
	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * Getter for readonly property
	*/
	public Boolean getReadonly() {
		return this.readonly;
	}

	/**
	 * Setter for namespace property
	*/
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Getter for namespace property
	*/
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Setter for hasSubClasses property
	*/
	public void setHasSubClasses(Boolean hasSubClasses) {
		this.hasSubClasses = hasSubClasses;
	}

	/**
	 * Getter for hasSubClasses property
	*/
	public Boolean getHasSubClasses() {
		return this.hasSubClasses;
	}

	/**
	 * Setter for property property
	*/
	public void setProperty(List<ModelClassProperty> property) {
		this.property = property;
	}

	/**
	 * Getter for property property
	*/
	public List<ModelClassProperty> getProperty() {
		return this.property;
	}

	/**
	 * Setter for restUrl property
	*/
	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}

	/**
	 * Getter for restUrl property
	*/
	public String getRestUrl() {
		return this.restUrl;
	}

	/**
	 * Setter for imports property
	*/
	public void setImports(List<ModelClassImports> imports) {
		this.imports = imports;
	}

	/**
	 * Getter for imports property
	*/
	public List<ModelClassImports> getImports() {
		return this.imports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((testClassName == null) ? 0 : testClassName.hashCode());
		result = prime * result + ((readonly == null) ? 0 : readonly.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((hasSubClasses == null) ? 0 : hasSubClasses.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((restUrl == null) ? 0 : restUrl.hashCode());
		result = prime * result + ((imports == null) ? 0 : imports.hashCode());
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
		ModelClass other = (ModelClass)obj;
		if(uniqueId == null) {
			if(other.uniqueId != null)
				return false;
		} else if(!uniqueId.equals(other.uniqueId))
			return false;
		if(className == null) {
			if(other.className != null)
				return false;
		} else if(!className.equals(other.className))
			return false;
		if(fileName == null) {
			if(other.fileName != null)
				return false;
		} else if(!fileName.equals(other.fileName))
			return false;
		if(testClassName == null) {
			if(other.testClassName != null)
				return false;
		} else if(!testClassName.equals(other.testClassName))
			return false;
		if(readonly == null) {
			if(other.readonly != null)
				return false;
		} else if(!readonly.equals(other.readonly))
			return false;
		if(namespace == null) {
			if(other.namespace != null)
				return false;
		} else if(!namespace.equals(other.namespace))
			return false;
		if(hasSubClasses == null) {
			if(other.hasSubClasses != null)
				return false;
		} else if(!hasSubClasses.equals(other.hasSubClasses))
			return false;
		if(property == null) {
			if(other.property != null)
				return false;
		} else if(!property.equals(other.property))
			return false;
		if(restUrl == null) {
			if(other.restUrl != null)
				return false;
		} else if(!restUrl.equals(other.restUrl))
			return false;
		if(imports == null) {
			if(other.imports != null)
				return false;
		} else if(!imports.equals(other.imports))
			return false;
		return true;
	}

}	// end ModelClass

