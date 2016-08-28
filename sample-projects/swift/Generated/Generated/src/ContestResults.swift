/**
 * ContestResults.swift
 *
 * Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
 * https://github.com/intere/generator-json-models
 *
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
 */

import Foundation

class ContestResults {
    var order: Int?
    var sponsor: ContestResultsSponsor?
    var summary: ContestResultsSummary?
    var trackId: String?
    var userId: String?
    var winner: Bool?

}

// MARK: - Serialization

extension ContestResults {

    /**
    Serializes this ContestResults to a Dictionary.
    - Returns: A Map which represents this object as a serialized object
    */
    func toMap() -> [String: AnyObject] {
        var dict = [String: AnyObject]()

        dict["order"] = order
        if let sponsor = sponsor {
            dict["sponsor"] = sponsor.toMap()
        }
        if let summary = summary {
            dict["summary"] = summary.toMap()
        }
        dict["trackId"] = trackId
        dict["userId"] = userId
        dict["winner"] = winner

        return dict
    }

    /**
    Converts the provided array of ContestResults Objects to an array of dictionaries.
    - Parameter models: An array of ContestResults Objects to be converted to an array of dictionaries.
    - Returns: An array of Dictionaries
    */
    class func toMapArray(models: [ContestResults]?) -> [[String: AnyObject]]? {
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
	- Parameter array: Array of ContestResults objects to be serialized.
	- Returns: NSData that contains the serialized objects, or nil if there's a problem serializing them.
	*/
	class func toJsonData(array: [ContestResults]) -> NSData? {
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
    - Parameter array: Array of ContestResults objects to be serialized
    - Returns: Optional String (nil if there's a problem serializing to NSData or converting to a UTF-8 String).
    */
    class func toJsonString(array: [ContestResults]) -> String? {
        guard let data = toJsonData(array) else {
            return nil
        }

        return NSString(data: data, encoding: NSUTF8StringEncoding) as? String
    }

}

// MARK: - Deserialization

extension ContestResults {

    /**
    Gets you a ContestResults object from the provided map
    - Parameter map: The Map to read the data from.
    - Returns: A ContestResults object (assuming the map you provided is non-nil)
    */
    class func fromMap(map: [String:AnyObject]?) -> ContestResults? {
        guard let map = map else {
            return nil
        }

        let model = ContestResults()
        model.order = map["order"] as? Int
        model.sponsor = ContestResultsSponsor.fromMap(map["sponsor"] as? [String: AnyObject])
        model.summary = ContestResultsSummary.fromMap(map["summary"] as? [String: AnyObject])
        model.trackId = map["trackId"] as? String
        model.userId = map["userId"] as? String
        model.winner = map["winner"] as? Bool
        return model
    }

    /**
    Creates an Array of ContestResults objects for the provided Array of Maps.
    - Parameter mapArray: An array of Maps, assumes that each map is a serialized ContestResults.
    - Returns: An array of ContestResults objects (should be 1 to 1).
    */
    class func fromArrayOfMaps(mapArray: [[String:AnyObject]]?) -> [ContestResults]? {
        guard let mapArray = mapArray else {
            return nil
        }

        var modelArray = [ContestResults]()

        for map in mapArray {
            guard let model = fromMap(map) else {
                continue
            }
            modelArray.append(model)
        }

        return modelArray
    }

    /**
    Gives you back a collection of ContestResults objects for the provided JSON Serialized Data.  This method handles both a single object (map) and an array of objects (array of maps).
    - Parameter data: The NSData (optional) to pull the objects from.
    - Returns: An array of ContestResults objects that were deserialized from the provided data.
    */
    class func fromData(data: NSData?) -> [ContestResults]? {
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
	 Gives you back a collection of ContestResults objects after deserializing them from a String to Data, Data to a Dictionary and then finally to a Model Object.
	 - Parameter json: The (UTF-8) JSON String to deserialize into ContestResults objects.
	 - Returns: An Array of ContestResults objects.
	*/
	class func fromJson(json: String) -> [ContestResults]? {
		let data = (json as NSString).dataUsingEncoding(NSUTF8StringEncoding)
		return fromData(data)
	}

}

