package com.intere.generator.builder.generation.views;

import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isBoolean;
import static com.intere.generator.deserializer.JsonNodeUtils.isDate;
import static com.intere.generator.deserializer.JsonNodeUtils.isFloat;
import static com.intere.generator.deserializer.JsonNodeUtils.isInteger;
import static com.intere.generator.deserializer.JsonNodeUtils.isLong;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;
import static com.intere.generator.deserializer.JsonNodeUtils.isText;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.builder.generation.utils.CodeGenerationUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;

public class ObjectiveCViewGeneration extends ViewCodeGeneration {
	
	private JsonLanguageInterpreter INTERPRETER = new ObjectiveCModelInterpreter();

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return INTERPRETER;
	}
	
	@Override
	public String generateHeaderFile(JsonDeserializer deserializer) {
		String modelClassName = deserializer.getName();
		String viewClassName = INTERPRETER.buildViewFilenameFromClassname(modelClassName);
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(viewClassName + ".h"));
		builder.append("#import <UIKit/UIKit.h>\n");
		builder.append("#import \"" + modelClassName + ".h\"\n");
		builder.append("\n");

		builder.append("@interface " + viewClassName + " : UIView\n\n");
		
		builder.append(comment("Gets the " + modelClassName + " object that's set on this view") + "\n");
		builder.append("-(" + modelClassName + " *)get" + modelClassName + ";\n\n");
		

		builder.append(comment("Sets the " + modelClassName + " object for this view") + "\n");
		builder.append("-(void)set" + modelClassName + ":(" + modelClassName + " *)a" + modelClassName + ";\n\n");
	
		builder.append("@end\n\n");
		return builder.toString();
	}

	@Override
	public String generateImplementationFile(JsonDeserializer deserializer) {
		String modelClassName = deserializer.getName();
		String modelInstanceName = getInterpreter().cleanVariableName(modelClassName);
		String viewClassName = INTERPRETER.buildViewFilenameFromClassname(modelClassName);
		NavigableMap<String, List<JsonDeserializer>> subClasses = deserializer.getSubClasses();
		JsonNode node = deserializer.getNode();
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(viewClassName + ".m"));
		builder.append("#import \"" + viewClassName + ".h\"\n");
		builder.append("#import \"UITheme.h\"\n");
		builder.append("\n");
		
		// Interface Declaration
		builder.append("@interface " + viewClassName + "() {\n");
		builder.append("\t" + modelClassName + " *" + modelInstanceName + ";\n");
		if(hasDate(node)) {
			builder.append("\tNSDateFormatter *dateFormatter;\n");
		}
		// Now - go generate all of the properties:
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append("\t" + buildViewType(node.get(name), name) + " *" + getInterpreter().cleanVariableName(name) + ";\n");
		}
		builder.append("}\n");
		builder.append("-(void)buildView;\n");
		builder.append("-(void)updateValues;\n");
		builder.append("-(void)configureAndAddPropertyLabel:(UILabel *)propertyLabel;\n");
		builder.append("-(void)configureAndAddDatePicker:(UIDatePicker *)datePicker;\n");
		builder.append("-(void)configureAndAddTextView:(UITextView *)textView;\n");
		builder.append("-(void)configureAndAddTextField:(UITextField *)textField;\n");
		builder.append("-(void)configureAndAddButton:(UIButton *)button;\n");
		builder.append("-(void)configureAndAddSwitch:(UISwitch *)toggleSwitch;\n");
		builder.append("-(void)buttonPressed:(UIButton *)button;\n");
		builder.append("@end\n\n");
		
		builder.append("@implementation " + viewClassName + "\n");
		builder.append("-(id)initWithCoder:(NSCoder *)aDecoder {\n");
		builder.append("\tself = [super initWithCoder:aDecoder];\n");
		builder.append("\tif(self) {\n");
		builder.append("\t\t[self buildView];\n");
		builder.append("\t}\n");
		builder.append("\treturn self;\n");
		builder.append("}\n\n");
		
		builder.append("-(id)initWithFrame:(CGRect)frame {\n");
		builder.append("\tself = [super initWithFrame:frame];\n");
		builder.append("\tif(self) {\n");
		builder.append("\t\t[self buildView];\n");
		builder.append("\t}\n");
		builder.append("\treturn self;\n");
		builder.append("}\n\n");
		
		builder.append("-(" + modelClassName + " *)get" + modelClassName + " {\n");
		builder.append("\treturn self->" + getInterpreter().cleanVariableName(modelClassName) + ";\n");
		builder.append("}\n\n");
		
		builder.append("-(void)set" + modelClassName + ":(" + modelClassName + " *)a" + modelClassName + " {\n");
		builder.append("\tself->" + getInterpreter().cleanVariableName(modelClassName) + " = a" + modelClassName + ";\n");
		builder.append("\t[self updateValues];\n");
		builder.append("}\n\n");
		
		
		builder.append("#pragma mark Private Methods\n");
		builder.append("-(void)configureAndAddPropertyLabel:(UILabel *)propertyLabel {\n");
		builder.append("[UITheme configurePropertyLabel:propertyLabel];\n");
		builder.append("\t[self addSubview:propertyLabel];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)configureAndAddDatePicker:(UIDatePicker *)datePicker {\n");
		builder.append("[UITheme configureDatePicker:datePicker];\n");
		builder.append("\t[self addSubview:datePicker];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)configureAndAddTextView:(UITextView *)textView {\n");
		builder.append("[UITheme configureTextView:textView];\n");
		builder.append("\t[self addSubview:textView];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)configureAndAddTextField:(UITextField *)textField {\n");
		builder.append("[UITheme configureTextField:textField];\n");
		builder.append("\t[self addSubview:textField];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)configureAndAddButton:(UIButton *)button {\n");
		builder.append("[UITheme configureButton:button];\n");
		builder.append("\t[self addSubview:button];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)configureAndAddSwitch:(UISwitch *)toggleSwitch {\n");
		builder.append("[UITheme configureSwitch:toggleSwitch];\n");
		builder.append("\t[self addSubview:toggleSwitch];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)buttonPressed:(UIButton *)button {\n");
		builder.append("#warning NOT YET IMPLEMENTED\n");
		builder.append("}\n\n");
		
		builder.append("-(void)updateValues {\n");
		updateView(builder, node, modelInstanceName);
		builder.append("}\n\n");
		
		builder.append("-(void)buildView {\n");
		if(hasDate(node)) {
			builder.append("\tdateFormatter = [[NSDateFormatter alloc] init];\n");
			builder.append("\t[dateFormatter setDateStyle:NSDateFormatterMediumStyle];\n");
		}
		buildView(builder, node);
		builder.append("\tframe = CGRectMake(0.0, 0.0, self.frame.size.width, y + height + 40.0);\n"); 
		builder.append("\t[self setFrame:frame];\n");
		builder.append("}\n\n");
		
		builder.append("@end\n\n");
		return builder.toString();
	}
	
	private void updateView(StringBuilder builder, JsonNode node, String modelInstanceName) {
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode childNode = node.get(name);
			String propertyName = getInterpreter().cleanVariableName(name);
			if(isDate(childNode)) {
				builder.append("\t[self->" + propertyName + " setDate:" + modelInstanceName + "." + propertyName + "];\n");
			} else if(isText(childNode)) {
				builder.append("\t[self->" + propertyName + " setText:" + modelInstanceName + "." + propertyName + "];\n");
			} else if(isInteger(childNode) || isLong(childNode)) {
				builder.append("\t[self->" + propertyName + " setText:[NSString stringWithFormat:@\"%li\", (NSInteger)" + modelInstanceName + "." + propertyName + "]];\n");
			} else if(isBoolean(childNode)) {
				builder.append("\t[self->" + propertyName + " setSelected:" + modelInstanceName + "." + propertyName + "];\n");
			} else if(isFloat(childNode)) {
				builder.append("\t[self->" + propertyName + " setText:[NSString stringWithFormat:@\"%1.2f\", " + modelInstanceName + "." + propertyName + "]];\n");
			}
		}
	}

	private void buildView(StringBuilder builder, JsonNode node) {
		Iterator<String> iter = node.getFieldNames();
		boolean first = true;
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode childNode = node.get(name);
			String propertyName = getInterpreter().cleanVariableName(name);
			String controlType = buildViewType(childNode, name);
			int controlHeight = getHeightForControl(controlType);
			String humanReadableName = getInterpreter().humanReadableName(name);
			
			builder.append("\t// " + getInterpreter().humanReadableName(propertyName) + " info\n");
			if(first) {
				first = false;
				builder.append("\tdouble x=10.0, y=5.0, width=self.frame.size.width - 20.0, height = 30.0;\n");
				builder.append("\tCGRect frame = CGRectMake(x, y, width, height);\n");
				builder.append("\tUILabel *propertyLabel = [[UILabel alloc]initWithFrame:frame];\n");
			} else {
				builder.append("\tx=10.0, y += height + 20.0, height = 30.0;\n");
				builder.append("\tframe = CGRectMake(x, y, width, height);\n");
				builder.append("\tpropertyLabel = [[UILabel alloc]initWithFrame:frame];\n");
			}
			
			builder.append("\t[propertyLabel setText:@\"" + humanReadableName + "\"];\n");
			builder.append("\t[self configureAndAddPropertyLabel:propertyLabel];\n");
			int x = 10;
			if(controlType.equals("UIDatePicker")) {
				x = 0;
			}
			builder.append("\tx = " + x + ".0,y += height + 10.0, height = " + controlHeight + ".0;\n");
			builder.append("\tframe = CGRectMake(x, y, width, height);\n");
			builder.append("\t" + propertyName + " = [[" + controlType + " alloc]initWithFrame:frame];\n");
			if(controlType.equals("UIButton")) {
				builder.append("\t[" + propertyName + " setTitle:@\"Manage " + humanReadableName + "\" forState:UIControlStateNormal];\n");
				builder.append("\t[" + propertyName + " sizeToFit];\n");
				builder.append("\t[" + propertyName + " addTarget:self action:@selector(buttonPressed:) " + 
						"forControlEvents:UIControlEventTouchUpInside];\n");
			}
			builder.append("\t[self configureAndAdd" + controlType.replace("UI", "") + ":" + propertyName + "];\n\n");
		}
	}
	
	/**
	 * Based on the children of the provided node, is there a date object?
	 * @param node
	 * @return
	 */
	private boolean hasDate(JsonNode node) {
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode childNode = node.get(name);
			if(isDate(childNode)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Based on the children (and specifically a presence of nested objects or arrays), does this node have "events" associated with it?
	 * @param node The node to check.
	 * @return
	 */
	private boolean hasEvents(JsonNode node) {
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode childNode = node.get(name);
			if(isObject(childNode) || isArray(childNode)) {
				return true;
			}
		}
		
		return false;
	}
	
	private int getHeightForControl(String control) {
		switch(control) {
		case "UITextView":
			return 130;
		case "UITextField":
			return 30;
		case "UISwitch":
			return 32;
		case "UIDatePicker":
			return 200;
		case "UIButton":
			return 30;
		}
		
		return 20;
	}

	private String buildViewType(JsonNode node, String name) {
		if(isDate(node)) {
			return "UIDatePicker";
		} else if(isText(node)) {
			if(node.getTextValue().length()>25) {
				return "UITextView";
			} else {
				return "UITextField";
			}
		} else if(isFloat(node) || isInteger(node) || isLong(node)) {
			return "UITextField";
		} else if(isBoolean(node)) {
			return "UISwitch";
		} else if(isObject(node) || isArray(node)) {
			return "UIButton";
		} else {
			System.out.println(name + " is some other type of node...");
			return "// Unknown Type: ";
		}
	}

	@Override
	public String generateTestFile(JsonDeserializer deserializer,
			String jsonFilename, String testJsonFilename) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String generateHeaderCommentBlock(String filename) {
		return CodeGenerationUtils.generateCStyleHeaderBlock(filename);
	}
	
	protected String comment(String message) {
		return "/** " + message + " */";
	}
}
