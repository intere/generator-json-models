//
//  Serializer.h
//  Objective-C Code Generator
//
//  Licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//  Created by Eric Internicola on 12/8/13.
//

#import <Foundation/Foundation.h>
#import "Log.h"

@interface Serializer : NSObject

/** Protect us from NSNull!  */
+(NSString *)safeGetDictString:(NSDictionary *)dict withKey:(NSString *)key;

/** Sets an INT serialization key.  */
+(void)setDict:(NSMutableDictionary *)dict intValue:(NSInteger)value forKey:(NSString *)key;

/** Sets a Double serialization key.  */
+(void)setDict:(NSMutableDictionary *)dict doubleValue:(double)value forKey:(NSString *)key;

/** Sets a BOOL serialization key.  */
+(void)setDict:(NSMutableDictionary *)dict boolValue:(BOOL)value forKey:(NSString *)key;

/** Sets a NSDate serialization key.  */
+(void)setDict:(NSMutableDictionary *)dict dateValue:(NSDate *)date forKey:(NSString *)key;

/**
 * Helper function that sets a value in the dictionary using the provided object and key.  If the object is nil, nothing happens, you don't blow up.  Yay!
 */
+(void)setDict:(NSMutableDictionary *)dict object:(NSObject *)object forKey:(NSString *)key;

/**
 * Reads the specified key from the dictionary and tries to get a double from it (assumes it's an NSNumber).  If the object is nil, it will return you back the defaultValue you've provided.
 */
+(double)getDoubleFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(double)defaultValue;

/**
 * Reads the specified key from the dictionary and tries to get an Integer from it (assumes it's an NSNumber).  If the object is nil, it will return you back the default value you've provided.
 */
+(NSInteger)getIntegerFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(NSInteger)defaultValue;

/**
 * Reads the specified key from the dictionary and tries to get a BOOL from it (assumes it's an NSNumber).  If the object is nil, it will return you back the default value you've provided.
 */
+(BOOL)getBoolFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(BOOL)defaultValue;

/**
 * Reads you the specified key from the dictionary and tries to get a NSDate from it (native storage type is NSNumber wrapper of double).  If the object is nil, it will return you back the default value you've provided.
 */
+(NSDate *)getDateFromDict:(NSDictionary *)dict forKey:(NSString *)key orDefaultTo:(NSDate *)defaultValue;

//
//
// Serialization
//
//


/**
 * Uses the provided dictionary and serializes that into a JSON string for you and returns it:
 * NOTE: This *will not* be a pretty print JSON string.
 */
+(NSString *)jsonStringFromDictionary:(NSDictionary *)dict;

/**
 * Uses the provided dictionary and serializes that into a JSON string for you.  
 * NOTE: It will do pretty printing if you ask that of it.
 */
+(NSString *)jsonStringFromDictionary:(NSDictionary *)dict withPrettyPrint:(BOOL)prettyPrint;

/**
 * The internal "Time Interval" used by iOS is not the same as it is for all other languages I'm familiar with
 * so this method will convert the given date into an Integer that is "portable".
 */
+(double)dateToStandardTimeInterval:(NSDate *)date;

//
//
// Deserialization
//
//

/** Uses the provided (JSON) String object to deserialize into an NSArray.  */
+(NSArray *)arrayFromJsonString:(NSString *)json andError:(NSError *)error;

/** Uses the provided (JSON) NSData object to deserialize into an NSArray.  */
+(NSArray *)arrayFromJsonData:(NSData *)data andError:(NSError *)error;

/**
 * Uses the provided (JSON) NSData object to deserialize into an NSDictionary.
 */
+(NSDictionary *)fromJsonData:(NSData *)data;

/**
 * Uses the provided (JSON) String object to deserialize into an NSDictionary.
 */
+(NSDictionary *)fromJsonString:(NSString *)json;

/**
 * Takes the Standard Time Interval you provide and gives you back an NSDate object for it.
 */
+(NSDate *)standardTimeIntervalToDate:(double)standardTime;


@end
