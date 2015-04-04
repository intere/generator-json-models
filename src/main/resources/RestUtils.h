//
//  RestUtils.h
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

#import <Foundation/Foundation.h>

@interface RestUtils : NSObject
/**
 * This method will build a URL with the provided (required) baseUrl, (optional) context and (required) path.
 */
+(NSString *)buildUrlWithBase:(NSString *)baseUrl andContext:(NSString *)context andPath:(NSString *)path;

/**
 * This method will build a URL with the provided (required) baseUrl, (optional) context, (required) path and (required) objectId;
 */
+(NSString *)buildUrlWithBase:(NSString *)baseUrl andContext:(NSString *)context andPath:(NSString *)path andObjectId:(NSString *)objectId;

/**
 * This method will build a URL with the provided (required) baseUrl, (optional) context, (required) path and Options Dictionary.
 */
+(NSString *)buildUrlWithBase:(NSString *)baseUrl andContext:(NSString *)context andPath:(NSString *)path andOptions:(NSDictionary *)options;

/**
 * Synchronous HTTP GET of the provided URL.
 */
+(NSData *)httpGet:(NSString *)url andError:(NSError *)error;

/**
 * Given the provided options dictionary, this method will return you a HTTP URL Parameter String
 */
+(NSString *)buildUrlParamsFromDictionary:(NSDictionary *)options;
@end
