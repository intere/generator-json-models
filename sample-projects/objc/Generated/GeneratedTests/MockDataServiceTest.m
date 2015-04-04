//
//  MockDataServiceTest.m
//  Generated
//
//  Created by Eric Internicola on 3/12/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "MockDataService.h"

@interface MockDataServiceTest : XCTestCase

@end

@implementation MockDataServiceTest

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testServiceExists {
    MockDataService *service = [MockDataService getSharedInstance];
    XCTAssertNotNil(service);
}

-(void)testDataLoaded {
    NSMutableArray *data = [MockDataService getSharedInstance].contests;
    XCTAssertNotNil(data);
    XCTAssertTrue(data.count > 0);
}


@end
