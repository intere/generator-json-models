//
//  UITheme.h
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

#import <UIKit/UIKit.h>

@interface UITheme : NSObject
+(UIColor *)getBackgroundColor;
+(void)configurePropertyLabel:(UILabel *)propertyLabel;
+(void)configureDatePicker:(UIDatePicker *)datePicker;
+(void)configureTextView:(UITextView *)textView;
+(void)configureTextField:(UITextField *)textField;
+(void)configureButton:(UIButton *)button;
+(void)configureSwitch:(UISwitch *)toggleSwitch;
+(void)configureImageView:(UIImageView *)imageView;
+(UIButton *)createUIButtonWithFrame:(CGRect)frame andText:(NSString *)title;
@end
