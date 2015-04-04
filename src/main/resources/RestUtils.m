//
//  RestUtils.m
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

#import "RestUtils.h"

@implementation RestUtils
+(NSString *)buildUrlWithBase:(NSString *)baseUrl andContext:(NSString *)context andPath:(NSString *)path {
    if(baseUrl) {
        if(context) {
            return [NSString stringWithFormat:@"%@/%@%@", baseUrl, context, path];
        } else {
            return [NSString stringWithFormat:@"%@%@", baseUrl, path];
        }
    } else {
        NSLog(@"No Base URL Defined for class ContestRestClient or Default Base URL");
    }
    return nil;
}

+(NSString *)buildUrlWithBase:(NSString *)baseUrl andContext:(NSString *)context andPath:(NSString *)path andObjectId:(NSString *)objectId {

    if(baseUrl && objectId) {
        if(context) {
            return [NSString stringWithFormat:@"%@/%@%@/%@", baseUrl, context, path, objectId];
        } else {
            return [NSString stringWithFormat:@"%@%@/%@", baseUrl, path, objectId];
        }
    } else {
        NSLog(@"No Base URL Defined for class ContestRestClient or Default Base URL");
    }
    return nil;
}

+(NSString *)buildUrlWithBase:(NSString *)baseUrl andContext:(NSString *)context andPath:(NSString *)path andOptions:(NSDictionary *)options {
    NSString *url = [self buildUrlWithBase:baseUrl andContext:context andPath:path];
    NSString *params = [self buildUrlParamsFromDictionary:options];
    NSString *fullUrl = [NSString stringWithFormat:@"%@%@", url, params];

    return fullUrl;
}

+(NSData *)httpGet:(NSString *)url andError:(NSError *)error {
    url = [url stringByReplacingOccurrencesOfString:@" " withString:@"%20"];
    NSURLRequest *request = [[NSURLRequest alloc]initWithURL:[NSURL URLWithString:url]];
    NSHTTPURLResponse *response;
    NSData *data = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];

    if(error) {
        NSLog(@"Error hitting url: %@", url);
        NSLog(@"Error description: %@", error.localizedDescription);
    } else if(response.statusCode == 200) {
        NSLog(@"Received a 200 Response from URL: %@", url);
        return data;
    } else if(!error) {
        NSString *message = [NSString stringWithFormat:@"Response Code: %li, and message: %@", (long)response.statusCode, [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding]];
        error = [NSError errorWithDomain:message code:-2 userInfo:nil];
        NSLog(@"Received a non-200 Response from URL: %@", url);
        NSLog(@"Error Message: %@", message);
    }

    return nil;
}

+(NSString *)buildUrlParamsFromDictionary:(NSDictionary *)options {
    NSString *params = @"";
    if(options && [options allKeys].count>0) {
        for (NSString *key in [options allKeys]) {
            if([params isEqualToString:@""]) {
                params = [NSString stringWithFormat:@"?%@=%@", key, [options objectForKey:key]];
            } else {
                params = [NSString stringWithFormat:@"%@&%@=%@", params, key, [options objectForKey:key]];
            }
        }
    }

    return params;
}

@end
