//
//  DataLoadingService.h
//  Generated
//
//  Created by Eric Internicola on 4/4/15.
//  Copyright (c) 2015 intere. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DataLoadingService : NSObject

/** This method loads all of the (default) users in the system and stores them in the UserService.allUser array. */
-(void)loadAllUsers;

/** This method loads all of the (default) contests and stores them in the ContestService.allContest array.  */
-(void)loadAllContests;

/** This method loads all of the (default) enumeration data and stores it in the EnumerationService.allEnumeration array.  */
-(void)loadEnumerationData;

/** Singleton Accessor Method.  */
+(DataLoadingService *)getSharedInstance;

@end
