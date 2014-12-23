package com.intere.generator.deserializer;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.JsonNode;

/**
 * Some (testable) utility methods that help out with the generation of {@link JsonDeserializer}.
 * @author einternicola
 */
public class JsonNodeUtils {
	private static final DateFormat ZULU_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//	private static final DateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	private static final DateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	/**
	 * Is the provided node an Array of Objects?
	 * @param node The node to check.
	 * @return True if the node is in fact an array of objects.
	 */
	public static boolean isArrayOfObjects(JsonNode node) {
		if(node.isArray()) {
			for (Iterator<JsonNode> iterator = node.getElements(); iterator.hasNext();) {
				JsonNode childNode = iterator.next();
				if(childNode.isObject()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isArrayofArrays(JsonNode node) {
		if(node.isArray()) {
			for (Iterator<JsonNode> iterator = node.getElements(); iterator.hasNext();) {
				JsonNode childNode = iterator.next();
				if(childNode.isArray()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isArray(JsonNode node) {
		return null != node && node.isArray();
	}
	
	public static boolean isObject(JsonNode node) {
		return null != node && node.isObject();
	}
	
	public static boolean isBoolean(JsonNode node) {
		return null != node && node.isBoolean();
	}
	
	public static boolean isFloat(JsonNode node) {
		return null != node && node.isFloatingPointNumber();
	}
	
	public static boolean isLong(JsonNode node) {
		return null != node && node.isLong();
	}
	
	/**
	 * Is the provided node an Integer value?
	 * @param node The {@link JsonNode}.
	 * @return true/false
	 */
	public static boolean isInteger(JsonNode node) {
		return null != node && node.isInt();
	}
	
	/**
	 * Is the provided node a Text Node?
	 * @param node The {@link JsonNode}.
	 * @return true/false
	 */
	public static boolean isText(JsonNode node) {
		return null != node && node.isTextual();
	}
	
	/**
	 * Is the provided node a date?
	 * @param node The {@link JsonNode}.
	 * @return true/false
	 */
	public static boolean isDate(JsonNode node) {
		if(isText(node)) {
			try {
				ZULU_DATE.parse(node.getTextValue());
				return true;
			} catch(ParseException ex) {
				// Not a ZULU Date
			}
			try {
				ISO_DATE.parse(node.getTextValue());
				return true;
			} catch (ParseException ex) {
				// Not an ISO Date
			}
		} else if(isLong(node)) {
			try {
				Date found = new Date(node.getLongValue());
				long y2k = ISO_DATE.parse("2014-01-01T23:28:56.782-0700").getTime();
				return found.getTime() > y2k;
			} catch (ParseException ex) {
				ex.printStackTrace();
				// Not a Long Date (greater than 2000)
			}			
		}
		return false;
	}
	
	public static String getFilenameWithoutPathWithoutExtension(String filename) {
		File f = new File(filename);
		return FilenameUtils.removeExtension(f.getName());
	}
	
	public static String getExtensionFromFilename(String filename) {
		File f = new File(filename);
		return FilenameUtils.getExtension(f.getName());
	}
}
