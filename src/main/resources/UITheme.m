//
//  UITheme.m
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//

#import "UITheme.h"

@implementation UITheme
+(UIColor *)getBackgroundColor {
    return [UIColor lightGrayColor];
}

+(void)configurePropertyLabel:(UILabel *)propertyLabel {
    [propertyLabel setFont:[self getTitleFont]];
    //    [propertyLabel setTextAlignment:NSTextAlignmentCenter];
    [propertyLabel setTextColor:[UIColor greenColor]];
}

+(void)configureDatePicker:(UIDatePicker *)datePicker {
    [datePicker.layer setBorderWidth:2.0f];
    [datePicker.layer setBorderColor:[UIColor greenColor].CGColor];
}

+(void)configureTextView:(UITextView *)textView {
    
}

+(void)configureTextField:(UITextField *)textField {
    [textField setBackgroundColor:[UIColor whiteColor]];
    [textField setTextColor:[UIColor blackColor]];
}

+(void)configureButton:(UIButton *)button {
    [button.layer setBorderWidth:1.0f];
    [button.layer setBorderColor:[UIColor greenColor].CGColor];
    
    [button setUserInteractionEnabled:YES];
}

+(void)configureSwitch:(UISwitch *)toggleSwitch {
#warning NOT YET IMPLEMENTED
}

+(void)configureImageView:(UIImageView *)imageView {
#warning NOT YET IMPLEMENTED
}

+(UIButton *)createUIButtonWithFrame:(CGRect)frame andText:(NSString *)title {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button setTitle:title forState:UIControlStateNormal];
    [button sizeToFit];
    frame.size = CGSizeMake(button.frame.size.width + 10.0, button.frame.size.height);
    [button setFrame:frame];
    
    return button;
}

#pragma mark Private Methods
+(UIFont *)getTitleFont {
    UIFont *font = [UIFont fontWithName:@"Verdana" size:24.0];
    return font;
}
@end
