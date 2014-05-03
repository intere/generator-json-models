//
//  Serializer.m
//  Piste Off
//
//  Licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//  Created by Eric Internicola on 12/8/13.
//
//

#import "Serializer.h"

@implementation Serializer

+(NSString *)safeGetDictString:(NSDictionary *)dict withKey:(NSString *)key {
    NSString *value = [dict objectForKey:key];
    if([[NSNull null] isEqual:value]) {
        value = nil;
    }

    return value;
}

#pragma mark Helper Methods
+(void)setDict:(NSMutableDictionary *)dict intValue:(NSInteger)value forKey:(NSString *)key {
    NSNumber *number = [NSNumber numberWithInteger:value];
    [Serializer setDict:dict object:number forKey:key];
}

+(void)setDict:(NSMutableDictionary *)dict doubleValue:(double)value forKey:(NSString *)key {

    NSNumber *number = [NSNumber numberWithDouble:value];
    [Serializer setDict:dict object:number forKey:key];

}

+(void)setDict:(NSMutableDictionary *)dict object:(NSObject *)object forKey:(NSString *)key {

    if(object) {
        [dict setObject:object forKey:key];
    } else {
        NSLog(@"skipping setting object for key: %@ (it was nil)", key);
    }

}

+(double)getDoubleFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(double)defaultValue {

    NSNumber *value = [dict objectForKey:key];

    if(value && ![[NSNull null] isEqual:value]) {
        return [value doubleValue];
    }

    return defaultValue;
}

+(NSInteger)getIntegerFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(NSInteger)defaultValue {

    NSNumber *value = [dict objectForKey:key];

    if(value && ![[NSNull null] isEqual:value]) {
        return [value integerValue];
    }

    return defaultValue;
}

#pragma mark Serialization Methods

+(NSString *)jsonStringFromDictionary:(NSDictionary *)dict {
    return [Serializer jsonStringFromDictionary:dict withPrettyPrint:NO];
}

+(NSString *)jsonStringFromDictionary:(NSDictionary *)dict withPrettyPrint:(BOOL)prettyPrint {

    NSError *error;
    NSJSONWritingOptions opts = (prettyPrint ? NSJSONWritingPrettyPrinted : 0);

    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:opts error:&error];


    if (! jsonData) {
        NSLog(@"Serializer jsonStringFromDictionary: error: %@", error.localizedDescription);
    } else {
        return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }

    return nil;
}

+(double)dateToStandardTimeInterval:(NSDate *)date {

    double nativeTimeInterval = [date timeIntervalSince1970];
    double standardTimeInterval = (nativeTimeInterval * 1000);

    return standardTimeInterval;
}

# pragma mark Deserialization Methods

+(NSArray *)arrayFromJsonData:(NSData *)data andError:(NSError *)error {

    if(data) {
        NSArray *array = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];

        return array;
    }

    return nil;
}


+(NSArray *)arrayFromJsonString:(NSString *)json andError:(NSError *)error {

    if(json) {
        NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];

        return [Serializer arrayFromJsonData:data andError:error];
    }

    return nil;
}

+(NSDictionary *)fromJsonData:(NSData *)data {

    if(data) {
        NSError *error;
        NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];

        if(error) {
            NSLog(@"Error deserializing JSON data into Dictionary: %@", error.localizedDescription);
        } else {
            return dict;
        }
    }

    return nil;
}

+(NSDictionary *)fromJsonString:(NSString *)json {

    NSData *data = [json dataUsingEncoding:NSUTF8StringEncoding];

    return [Serializer fromJsonData:data];
}


+(NSDate *)standardTimeIntervalToDate:(double)standardTime {

    double nativeTimeInterval = (standardTime / 1000);

    return [[NSDate alloc]initWithTimeIntervalSince1970:nativeTimeInterval];
}

@end
