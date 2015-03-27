package com.intere.generator.builder.orchestration.language.utility.comments;

import java.util.Date;

import com.intere.generator.App;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;

public abstract class CommonCommentBuilder implements LanguageUtility.CommentBuilder {
	@Override
	public String tabs(int tabCount) {
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<tabCount; i++) {
			builder.append("\t");
		}
		return builder.toString();
	}
	
	@Override
	public String singleLineComment(String comment) {
		return singleLineComment(comment, 0);
	}
	
	@Override
	public String multiLineComment(String comment) {
		return multiLineComment(comment, 0);
	}
	
	@Override
	public String buildFileComment(String filename) {
		return multiLineComment("\n" +
				filename + "\n" +
				"\n" +
				"Generated by JSON Model Generator v" + App.getVersion() + " on " + getDate() + ".\n" +
				"https://github.com/intere/generator-json-models\n" +
				"\n" +
				"The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content\n" +
				"\n") + "\n\n";
	}
	
	public static String getDate() {
		return new Date().toString();
	}
}