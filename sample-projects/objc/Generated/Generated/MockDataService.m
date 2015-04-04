//
//  MockDataService.m
//  Generated
//
//  Created by Eric Internicola on 3/12/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import "MockDataService.h"
#import "Serializer.h"

static MockDataService *sharedMockDataService;

@interface MockDataService() {
    
}
-(void)loadMockContests;
@end

@implementation MockDataService
-(id)init {
    self = [super init];
    if(self) {
        self.contests = [[NSMutableArray alloc]init];
        [self loadMockContests];
    }
    return self;
}

+(MockDataService *)getSharedInstance {
    if(!sharedMockDataService) {
        sharedMockDataService = [[MockDataService alloc]init];
    }
    return sharedMockDataService;
}

#pragma mark Private Methods
-(void)loadMockContests {
    NSString* path = [[NSBundle mainBundle] pathForResource:@"contests" ofType:@"json"];
    NSString* data = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:NULL];
    NSError *error = nil;
    NSArray *arrayOfDicts = [Serializer arrayFromJsonString:data andError:error];
    if(error) {
        NSLog(@"Error loading mock data: %@", error.localizedDescription);
        return;
    }
    self.contests = [Contest fromArrayOfDictionaries:arrayOfDicts];
}
@end
