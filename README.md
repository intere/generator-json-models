generator-json-models
=====================
Multi-Language (currently Objective-C, Ruby and Java) JSON Model Class Creator.

For Objective-C: Essentially it takes JSON and a Class Name, and generates .h and .m files that will have properties and serialization/deserialization methods implemented.

##Background
I was / am in the process of building a full stack application that has an Objective-C
front end.  It became apparent that it was a lot of work to create JSON serializing / deserializing
classes in Objective-C (not difficult, just tedious).  I wanted to build a tool that
would save me a lot of time by reading a JSON file and generating an Objective-C
class that was capable of the following:
1.  Representing that JSON as a model
2.  Deserializing that JSON into the model (including collections)
3.  Serializing the Model into the same style JSON.
4.  Support nested classes (not a feature of ObjC, so I've created a class naming mechanism to work around this)


##Usage
App Usage Information:
```
usage: code-generator
 -cn,--className <arg>        What is the name for the base class?  You
                              must provide this
 -f,--input-file <arg>        The Input (JSON) File to read to generate
                              the class
 -l,--language <arg>          What Language you would like to use, can be
                              one of: objc (Objective-C), ruby, java
 -ns,--namespace <arg>        The Namespace (ruby) or Package (java) that
                              the generated code should live in
 -o,--output-location <arg>   Where do you want the generated code to go?
```

Building / Running
```bash
# First you need to compile the code and ensure it's up to date:
mvn clean install

# Now you can run it - there are 3 sample JSON files provided to demonstrate
# the capabilities / limitations of the project:

# The first - is a "user"
mvn exec:java -Dexec.args="--className User --input-file src/test/resources/user.json"

# The second is an array - this demonstrates (current) limitations of the project
mvn exec:java -Dexec.args="-cn TrackList -f src/test/resources/track-list.json"

# The third is a track
mvn exec:java -Dexec.args="-cn Track -f src/test/resources/track.json"
```

## Sample Usage
```bash
./run.sh -f src/test/resources/contests.json \
  -cn Contest \
  -l objc \
  -o tmp/objc
```

##License
This tool is Licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content

My intent is to allow you to use this tool for personal and/or commercial purposes.

##Limitations
* Currently this project doesn't handle arrays of arrays

##Why Java?
    Q: Why would you ever create a tool that generates Objective-C and Ruby code using Java?
    A: My primary competency is in Java, and it was quick and dirty to do.
