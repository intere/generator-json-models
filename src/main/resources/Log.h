//
//  Log.h
//
//  Released with JSON Model Generator ${version} on ${date}
//    https://github.com/intere/generator-json-models
//
//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
//
//

#ifndef __Generated_Log_h__
#define __Generated_Log_h__



#ifdef __DD_LOG_H__
#import "DDLog.h"
#import "DDASLLogger.h"
#import "DDTTYLogger.h"
#import "DDFileLogger.h"

// Log Level
static const int ddLogLevel = LOG_LEVEL_DEBUG;

#define NSLog CSLogDebug

#define CSLogDebug(__FORMAT__, ...) DDLogDebug((@"%s [Line %d] " __FORMAT__), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define CSLogInfo(__FORMAT__, ...) DDLogInfo((@"%s [Line %d] " __FORMAT__), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define CSLogWarn(__FORMAT__, ...) DDLogWarn((@"%s [Line %d] " __FORMAT__), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define CSLogError(__FORMAT__, ...) DDLogError((@"%s [Line %d] " __FORMAT__), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#define CSLogVerbose(__FORMAT__, ...) DDLogVerbose((@"%s [Line %d] " __FORMAT__), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)

#endif  // __DD_LOG_H__


#endif  // __Generated_Log_h__
