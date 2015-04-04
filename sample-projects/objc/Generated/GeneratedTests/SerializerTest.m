//
//  SerializerTest.m
//  Generated
//
//  Created by Eric Internicola on 3/5/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "Serializer.h"

@interface SerializerTest : XCTestCase

@end

@implementation SerializerTest

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testDateStrings {
    NSDate *date = [[NSDate alloc]init];    // now
    NSString *zuluDate = [Serializer formatDateToZuluString:date];
    NSString *isoDate = [Serializer formatDateToIsoString:date];
    NSLog(@"Zulu Date:     %@", zuluDate);
    NSLog(@"ISO Date:      %@", isoDate);
    XCTAssertNotEqualObjects(zuluDate, isoDate);
    NSString *whatIsThis = [Serializer formatZuluDateToString:zuluDate];
    NSLog(@"What is this?  %@", whatIsThis);
    XCTAssertNotNil(whatIsThis);
    
    NSDate *fromString = [Serializer zuluDateStringToNSDate:zuluDate];
    XCTAssertEqualWithAccuracy([Serializer dateToStandardTimeInterval:fromString], [Serializer dateToStandardTimeInterval:date], 1);
    
    fromString = [Serializer isoDateStringToNSDate:isoDate];
    XCTAssertEqualWithAccuracy([Serializer dateToStandardTimeInterval:fromString], [Serializer dateToStandardTimeInterval:date], 1);
}

@end
