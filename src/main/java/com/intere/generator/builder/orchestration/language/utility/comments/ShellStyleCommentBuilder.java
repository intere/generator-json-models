package com.intere.generator.builder.orchestration.language.utility.comments;

import org.springframework.stereotype.Service;

@Service("ShellStyle")
public class ShellStyleCommentBuilder extends CommonCommentBuilder {
	@Override
	public String singleLineComment(String comment, int tabCount) {
		return tabs(tabCount) + "# " + comment;
	}
	
	@Override
	public String multiLineComment(String comment, int tabCount) {
		comment = comment.trim();
		String nlReplacement = "\n" + tabs(tabCount) + "# ";
		return tabs(tabCount) + "#\n" + tabs(tabCount) + "# " + comment.replaceAll("\\\n", nlReplacement) + "\n" + tabs(tabCount) + "#";
	}
}
