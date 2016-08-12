generator-json-models
=====================
Multi-Language (currently Objective-C, Ruby and Java) JSON Model Class Creator.

## Objective
Using JSON files as a starting point, generated models, services, views, unit tests, etc that will jumpstart you into the building blocks for your application.

## Supported Languages
* Objective-C
* Java
* Ruby
* Swift

## Documentation
See: [docs/README.md](docs/README.md)

## Project Management / Feature Roadmap
Pivotal Tracker: https://www.pivotaltracker.com/n/projects/1295722

### Roadmap (high level)
* ~~Migrate to using Spring~~
* ~~Refactor the LanguageUtility interfaces~~
* Add Swift Support - IN PROGRESS
* Create a Spring Shell Interface
* Create View, Service, Rest Service, Rest Client code for all supported languages

## Background
I was / am in the process of building a full stack application that has an Objective-C
front end.  It became apparent that it was a lot of work to create JSON serializing / deserializing classes in Objective-C (not difficult, just tedious).  I wanted to build a tool that would save me a lot of time by reading a JSON file and generating an Objective-C class that was capable of the following:
1.  Representing that JSON as a model
2.  Serializing / Deserializing that JSON into the model (including collections)
3.  Support nested classes (not a feature of ObjC, so I've created a class naming mechanism to work around this)
4.  Create Unit Tests that validate the ability to serialize / deserialize properly

## Strategy
Early on, I was just generating a set of classes from a single json file, but quickly realized that this didn't cut it.  I wanted to be able to generate a full set of JSON for a full set of related JSON files, and I wanted to be able to add transient properties, override data, etc.  So I came up with the following strategy:

* Create a ``Metdata JSON`` File (the orchestration file)
    * References all of the JSON files that we're going to generate code for
    * Configures the Language
    * Configures "what" to generate
    * Configures the "namespace" (unused in Obj-C, package in Java, Module in Ruby)
* Generates *all* of the code that's referenced in the ``Metadata JSON`` file

## Use Cases
* See the Sample Projects for use cases (below).  I have setup 3 sample projects (which don't do a heck of a lot yet), but do demonstrate the code generation

## Dog Food?
* This project "eats it's own dog food".  I have literally used the code in this project to generate code *for* this project.  I have a "dog-food.sh" script that generates code that is used by this project.  This has helped me drive additional use cases (like the addition of "transient properties" to the models).

## Usage
### App Usage Information:
```
usage: code-generator
 -o,--output-location <arg>   Where do you want the generated code to go?
 -or,--orchestrate <arg>      Specify a metadata.json file to use to
                              orchestrate the code generation (overrides
                              most command line options)
```

### Building / Running
```bash
# First you need to compile the code and ensure it's up to date:
mvn clean install
```

## Sample Projects
### Generating
```bash
mvn clean install  # Ensure it's built
cd sample-projects
./setup.sh
```

## Sample Usage
### New Usage
```bash
# Generates Objective-C Code
./run.sh --orchestrate ${PWD}/sample-projects/metadata-objc.json \
    -o ${PWD}/tmp/obj-c

# Generates Java Code
./run.sh --orchestrate ${PWD}/sample-projects/metadata-java.json \
    -o ${PWD}/tmp/java

# Generates Ruby Code
./run.sh --orchestrate ${PWD}/sample-projects/metadata-ruby.json \
    -o ${PWD}/tmp/ruby
```

# Generates Swift Code
```
./run --orchestrate ${PWD}/sample-projects/metadata-swift.json \
    -o ${PWD}/tmp/swift
```

## License
This tool is Licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
My intent is to allow you to use this tool for personal and/or commercial purposes.
