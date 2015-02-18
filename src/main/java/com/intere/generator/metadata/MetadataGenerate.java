//
//  MetadataGenerate
//
//  Generated by JSON Model Generator v0.0.3 on Wed Feb 18 08:00:01 MST 2015.
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
public class MetadataGenerate implements Serializable {
	private Boolean models;
	private Boolean tests;
	private Boolean views;
	private Boolean services;
	private Boolean restServices;

	/**
	 * Setter for models property.
	 */
	public void setModels(Boolean models) {
		this.models = models;
	}

	/**
	 * Getter for models property.
	 */
	public Boolean getModels() {
		return models;
	}

	/**
	 * Setter for tests property.
	 */
	public void setTests(Boolean tests) {
		this.tests = tests;
	}

	/**
	 * Getter for tests property.
	 */
	public Boolean getTests() {
		return tests;
	}

	/**
	 * Setter for views property.
	 */
	public void setViews(Boolean views) {
		this.views = views;
	}

	/**
	 * Getter for views property.
	 */
	public Boolean getViews() {
		return views;
	}

	/**
	 * Setter for services property.
	 */
	public void setServices(Boolean services) {
		this.services = services;
	}

	/**
	 * Getter for services property.
	 */
	public Boolean getServices() {
		return services;
	}

	/**
	 * Setter for restServices property.
	 */
	public void setRestServices(Boolean restServices) {
		this.restServices = restServices;
	}

	/**
	 * Getter for restServices property.
	 */
	public Boolean getRestServices() {
		return restServices;
	}

}
