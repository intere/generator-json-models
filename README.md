generator-objective-c
=====================
Objective-C Class creator (via JSON).  Essentially it takes JSON and a Class Name, and generates .h and .m files that will have properties and serialization/deserialization methods implemented.
This is currently a *very* rudimentary tool.  It does not dive any deeper than top level (primitive) attributes.

##Background
I was / am in the process of building a full stack application that has an Objective-C
front end.  It became apparent that it was a lot of work to create JSON serializing / deserializing
classes in Objective-C (not difficult, just tedious).  I wanted to build a tool that
would save me a lot of time by reading a JSON file and generating an Objective-C
class that was capable of the following:
1.  Representing that JSON as a model
2.  Deserializing that JSON into the model
3.  Serializing the Model into the same style JSON.


##Notes
You'll want to make use of the following to files (add them to your Objective-C project)
* Serializer.h
* Serializer.m

These 2 files just have some utility methods used by the generated code.

##Usage

    # First you need to compile the code and ensure it's up to date:
    $ mvn clean install

    # Now you can run it - there are 3 sample JSON files provided to demonstrate
    # the capabilities / limitations of the project:

    # The first - is a "user"
    $ mvn exec:java -Dexec.args="User src/test/resources/user.json"
    
    # The second is an array - this demonstrates (current) limitations of the project
    $ mvn exec:java -Dexec.args="TrackList src/test/resources/track-list.json"

    # The third is a track
    $ mvn exec:java -Dexec.args="Track src/test/resources/track.json"

##License
This tool is Licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content

My intent is to allow you to use this tool for personal and/or commercial purposes.

##Limitations
* Currently this project doesn't handle nested object structures
* Currently this project doesn't handle top level arrays

##Why Java?
    Q: Why would you ever create a tool that generates Objective-C code using Java?
    A: My primary competency is in Java, and it was quick and dirty to do.
