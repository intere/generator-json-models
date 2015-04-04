//
//  MockDataService.h
//  Generated
//
//  Created by Eric Internicola on 3/12/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Contest.h"

@interface MockDataService : NSObject
@property (nonatomic, strong) NSMutableArray *contests;

+(MockDataService *)getSharedInstance;
@end
