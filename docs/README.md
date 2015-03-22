# User Documentation
So, you want to generate some code using some JSON files?  Here is an overview of how this works (followed by detailed information).

## Overview
1. Gather your JSON files that you want to generate code
2. Ensure you "clean" the JSON files such that it's easy to identify the types of each property
3. Author a "Metadata JSON" file (organizes them all together)
4. Run the generator

## 1. Gathering JSON Files
Typically, you will have an application in mind, with a series of JSON files (which reflect the object models) that compose the data types of your application.  You'll want to gather these, do a good job of naming them (for your own sanity).

For an example, see the [sample-projects](../sample-projects) folder in this project.

```bash
mkdir json
cd json
cp ~/my-json-files/* ./
ls -la
```
might produce output like this:
```bash
lrwxr-xr-x   1 einternicola  staff     43 Mar 16 07:22 contest.json
-rw-r--r--   1 einternicola  staff   3311 Mar 16 07:22 enumerations.json
```

So now I have 2 JSON files in this folder.  Next, I need to clean this data.  Let's take a look.

## 2. Cleaning the JSON files
For a data type reference, please see the [Data Types Reference](DataTypes.md)
Data Types are inferred from the JSON Files, so it's important that you make sure they're ``clean``.  By ``clean``, I mean that you've done due dilligence to replace any ``null`` values in the document with some value that represents a true type (e.g. string, int, bool, array, etc).

### Bad Data
This is not a *perfect* product (ahem, yet).  There are limitations, and it's important that you are aware and work around these limitations
* ``null`` - I'm not a mind reader: help me, help you.
* Emtpy Arrays: ``[]`` - What kind of array is this?  I can't read your mind.
* Deeply Nested Arrays: ``[[[{this:'that'}]]]`` - Okay, I've got support for an "array of arrays", but if you want me to deserialize a bit deeper, I've got to write some code.  I'll admit, this is a limitation on my end, but I'm lazy.  :P

So, in summary, let's get your JSON cleaned up so it's parseable.  Check out this reference JSON:

```javascript
{
  "id": "ou812",
  "title": "My JSON Object",
  "state": "solid",
  "imageUrl": "image4x3:http://www.google.com/images/my-image.jpg",
  "description": "Do you think this JSON object can be used by any programming language?",
  "relatedObjects": [
    "5302e4d2aec0858466a69b3e"
  ],
  "results": [
    {
      "order": 1,
      "sponsor": {
        "contactEmail": "user@domain.com",
        "contactName": "Ed Gruberman",
        "id": "12345",
        "logo": "image16x9:http://google.com/images/fake.png",
        "phoneNumber": "(555) 555-5555"
      },
      "acknowledged": true
    }
  ],
  "createdDate": 1421158421429,
  "latitude": 39.79813221845757,
  "longitude": -105.7788929064812,
  "size": 50,
  "contact": {
    "contactEmail": "user@domain.com",
    "name": "Billy Bob",
    "logo": "image:http://www.google.com/some-image.jpg",
    "phoneNumber": "(555) 555-5555",
    "siteUrl": "http://www.google.com/"
  },
  "state": 2,
  "trackIDs": [
    "54c6cb8ce4b0e7fb4e9aed2b",
    "54c72851e4b0e7fb4e9aed40"
  ],
  "type": 0
}
```

This is very much a faux data type, but you'll notice the following:
1.  There are no ``null`` values in the document at all
2.  Strings that I expect to be images, I've placed an appropriate image seed into the string (``image4x3:``, ``image16x9:``, ``image:``)
3.  None of the arrays are empty or nested too deeply

## 3. Author Metadata JSON File
So you've gathered and cleaned your JSON files together, now you need to stitch it all together with your ``Metadata JSON`` file.

The purpose of this file is to do the following:
* Establish the Language to generate
* Establish the namespace for the code
* Link the associated JSON Files
* Map properties between the JSON Objects
* Add additional (transient) properties to the types
* Define *what* to generate (Models, Tests, Views, Services, REST Services, etc)


### Reference Metadata.json file
```javascript
{
  "language": "Java",
  "namespace": "com.intere.test",
  "classes": [
    {
      "className": "Contest",
      "jsonFile": "contest.json",
      "key": "id",
      "urlPath": "/contest",
      "readonly": false,
      "propertyMap": [
        {
          "property": "state",
          "mapClassName": "EnumerationTournamentStates",
          "mapClassProperty": "value"
        },
        {
          "property": "stateName",
          "mapClassName": "EnumerationTournamentStates",
          "mapClassProperty": "name"
        }
      ],
      "transientProperty": [
        {
          "className": "",
          "name": "eric",
          "type": "String"
        }
      ]
    },
    {
      "className": "Enumeration",
      "jsonFile": "enumerations.json",
      "readonly": true,
      "urlPath": "/enumerations"
    }
  ],
  "generate": {
    "models": true,
    "tests": true,
    "views": true,
    "services": true,
    "restServices": true
  }
}
```

The above JSON tells us the following:
* The language is Java: ``"language": "Java"``
* The namespace is "com.intere.test": ``"namespace": "com.intere.test"``
* There are 2 JSON Files being linked in (``classes`` array with 2 objects)
    * First Class should be called "Contest": ``"className": "Contest"``
    * First Class is based on contest.json: ``"jsonFile": "contest.json"``
    * Second Class should be called "Enumeration": ``"className": "Enumeration"``
    * Second class is based on enumerations.json: ``"jsonFile": "enumerations.json"``
* We expect to generate models, tests, views, services and rest services for the JSON types.  (``NOTE:`` at the time that this is written, for Java, we currently only support models and tests).
