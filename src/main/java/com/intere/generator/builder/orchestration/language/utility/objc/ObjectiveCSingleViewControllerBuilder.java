package com.intere.generator.builder.orchestration.language.utility.objc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.SingleViewControllerBuilder;
import com.intere.generator.metadata.ModelClass;

@Service("ObjectiveCSingleViewControllerBuilder")
public class ObjectiveCSingleViewControllerBuilder implements SingleViewControllerBuilder {
	@Autowired @Qualifier("CStyle")
	protected CommentBuilder commentBuilder;
	@Autowired @Qualifier("ObjectiveCInterpreter")
	protected JsonLanguageInterpreter interpreter;

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		return commentBuilder.buildFileComment(modelClass.getSingleControllerName() + ".h");
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import <UIKit/UIKit.h>\n");
		builder.append("#import \"" + modelClass.getViewClassName() + ".h\"\n\n");		
		return builder.toString();
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@interface " + modelClass.getSingleControllerName() + " : UIViewController\n");
		builder.append("@property (strong, nonatomic) UIScrollView *scrollView;\n");
		return builder.toString();
	}

	@Override
	public String buildUtilityDeclarationMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		// TODO Auto-generated method stub
		return builder.toString();
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@end" + commentBuilder.singleLineComment("End of " + modelClass.getSingleControllerName() + " Class", 3));
		return builder.toString();
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return commentBuilder.buildFileComment(modelClass.getSingleControllerName() + ".m");
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		String viewClass = modelClass.getViewClassName();
		String viewProperty = interpreter.cleanVariableName(viewClass);
		StringBuilder builder = new StringBuilder();
		builder.append("#import \"" + modelClass.getSingleControllerName() + ".h\"\n");
		builder.append("#import \"UITheme.h\"\n\n");
		builder.append("@interface " + modelClass.getSingleControllerName() + "() {\n");
		builder.append(tabs(1) + viewClass + " *" + viewProperty + ";\n");
		builder.append("}\n");
		builder.append("@end\n\n");
		
		builder.append("@implementation " + modelClass.getSingleControllerName() + "\n");
		builder.append("- (void)viewDidLoad {\n");
		builder.append(tabs(1) + "[super viewDidLoad];\n");
		builder.append(tabs(1) + "CGRect scrollFrame = CGRectMake(0, 20.0, self.view.frame.size.width, self.view.frame.size.height-20.0);\n");
		builder.append(tabs(1) + "self.scrollView = [[UIScrollView alloc]initWithFrame:scrollFrame];\n");
		builder.append(tabs(1) + "[self.view addSubview:self.scrollView];\n");
		builder.append(tabs(1) + viewProperty + " = [[" + viewClass +" alloc]initWithFrame:self.view.frame];\n");
		builder.append(tabs(1) + "[self.scrollView addSubview:" + viewProperty + "];\n");
		builder.append(tabs(1) + "[self.scrollView setContentSize:" + viewProperty + ".frame.size];\n");
		builder.append(tabs(1) + "[self.view setBackgroundColor:[UITheme getBackgroundColor]];\n");
		builder.append("}\n\n");		
		return builder.toString();
	}

	@Override
	public String buildUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		// TODO Auto-generated method stub
		return builder.toString();
	}
	
	private String tabs(int tabCount) {
		return commentBuilder.tabs(tabCount);
	}
}
