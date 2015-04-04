//
//  RestUtilsTest.m
//  Generated
//
//  Created by Eric Internicola on 4/4/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "RestUtils.h"

@interface RestUtilsTest : XCTestCase

@end

@implementation RestUtilsTest

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testBuildParamsFromNil {
    XCTAssertEqualObjects(@"", [RestUtils buildUrlParamsFromDictionary:nil]);
}

-(void)testBuildParamsFromEmptyDictionary {
    XCTAssertEqualObjects(@"", [RestUtils buildUrlParamsFromDictionary:[[NSDictionary alloc]init]]);
}

-(void)testBuildParamsFromDictionary {
    NSDictionary *dict = [[NSDictionary alloc]initWithObjectsAndKeys:@"b",@"a",@"d",@"c", nil];
    XCTAssertEqualObjects(@"?a=b&c=d", [RestUtils buildUrlParamsFromDictionary:dict]);
}

@end
