package com.intere.generator.builder.orchestration.language.utility.objc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.ListViewControllerBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

@Service("ObjectiveCListViewControllerGenerator")
public class ObjectiveCListViewControllerGenerator implements ListViewControllerBuilder {
	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCListViewControllerGenerator.class);
	
	@Autowired @Qualifier("CStyle")
	protected CommentBuilder commentBuilder;
	@Autowired @Qualifier("ObjectiveCInterpreter")
	protected JsonLanguageInterpreter interpreter;

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		return commentBuilder.buildFileComment(modelClass.getListControllerName() + ".h");
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import <UIKit/UIKit.h>\n");
		builder.append("#import \"" + modelClass.getSingleControllerName() + ".h\"\n\n");
		return builder.toString();
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@interface " + modelClass.getListControllerName() + " : UITableViewController<UITableViewDataSource, UITableViewDelegate>\n");
		builder.append(commentBuilder.multiLineComment("Sets the array of " + modelClass.getClassName()));
		builder.append("-(void)set" + modelClass.getClassName() + "Array:(NSArray *)arrayOf" + modelClass.getClassName() + ";\n");		
		return builder.toString();
	}

	@Override
	public String buildUtilityDeclarationMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		return builder.toString();
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		return "@end" + commentBuilder.singleLineComment("End of " + modelClass.getListControllerName() + " Class", 3) + "\n\n"; 
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return commentBuilder.buildFileComment(modelClass.getListControllerName() + ".m");
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import \"" + modelClass.getListControllerName() + ".h\"\n");
		builder.append("#import \"" + modelClass.getServiceClassName() + ".h\"\n\n");
		
		builder.append("@interface " + modelClass.getListControllerName() + "() {\n");
		builder.append(tabs(1) + "NSMutableArray *array;\n");
		builder.append("}\n\n");
		builder.append("@end\n\n");
		
		builder.append("#define CELL_ID @\"" + modelClass.getClassName() + "CellReuseIdentifier" + "\"\n\n");
		
		builder.append("@implementation " + modelClass.getListControllerName() + "\n");
		builder.append("- (void)viewDidLoad {\n");
		builder.append(tabs(1) + "[super viewDidLoad];\n");
		builder.append(tabs(1) + "[self set" + modelClass.getClassName() + "Array:[" + modelClass.getServiceClassName() + " getSharedInstance].all" + modelClass.getClassName() + "];\n");
		builder.append(tabs(1) + "self.tableView.delegate = self;\n");
		builder.append(tabs(1) + "self.tableView.dataSource = self;\n");
		builder.append("}\n\n");
		
		builder.append("- (void)set" + modelClass.getClassName() + "Array:(NSArray *)arrayOf" + modelClass.getClassName() + "{\n");
		builder.append(tabs(1) + "self->array = [[NSMutableArray alloc]initWithArray:arrayOf" + modelClass.getClassName() + "];\n");
		builder.append("}\n\n");
		
		builder.append("#pragma mark - Table view data source\n\n");
		builder.append("- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {\n");
		builder.append(tabs(1) + "return 1;\n");
		builder.append("}\n\n");
		
		builder.append("- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {\n");
		builder.append(tabs(1) + "if(self->array) {\n");
		builder.append(tabs(2) + "return self->array.count;\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return 0;\n");
		builder.append("}\n\n");
		
		builder.append("- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {\n");
		builder.append(tabs(1) + "UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CELL_ID forIndexPath:indexPath];\n");
		builder.append(tabs(1) + "if(!cell) {\n");
		builder.append(tabs(2) + "cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CELL_ID];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + modelClass.getClassName() + " *model = [self->array objectAtIndex:indexPath.row];\n");
		builder.append(tabs(1) + "NSString *text = " + buildSummaryText(modelClass) + ";\n");
		builder.append(tabs(1) + "[cell.textLabel setText:text];\n");
		builder.append(tabs(1) + "return cell;\n");
		builder.append("}\n\n");
		
		builder.append("#pragma mark - Table View Delegate\n\n");
		builder.append("- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {\n");
		builder.append(tabs(1) + "NSLog(@\"Selected row: %li\", (long)indexPath.row);\n");
		builder.append(tabs(1) + modelClass.getClassName() + " *selected = [self->array objectAtIndex:indexPath.row];\n");
		builder.append(tabs(1) + modelClass.getSingleControllerName() + " *vc = [[" + modelClass.getSingleControllerName() + " alloc]init];\n");
		builder.append(tabs(1) + "[[" + modelClass.getServiceClassName() + " getSharedInstance].named" + modelClass.getClassName() 
				+ "Objects setObject:selected forKey:@\"selected" + modelClass.getClassName() + "\"];\n");
		builder.append(tabs(1) + "[self.navigationController pushViewController:vc animated:YES];\n");
		builder.append("}\n\n");
		
		return builder.toString();
	}

	@Override
	public String buildUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		
		return builder.toString();
	}
	
	private String buildSummaryText(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("[NSString stringWithFormat:@\"");
		for(int i=0; i<modelClass.getSummaryProperties().size();i++) {
			if(i != 0) {
				builder.append(" ");
			}
			builder.append("%@");
		}
		builder.append("\"");
		
		for(String prop : modelClass.getSummaryProperties()) {
			builder.append(", ");
			ModelClassProperty property = getProperty(modelClass, prop);
			if(null == property) {
				LOGGER.warn("No Summary Property found for class " + modelClass.getClassName() + " with name: " + prop);
				builder.append("@\"Unknown\"");
			} else {
				builder.append("model." + property.getAlias());
			}
		}
		
		builder.append("]");
				
		return builder.toString();
	}


	private ModelClassProperty getProperty(ModelClass modelClass, String prop) {
		for(ModelClassProperty property : modelClass.getProperty()) {
			if(property.getName().equals(prop)) {
				return property;
			}
		}
		return null;
	}

	private String tabs(int tabCount) {
		return commentBuilder.tabs(tabCount);
	}
}
