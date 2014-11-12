package com.intere.generator.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;

public class JsonDeserializer {

	private String json;
	private JsonNode node;
	private String name;
	private String filename;
	private String testFilename;
	private String namespace;
	private NavigableMap<String, List<JsonDeserializer>> subClasses;
	private JsonLanguageInterpreter interpreter;
	private boolean rootLevel;
	
	/**
	 * Constructor that is used to hierarchically build the class structures for the JSON model.
	 * @param subClasses A map to keep track of the hierarchical Class Tree Structure (passed throughout all child instances).
	 * @param name This classes name.
	 * @param json The Json String to be parsed for this part of the class tree.
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public JsonDeserializer(NavigableMap<String, List<JsonDeserializer>> subClasses, JsonLanguageInterpreter interpreter, 
			String namespace, String name, String json) throws JsonParseException, IOException {
		this.subClasses = subClasses;
		this.interpreter = interpreter;
		this.namespace = namespace;
		this.name = name;
		this.json = json;
		this.filename = interpreter.buildFilenameFromClassname(name);
		this.testFilename = interpreter.buildTestfilenameFromClassname(name);
		parseJson();
		buildObjectNodeTree();
	}
	
	/**
	 * This is the "User Constructor" if you will.  It is the root class for the JSON
	 * @param interpreter The {@link JsonLanguageInterpreter} that we want to use to translate names.
	 * @param name The root class name.
	 * @param json The JSON for the root class.
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public JsonDeserializer(JsonLanguageInterpreter interpreter, String namespace, String name, String json) throws JsonParseException, IOException {
		this(new TreeMap<String, List<JsonDeserializer>>(), interpreter, namespace, name, json);
		this.rootLevel = true;
	}

	/**
	 * Parses the JSON to create the {@link JsonNode} that this class instance works with.
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private void parseJson() throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use mapper.getFactory() instead
		JsonParser jp = factory.createJsonParser(json);
		node = mapper.readTree(jp);
		if(node.isArray()) {
			node = node.get(0);
		}
	}
	
	/**
	 * This is what builds out the {@link JsonDeserializer} tree structure, recursively.
	 * @throws JsonParseException
	 * @throws IOException
	 */
	private void buildObjectNodeTree() throws JsonParseException, IOException {
		Iterator<String> iter = node.getFieldNames();
		
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode childNode = node.get(name);
			if(childNode.isObject()) {
				addChildClass(new JsonDeserializer(subClasses, interpreter, namespace, interpreter.buildSubClassName(getName(), name), childNode.toString()));
			} else if (JsonNodeUtils.isArrayOfObjects(childNode)) {
				addChildClass(new JsonDeserializer(subClasses, interpreter, namespace, interpreter.buildSubClassName(getName(), name), childNode.iterator().next().toString()));
			}
		}
	}
	
	private void addChildClass(JsonDeserializer child) {
		if(!subClasses.containsKey(name)) {
			subClasses.put(name, new ArrayList<JsonDeserializer>());
		}
		subClasses.get(name).add(child);
	}
	
	public String getTestFilename() {
		return testFilename;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getName() {
		return name;
	}
	
	public String getJson() {
		return json;
	}
	
	public JsonNode getNode() {
		return node;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public boolean isRootLevel() {
		return rootLevel;
	}
	
	public NavigableMap<String, List<JsonDeserializer>> getSubClasses() {
		return subClasses;
	}
}
