# Revision History

## v0.0.5
- Added "Custom Template" capabilities (for swift) that allow you to specify your own templates.  Currently, the documentation is the crappy source code.
- Ripped out code generation mechanism for Swift Models and Swift Tests to use a Freemarker Template
- Ripped out code generation mechanism for Ruby Models and Specs to use a Freemarker Template
- Ripped out code generation mechanism for Objective-C Models and Tests to use a Freemarker Template
- Ripped out code generation mechanism for Java Models and Tests to use a Freemarker Template
- Setup Sample projects to demonstrate source code generation (and validate that we can serialize / deserialize the data)
- Swift (2.2) Support

## v0.0.4
- Rebuilt the interface using a "metadata.json" file that allows us to add additional metadata about json files we're generating code for.  Support for things like "transient properties",
- Ruby Support (generating models & spec tests)

## v0.0.3
- Prototype of View code generation for Obj-C
- Generating Unit Tests for Java
- Generating Unit Tests for Objective-C
- Fixed properties "id" and "description" for Objective-C (we rename them "theId" and "theDescription")

## v0.0.2
- Version string and date included in the source code header comments
- Added Java Support for the code generator
- Using Commons-CLI now to manage the command line options and usage output
- Added support for Booleans, (partial support for) Arrays of Objects, Arrays of Arrays
- working POC for the generator
