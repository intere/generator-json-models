package com.intere.generator;

/**
 * These Banners were created via: 
 * <ul>
 *   <li>http://patorjk.com/software/taag/#p=display&f=Roman&t=JSON%20Generator</li>
 *   <li>http://patorjk.com/software/taag/#p=display&f=Roman&t=Java</li>
 *   <li>http://patorjk.com/software/taag/#p=display&f=Roman&t=Ruby</li>
 *   <li>http://patorjk.com/software/taag/#p=display&f=Roman&t=Objective-C</li>
 * </ul>
 *
 */
public class Banners {
	public static String appBanner() {
		return  "\n" + 
				"============================================================================================================================================================\n" + 
				"   oooo  .oooooo..o   .oooooo.   ooooo      ooo        .oooooo.                                                           .                      \n" + 
				"   `888 d8P'    `Y8  d8P'  `Y8b  `888b.     `8'       d8P'  `Y8b                                                        .o8                      \n" + 
				"    888 Y88bo.      888      888  8 `88b.    8       888            .ooooo.  ooo. .oo.    .ooooo.  oooo d8b  .oooo.   .o888oo  .ooooo.  oooo d8b \n" + 
				"    888  `\"Y8888o.  888      888  8   `88b.  8       888           d88' `88b `888P\"Y88b  d88' `88b `888\"\"8P `P  )88b    888   d88' `88b `888\"\"8P \n" + 
				"    888      `\"Y88b 888      888  8     `88b.8       888     ooooo 888ooo888  888   888  888ooo888  888      .oP\"888    888   888   888  888     \n" + 
				"    888 oo     .d8P `88b    d88'  8       `888       `88.    .88'  888    .o  888   888  888    .o  888     d8(  888    888 . 888   888  888     \n" + 
				".o. 88P 8\"\"88888P'   `Y8bood8P'  o8o        `8        `Y8bood8P'   `Y8bod8P' o888o o888o `Y8bod8P' d888b    `Y888\"\"8o   \"888\" `Y8bod8P' d888b    \n" + 
				"`Y888P\n" + 
				"============================================================================================================================================================\n" + 
				"\n\n" +
				"VERSION: " + App.getVersion() + "\n\n";
		
		
	}
	
	public static String javaBanner() {
		return  "   oooo                                 \n" + 
				"   `888                                 \n" + 
				"    888  .oooo.   oooo    ooo  .oooo.   \n" + 
				"    888 `P  )88b   `88.  .8'  `P  )88b  \n" + 
				"    888  .oP\"888    `88..8'    .oP\"888  \n" + 
				"    888 d8(  888     `888'    d8(  888  \n" + 
				".o. 88P `Y888\"\"8o     `8'     `Y888\"\"8o \n" + 
				"`Y888P                                  \n" + 
				"                                        \n";
	}
	
	public static String rubyBanner() {
		return  "ooooooooo.                .o8                   \n" + 
				"`888   `Y88.             \"888                   \n" + 
				" 888   .d88' oooo  oooo   888oooo.  oooo    ooo \n" + 
				" 888ooo88P'  `888  `888   d88' `88b  `88.  .8'  \n" + 
				" 888`88b.     888   888   888   888   `88..8'   \n" + 
				" 888  `88b.   888   888   888   888    `888'    \n" + 
				"o888o  o888o  `V88V\"V8P'  `Y8bod8P'     .8'     \n" + 
				"                                    .o..P'      \n" + 
				"                                    `Y8P'       \n";
	}
	
	public static String objectiveCBanner() {
		return  "  .oooooo.    .o8           o8o                         .    o8o                                  .oooooo.   \n" + 
				" d8P'  `Y8b  \"888           `\"'                       .o8    `\"'                                 d8P'  `Y8b  \n" + 
				"888      888  888oooo.     oooo  .ooooo.   .ooooo.  .o888oo oooo  oooo    ooo  .ooooo.          888          \n" + 
				"888      888  d88' `88b    `888 d88' `88b d88' `\"Y8   888   `888   `88.  .8'  d88' `88b         888          \n" + 
				"888      888  888   888     888 888ooo888 888         888    888    `88..8'   888ooo888 8888888 888          \n" + 
				"`88b    d88'  888   888     888 888    .o 888   .o8   888 .  888     `888'    888    .o         `88b    ooo  \n" + 
				" `Y8bood8P'   `Y8bod8P'     888 `Y8bod8P' `Y8bod8P'   \"888\" o888o     `8'     `Y8bod8P'          `Y8bood8P'  \n" + 
				"                            888                                                                              \n" + 
				"                        .o. 88P                                                                              \n" + 
				"                        `Y888P                                                                               ";
	}
}
