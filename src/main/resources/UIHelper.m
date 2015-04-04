//
//  UIHelper.m
//
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

#import "UIHelper.h"

@implementation UIHelper
+(void)loadImageInBackground:(UIImageView *)imageView fromUrl:(NSString *)imageUrl {
    if(!imageUrl || [imageUrl isEqualToString:@""] || [imageUrl isEqual:[NSNull null]]) {
        return;
    }
    [self performSelectorInBackground:@selector(doImageLoad:) withObject:[NSArray arrayWithObjects:imageView, imageUrl, nil]];
}

#pragma mark Private Methods
+(void)doImageLoad:(NSArray *)objects {
    UIImageView *imageView = [objects objectAtIndex:0];
    NSString *imageUrl = [objects objectAtIndex:1];

    NSData *imageData = [self loadImage:imageUrl];
    UIImage *image = [UIImage imageWithData:imageData];
    [imageView performSelectorOnMainThread:@selector(setImage:) withObject:image waitUntilDone:NO];
}

+(NSData *)loadImage:(NSString *)url {
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
    NSURLResponse *response;
    NSError *error;

    NSData *data = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];

    if(error) {
        NSLog(@"Error getting image url: %@, message=%@", url, error.localizedDescription);
        return nil;
    }

    return data;
}
@end
