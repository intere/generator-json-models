//
//  Serializer.m
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

#import "Serializer.h"

static NSDateFormatter *ISO_FORMATTER;
static NSDateFormatter *ZULU_FORMATTER;

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

+(void)setDict:(NSMutableDictionary *)dict boolValue:(BOOL)value forKey:(NSString *)key {
    NSNumber *number = [NSNumber numberWithBool:value];
    [Serializer setDict:dict object:number forKey:key];
}

+(void)setDict:(NSMutableDictionary *)dict dateValue:(NSDate *)date forKey:(NSString *)key {
    if(date) {
        NSString *isoString = [[self isoDateFormatterInstance] stringFromDate:date];
        if(isoString) {
            [dict setObject:isoString forKey:key];
        } else {
            NSLog(@"Failed to convert Date to ISO Date String: %@", date);
        }
    }
}

+(void)setDict:(NSMutableDictionary *)dict object:(NSObject *)object forKey:(NSString *)key {

    if(object) {
        [dict setObject:object forKey:key];
    } else {
//        NSLog(@"skipping setting object for key: %@ (it was nil)", key);
    }
}

+(double)getDoubleFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(double)defaultValue {

    NSNumber *value = [dict objectForKey:key];

    if(value && ![[NSNull null] isEqual:value]) {
        return [value doubleValue];
    }

    return defaultValue;
}

+(NSArray *)getArrayFromDict:(NSDictionary *)dict forKey:(NSString *)key {
  NSArray *array = [dict objectForKey:key];

  if(array && ![[NSNull null] isEqual:array]) {
    return array;
  }

  return [[NSArray alloc]init];
}

+(NSInteger)getIntegerFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(NSInteger)defaultValue {

    NSNumber *value = [dict objectForKey:key];

    if(value && ![[NSNull null] isEqual:value]) {
        return [value integerValue];
    }

    return defaultValue;
}

+(BOOL)getBoolFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(BOOL)defaultValue {
    NSNumber *value = [dict objectForKey:key];
    if(value && ![[NSNull null] isEqual:value]) {
        return [value boolValue];
    }
    return defaultValue;
}

+(NSDate *)getDateFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(NSDate *)defaultValue {
    
    NSObject *object = [dict objectForKey:key];
    
    if([object isKindOfClass:[NSNumber class]]) {
        NSNumber *value = [dict objectForKey:key];
        if(value && ![[NSNull null] isEqual:value]) {
            double secondsSinceEpoch = ([value doubleValue]/1000.0);
            return [NSDate dateWithTimeIntervalSince1970:secondsSinceEpoch];
        }
    } else if([object isKindOfClass:[NSString class]]) {
        NSString *date = [self safeGetDictString:dict withKey:key];
        NSDate *objDate = [[self zuluDateFormatterInstance] dateFromString:date];
        if(!objDate) {
            objDate = [[self isoDateFormatterInstance] dateFromString:date];
        }
        return objDate;
    } else if([object isKindOfClass:[NSDate class]]) {
        return (NSDate *)object;
    }
    

    return defaultValue;
}

#pragma mark Serialization Methods
+(NSString *)jsonStringFromArray:(NSArray *)array {
    return [Serializer jsonStringFromArray:array withPrettyPrint:NO];
}

+(NSString *)jsonStringFromArray:(NSArray *)array withPrettyPrint:(BOOL)prettyPrint {
    NSError *error;
    NSJSONWritingOptions opts = (prettyPrint ? NSJSONWritingPrettyPrinted : 0);
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:array options:opts error:&error];
    
    
    if (! jsonData) {
        NSLog(@"Serializer jsonStringFromDictionary: error: %@", error.localizedDescription);
    } else {
        return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
    
    return nil;
}

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

+(NSDateFormatter *)zuluDateFormatterInstance {
    if(!ZULU_FORMATTER) {
        ZULU_FORMATTER = [[NSDateFormatter alloc]init];
        ZULU_FORMATTER.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    }
    return ZULU_FORMATTER;
}

+(NSDateFormatter *)isoDateFormatterInstance {
    if(!ISO_FORMATTER) {
        ISO_FORMATTER = [[NSDateFormatter alloc]init];
        ISO_FORMATTER.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    }
    
    return ISO_FORMATTER;
}

#pragma mark Date Utility Methods

+(NSString *)formatZuluDateToString:(NSString *)dateString {
    NSDateFormatter *dateFormat = [NSDateFormatter new];
    dateFormat.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    NSDate *parsedDate = [dateFormat dateFromString:dateString];
    return [dateFormat stringFromDate:parsedDate];
}

+(NSString *)formatDateToZuluString:(NSDate *)date {
    NSDateFormatter *dateFormat = [NSDateFormatter new];
    dateFormat.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    return [dateFormat stringFromDate:date];
}

+(NSString *)formatIsoDateToString:(NSString *)dateString {
    NSDateFormatter *dateFormat = [NSDateFormatter new];
    dateFormat.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    NSDate *parsedDate = [dateFormat dateFromString:dateString];
    return [dateFormat stringFromDate:parsedDate];
}

+(NSString *)formatDateToIsoString:(NSDate *)date {
    NSDateFormatter *dateFormat = [NSDateFormatter new];
    dateFormat.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    return [dateFormat stringFromDate:date];
}

+(NSDate *)zuluDateStringToNSDate:(NSString *)dateString {
    NSDateFormatter *dateFormat = [NSDateFormatter new];
    dateFormat.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    return [dateFormat dateFromString:dateString];
}

+(NSDate *)isoDateStringToNSDate:(NSString *)dateString {
    NSDateFormatter *dateFormat = [NSDateFormatter new];
    dateFormat.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    return [dateFormat dateFromString:dateString];
}


@end
