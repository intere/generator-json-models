/**
 * ContestResultsSummary.swift
 *
 * Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
 * https://github.com/intere/generator-json-models
 *
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
 */

import Foundation

class ContestResultsSummary {
    var dir: String?
    var dist: Double?
    var duration: Int?
    var idxEnd: Int?
    var idxStart: Int?
    var maxElev: Double?
    var maxSpeed: Double?
    var minElev: Double?
    var runs: Int?
    var vAscent: Double?
    var vAscentDist: Double?
    var vDescent: Double?
    var vDescentDist: Double?
    var vDelta: Double?

}

// MARK: - Serialization

extension ContestResultsSummary {

    /**
    Serializes this ContestResultsSummary to a Dictionary.
    - Returns: A Map which represents this object as a serialized object
    */
    func toMap() -> [String: AnyObject] {
        var dict = [String: AnyObject]()

        dict["dir"] = dir
        dict["dist"] = dist
        dict["duration"] = duration
        dict["idx_end"] = idxEnd
        dict["idx_start"] = idxStart
        dict["max_elev"] = maxElev
        dict["max_speed"] = maxSpeed
        dict["min_elev"] = minElev
        dict["runs"] = runs
        dict["vAscent"] = vAscent
        dict["vAscentDist"] = vAscentDist
        dict["vDescent"] = vDescent
        dict["vDescentDist"] = vDescentDist
        dict["v_delta"] = vDelta

        return dict
    }

    /**
    Converts the provided array of ContestResultsSummary Objects to an array of dictionaries.
    - Parameter models: An array of ContestResultsSummary Objects to be converted to an array of dictionaries.
    - Returns: An array of Dictionaries
    */
    class func toMapArray(models: [ContestResultsSummary]?) -> [[String: AnyObject]]? {
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
	- Parameter array: Array of ContestResultsSummary objects to be serialized.
	- Returns: NSData that contains the serialized objects, or nil if there's a problem serializing them.
	*/
	class func toJsonData(array: [ContestResultsSummary]) -> NSData? {
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
    - Parameter array: Array of ContestResultsSummary objects to be serialized
    - Returns: Optional String (nil if there's a problem serializing to NSData or converting to a UTF-8 String).
    */
    class func toJsonString(array: [ContestResultsSummary]) -> String? {
        guard let data = toJsonData(array) else {
            return nil
        }

        return NSString(data: data, encoding: NSUTF8StringEncoding) as? String
    }

}

// MARK: - Deserialization

extension ContestResultsSummary {

    /**
    Gets you a ContestResultsSummary object from the provided map
    - Parameter map: The Map to read the data from.
    - Returns: A ContestResultsSummary object (assuming the map you provided is non-nil)
    */
    class func fromMap(map: [String:AnyObject]?) -> ContestResultsSummary? {
        guard let map = map else {
            return nil
        }

        let model = ContestResultsSummary()
        model.dir = map["dir"] as? String
        model.dist = map["dist"] as? Double
        model.duration = map["duration"] as? Int
        model.idxEnd = map["idx_end"] as? Int
        model.idxStart = map["idx_start"] as? Int
        model.maxElev = map["max_elev"] as? Double
        model.maxSpeed = map["max_speed"] as? Double
        model.minElev = map["min_elev"] as? Double
        model.runs = map["runs"] as? Int
        model.vAscent = map["vAscent"] as? Double
        model.vAscentDist = map["vAscentDist"] as? Double
        model.vDescent = map["vDescent"] as? Double
        model.vDescentDist = map["vDescentDist"] as? Double
        model.vDelta = map["v_delta"] as? Double
        return model
    }

    /**
    Creates an Array of ContestResultsSummary objects for the provided Array of Maps.
    - Parameter mapArray: An array of Maps, assumes that each map is a serialized ContestResultsSummary.
    - Returns: An array of ContestResultsSummary objects (should be 1 to 1).
    */
    class func fromArrayOfMaps(mapArray: [[String:AnyObject]]?) -> [ContestResultsSummary]? {
        guard let mapArray = mapArray else {
            return nil
        }

        var modelArray = [ContestResultsSummary]()

        for map in mapArray {
            guard let model = fromMap(map) else {
                continue
            }
            modelArray.append(model)
        }

        return modelArray
    }

    /**
    Gives you back a collection of ContestResultsSummary objects for the provided JSON Serialized Data.  This method handles both a single object (map) and an array of objects (array of maps).
    - Parameter data: The NSData (optional) to pull the objects from.
    - Returns: An array of ContestResultsSummary objects that were deserialized from the provided data.
    */
    class func fromData(data: NSData?) -> [ContestResultsSummary]? {
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
	 Gives you back a collection of ContestResultsSummary objects after deserializing them from a String to Data, Data to a Dictionary and then finally to a Model Object.
	 - Parameter json: The (UTF-8) JSON String to deserialize into ContestResultsSummary objects.
	 - Returns: An Array of ContestResultsSummary objects.
	*/
	class func fromJson(json: String) -> [ContestResultsSummary]? {
		let data = (json as NSString).dataUsingEncoding(NSUTF8StringEncoding)
		return fromData(data)
	}

}

