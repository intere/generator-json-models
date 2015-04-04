package com.intere.generator.builder.orchestration.language.utility.objc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseRestClientBuilder;
import com.intere.generator.metadata.ModelClass;

@Service("ObjectiveCRestClientBuilder")
public class ObjectiveCRestClientBuilder extends BaseRestClientBuilder {
	
	@Autowired @Qualifier("CStyle")
	protected CommentBuilder commentBuilder;
	
	@Autowired @Qualifier("ObjectiveCInterpreter")
	protected JsonLanguageInterpreter interpreter;
	
	@Override
	public CommentBuilder getCommentBuilder() {
		return commentBuilder;
	}

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return interpreter;
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import \"" + modelClass.getClassName() + ".h\"\n");
		
		builder.append("\n");
		return builder.toString();
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@interface " + modelClass.getRestClientClassName() + " : NSObject\n");
		// TODO
		return builder.toString();
	}

	@Override
	public String buildUtilityDeclarationMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(multiLineComment("Gets you all of the " + modelClass.getClassName() + " objects from the service using default options") + "\n");
		builder.append("+(NSArray *)getDefault" + modelClass.getClassName() + "Array;\n\n");
		
		builder.append(multiLineComment("Gets you the " + modelClass.getClassName() + " objects that match the parameters you provide") + "\n");
		builder.append("+(NSArray *)get" + modelClass.getClassName() + "WithOptions:(NSDictionary *)options;\n\n");
		
		builder.append(multiLineComment("Gets you a specific " + modelClass.getClassName() + " object by its id") + "\n");
		builder.append("+(" + modelClass.getClassName() + " *)get" + modelClass.getClassName() + "ById:(NSString *)objectId;\n\n");
		return builder.toString();
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@end" + singleLineComment("End of " + modelClass.getRestClientClassName() + " Class", 3) + "\n");
		builder.append("\n");
		return builder.toString();
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getRestClientClassName() + ".m");
	}
	
	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getRestClientClassName() + ".h");
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import \"" + modelClass.getRestClientClassName() + ".h\"\n");
		builder.append("#import \"Serializer.h\"\n");
		builder.append("#import \"RestUtils.h\"\n");
		builder.append("\n");
		builder.append("#define REST_ENDPOINT @\"" + modelClass.getRestUrl() + "\"\n\n");
		builder.append("@implementation " + modelClass.getRestClientClassName() + "\n");
		return builder.toString();
	}

	@Override
	public String buildUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("+(NSArray *)getDefault" + modelClass.getClassName() + "Array {\n");
		builder.append(tabs(1) + "NSString *url = [RestUtils buildUrlWithBase:[self getBaseUrl] andContext:[self getUrlContext] andPath:REST_ENDPOINT];\n");
		builder.append(tabs(1) + "return [self get" + modelClass.getClassName() + "ArrayFromUrl:url];\n");
		builder.append("}\n\n");
		
		builder.append("+(NSArray *)get" + modelClass.getClassName() + "WithOptions:(NSDictionary *)options {\n");
		builder.append(tabs(1) + "NSString *url = [RestUtils buildUrlWithBase:[self getBaseUrl] andContext:[self getUrlContext] andPath:REST_ENDPOINT andOptions:options];\n");
		builder.append(tabs(1) + "return [self get" + modelClass.getClassName() + "ArrayFromUrl:url];\n");
		builder.append("}\n\n");
		
		builder.append("+(" + modelClass.getClassName() + " *)get" + modelClass.getClassName() + "ById:(NSString *)objectId {\n");
		builder.append(tabs(1) + "NSString *url = [RestUtils buildUrlWithBase:[self getBaseUrl] andContext:[self getUrlContext] andPath:REST_ENDPOINT andObjectId:objectId];\n");
		builder.append(tabs(1) + "return [self get" + modelClass.getClassName() + "FromUrl:url];\n");
		builder.append("}\n\n");
		
		builder.append("#pragma mark Private Methods\n\n");
		
		builder.append("+(" + modelClass.getClassName() + " *)get" + modelClass.getClassName() + "FromUrl:(NSString *)url {\n");
		builder.append(tabs(1) + "NSError *error = nil;\n");
		builder.append(tabs(1) + "NSData *data = [RestUtils httpGet:url andError:error];\n");
		builder.append(tabs(1) + "if(error) {\n");
		builder.append(tabs(2) + "NSLog(@\"Error getting " + modelClass.getClassName() + " from %@: %@\", url, error.localizedDescription);\n");
		builder.append(tabs(1) + "} else {\n");
		builder.append(tabs(2) + "NSDictionary *dict = [Serializer fromJsonData:data];\n");
		builder.append(tabs(2) + "return [" + modelClass.getClassName() + " fromDictionary:dict];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return nil;\n");
		builder.append("}\n\n");
		
		builder.append("+(NSArray *)get" + modelClass.getClassName() + "ArrayFromUrl:(NSString *)url {\n");
		builder.append(tabs(1) + "NSError *error = nil;\n");
		builder.append(tabs(1) + "NSData *data = [RestUtils httpGet:url andError:error];\n");
		builder.append(tabs(1) + "if(error) {\n");
		builder.append(tabs(2) + "NSLog(@\"Error getting " + modelClass.getClassName() + " Data from %@: %@\", url, error.localizedDescription);\n");
		builder.append(tabs(1) + "} else {\n");
		builder.append(tabs(2) + "NSArray *arrayOfDictionaries = [Serializer arrayFromJsonData:data andError:error];\n");
		builder.append(tabs(2) + "if(error) {\n");
		builder.append(tabs(3) + "NSLog(@\"Error converting %@ to Array of " + modelClass.getClassName() + ": %@\", url, error.localizedDescription);\n");
		builder.append(tabs(2) + "} else {\n");
		builder.append(tabs(3) + "return [" + modelClass.getClassName() + " fromArrayOfDictionaries:arrayOfDictionaries];\n");
		builder.append(tabs(2) + "}\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return nil;\n");
		builder.append("}\n\n");
		
		builder.append("+(NSString *)getBaseUrl {\n");
		builder.append(tabs(1) + "NSString *baseUrl = [[NSUserDefaults standardUserDefaults] stringForKey:@\"" + modelClass.getClassName() + "BaseUrl\"];\n");
		builder.append(tabs(1) + "if(!baseUrl) {\n");
		builder.append(tabs(2) + "baseUrl = [[NSUserDefaults standardUserDefaults] stringForKey:@\"DefaultBaseUrl\"];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return baseUrl;\n");
		builder.append("}\n\n");
		
		builder.append("+(NSString *)getUrlContext {\n");
		builder.append(tabs(1) + "NSString *urlContext = [[NSUserDefaults standardUserDefaults] stringForKey:@\"" + modelClass.getClassName() + "UrlContext\"];\n");
		builder.append(tabs(1) + "if(!urlContext) {\n");
		builder.append(tabs(2) + "urlContext = [[NSUserDefaults standardUserDefaults] stringForKey:@\"DefaultUrlContext\"];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return urlContext;\n");
		builder.append("}\n\n");
		
		
		
		return builder.toString();
	}

}
