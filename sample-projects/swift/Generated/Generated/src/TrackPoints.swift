/**
 * TrackPoints.swift
 *
 * Generated by JSON Model Generator v0.0.4 on Aug 13, 2016
 * https://github.com/intere/generator-json-models
 *
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
 */

import Foundation

class TrackPoints {
    var alt: Double?
    var dist: Int?
    var hAcc: Double?
    var lat: Double?
    var lon: Double?
    var speed: Double?
    var time: NSDate?
    var vAcc: Double?

}

// MARK: - Serialization

extension TrackPoints {

    /**
    Serializes this TrackPoints to a Dictionary.
    - Returns: A Map which represents this object as a serialized object
    */
    func toMap() -> [String: AnyObject] {
        var dict = [String: AnyObject]()

        dict["alt"] = alt
        dict["dist"] = dist
        dict["h_acc"] = hAcc
        dict["lat"] = lat
        dict["lon"] = lon
        dict["speed"] = speed
        dict["time"] = TrackPoints.toEpochDateInt(time)
        dict["v_acc"] = vAcc

        return dict
    }

    /**
    Converts the provided array of TrackPoints Objects to an array of dictionaries.
    - Parameter models: An array of TrackPoints Objects to be converted to an array of dictionaries.
    - Returns: An array of Dictionaries
    */
    class func toMapArray(models: [TrackPoints]?) -> [[String: AnyObject]]? {
        guard let models = models else {
            return nil
        }
        var maps = [[String: AnyObject]]()

        for model in models {
            maps.append(model.toMap())
        }

        return maps
    }

    /**
    Converts this model to JSON Data.
    - Returns: Optional JSON Data (returns nil if there's a problem serializing the map).
    */
    func toJsonData() -> NSData? {
        let dict = toMap()
        do {
            return try NSJSONSerialization.dataWithJSONObject(dict, options: .PrettyPrinted)
        } catch {
            print("Error serializing this model to JSON: \(dict)")
        }

        return nil
    }

	/**
	Converts the provided array of models to JSON Data.
	- Parameter array: Array of TrackPoints objects to be serialized.
	- Returns: NSData that contains the serialized objects, or nil if there's a problem serializing them.
	*/
	class func toJsonData(array: [TrackPoints]) -> NSData? {
		let mapArray = toMapArray(array) as! AnyObject
		do {
			return try NSJSONSerialization.dataWithJSONObject(mapArray, options: .PrettyPrinted)
		} catch {
			print("Error serializing array of models to JSON: \(mapArray)")
		}
		return nil
	}

	/**
	Converts this model to a JSON String.
	- Returns: Optional String (nil if there's a problem serializing the map to Data or converting it to a UTF-8 String).
	*/
	func toJsonString() -> String? {
	    guard let data = toJsonData() else {
	        return nil
        }

        return NSString(data: data, encoding: NSUTF8StringEncoding) as? String
	}

    /**
    Converts the provided array of models to a JSON String.
    - Parameter array: Array of TrackPoints objects to be serialized
    - Returns: Optional String (nil if there's a problem serializing to NSData or converting to a UTF-8 String).
    */
    class func toJsonString(array: [TrackPoints]) -> String? {
        guard let data = toJsonData(array) else {
            return nil
        }

        return NSString(data: data, encoding: NSUTF8StringEncoding) as? String
    }

}

// MARK: - Deserialization

extension TrackPoints {

    /**
    Gets you a TrackPoints object from the provided map
    - Parameter map: The Map to read the data from.
    - Returns: A TrackPoints object (assuming the map you provided is non-nil)
    */
    class func fromMap(map: [String:AnyObject]?) -> TrackPoints? {
        guard let map = map else {
            return nil
        }

        let model = TrackPoints()
        model.alt = map["alt"] as? Double
        model.dist = map["dist"] as? Int
        model.hAcc = map["h_acc"] as? Double
        model.lat = map["lat"] as? Double
        model.lon = map["lon"] as? Double
        model.speed = map["speed"] as? Double
        model.time = TrackPoints.fromEpochDateInt(map["time"] as? Int)
        model.vAcc = map["v_acc"] as? Double
        return model
    }

    /**
    Creates an Array of TrackPoints objects for the provided Array of Maps.
    - Parameter mapArray: An array of Maps, assumes that each map is a serialized TrackPoints.
    - Returns: An array of TrackPoints objects (should be 1 to 1).
    */
    class func fromArrayOfMaps(mapArray: [[String:AnyObject]]?) -> [TrackPoints]? {
        guard let mapArray = mapArray else {
            return nil
        }

        var modelArray = [TrackPoints]()

        for map in mapArray {
            guard let model = fromMap(map) else {
                continue
            }
            modelArray.append(model)
        }

        return modelArray
    }

    /**
    Gives you back a collection of TrackPoints objects for the provided JSON Serialized Data.  This method handles both a single object (map) and an array of objects (array of maps).
    - Parameter data: The NSData (optional) to pull the objects from.
    - Returns: An array of TrackPoints objects that were deserialized from the provided data.
    */
    class func fromData(data: NSData?) -> [TrackPoints]? {
        guard let data = data else {
            return []
        }

        do {
            let jsonData = try NSJSONSerialization.JSONObjectWithData(data, options: .AllowFragments)

            // If we have an array of dictionaries:
            if let jsonArray = jsonData as? [[String:AnyObject]] {
                return fromArrayOfMaps(jsonArray)
            } else if let jsonMap = jsonData as? [String:AnyObject] {
                if let model = fromMap(jsonMap) {
                    return [model]
                }
            } else {
                print("Unknown JSON Data type: \(jsonData)")
            }
        } catch {
            print("Error deserializing json data")
        }

        return []
    }

	/**
	 Gives you back a collection of TrackPoints objects after deserializing them from a String to Data, Data to a Dictionary and then finally to a Model Object.
	 - Parameter json: The (UTF-8) JSON String to deserialize into TrackPoints objects.
	 - Returns: An Array of TrackPoints objects.
	*/
	class func fromJson(json: String) -> [TrackPoints]? {
		let data = (json as NSString).dataUsingEncoding(NSUTF8StringEncoding)
		return fromData(data)
	}

}

// MARK: - Date Methods

extension TrackPoints {

    	class func fromEpochDateInt(dateInt: Int?) -> NSDate? {
    	    guard let dateInt = dateInt else {
    	        return nil
    	    }

    		let dateDouble = NSTimeInterval(Double(dateInt) / 1000)
    		return NSDate(timeIntervalSince1970: dateDouble)
    	}

    	class func toEpochDateInt(date: NSDate?) -> Int? {
    	    guard let date = date else {
    			return nil
    		}

    		return Int(date.timeIntervalSince1970 * 1000)
    	}

}