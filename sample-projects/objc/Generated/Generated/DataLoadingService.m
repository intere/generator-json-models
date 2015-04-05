//
//  DataLoadingService.m
//  Generated
//
//  Created by Eric Internicola on 4/4/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import "DataLoadingService.h"
#import "UserRestClient.h"
#import "UserService.h"
#import "ContestRestClient.h"
#import "ContestService.h"
#import "EnumerationRestClient.h"
#import "EnumerationService.h"
#import "VendorRestClient.h"
#import "VendorService.h"
#import "PropertyRestClient.h"
#import "PropertyService.h"

static DataLoadingService *sharedDataLoadingService;

#define API_KEY @"peuts4ev6ba1Huc7eB4jiaJ0Ki3wued5uj0Ik2uf"
#define DEFAULT_OPTIONS [[NSDictionary alloc]initWithObjectsAndKeys:API_KEY, @"apiKey", nil]

@implementation DataLoadingService

-(id)init {
    self = [super init];
    if(self) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{[self loadAllUsers];});
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{[self loadAllContests];});
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{[self loadEnumerationData];});
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{[self loadAllVendors];});
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{[self loadAllProperties];});
    }
    return self;
}

-(void)loadAllUsers {
    NSArray *arrayOfUsers = [UserRestClient getUserWithOptions:DEFAULT_OPTIONS];
    [UserService getSharedInstance].allUser = [[NSMutableArray alloc]initWithArray:arrayOfUsers];
}

-(void)loadAllContests {
    NSDictionary *options = [[NSDictionary alloc]initWithObjectsAndKeys:API_KEY, @"apiKey",
                             @"true", @"all",
                             [self getStartDate], @"startDate",
                             [self getEndDate], @"endDate",
                             nil];
    
    NSArray *arrayOfContests = [ContestRestClient getContestWithOptions:options];
    [ContestService getSharedInstance].allContest = [[NSMutableArray alloc]initWithArray:arrayOfContests];
}

-(void)loadEnumerationData {
    Enumeration *object = [EnumerationRestClient getEnumerationById:@""];
    [[EnumerationService getSharedInstance].namedEnumerationObjects setObject:object forKey:@"selectedEnumeration"];
}

-(void)loadAllVendors {
    NSArray *arrayOfVendors = [VendorRestClient getVendorWithOptions:DEFAULT_OPTIONS];
    [VendorService getSharedInstance].allVendor = [[NSMutableArray alloc]initWithArray:arrayOfVendors];
}

-(void)loadAllProperties {
    NSArray *allProperties = [PropertyRestClient getPropertyWithOptions:DEFAULT_OPTIONS];
    [PropertyService getSharedInstance].allProperty = [[NSMutableArray alloc]initWithArray:allProperties];
}

#pragma mark Static Methods

+(DataLoadingService *)getSharedInstance {
  if(!sharedDataLoadingService) {
    sharedDataLoadingService = [[DataLoadingService alloc] init];
  }
  return sharedDataLoadingService;
}

#pragma mark Private Methods

-(NSString *)getStartDate {
    NSDate *today = [[NSDate alloc] init];
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *offsetComponents = [[NSDateComponents alloc] init];
    [offsetComponents setMonth:-2]; // note that I'm setting it to -1
    NSDate *startDate = [gregorian dateByAddingComponents:offsetComponents toDate:today options:0];
    
    return [self dateToString:startDate];
}

-(NSString *)getEndDate {
    return [self dateToString:[[NSDate alloc]init]];
}

-(NSString *)dateToString:(NSDate *)date {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    // going for: "Wed Feb 18 2015 00:00:00 GMT-0700 (MST)"
    [formatter setDateFormat:@"eee MMM dd YYYY HH:mm:ss 'GMT'ZZZ (zzz)"];

    return [formatter stringFromDate:date];
}
@end
