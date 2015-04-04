//
//  ContestRestClientTest.m
//  Generated
//
//  Created by Eric Internicola on 4/3/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "ContestRestClient.h"

@interface ContestRestClientTest : XCTestCase {
    NSString *defaultUrl;
}
@end

#define KEY_DEFAULT_URL @"DefaultBaseUrl"
#define MAGOO_PROD @"https://api.brothersmagoo.com"

@implementation ContestRestClientTest

- (void)setUp {
    [super setUp];
//    https://api.brothersmagoo.com/contest
    defaultUrl = [[NSUserDefaults standardUserDefaults] stringForKey:KEY_DEFAULT_URL];
    [[NSUserDefaults standardUserDefaults] setValue:MAGOO_PROD forKey:KEY_DEFAULT_URL];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [[NSUserDefaults standardUserDefaults] setValue:defaultUrl forKey:KEY_DEFAULT_URL];
    [super tearDown];
}

- (void)testExample {
    NSArray *array = [ContestRestClient getDefaultContestArray];
    XCTAssertNotNil(array);
    XCTAssertTrue(array.count > 0);
}


@end
