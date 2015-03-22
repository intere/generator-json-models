# Data Types
Currently we support the following data types (inferred from reading the JSON)
* ``String`` - A String that is less than 25 characters
* ``Text`` - A String that is longer than 25 characters
* ``Image`` - A String that starts with one of the following:
    * ``image:`` - indicates it's an image that doesn't have a specific aspect ratio
    * ``image4x3:`` - indicates it's an image that is in the 4x3 aspect ratio
    * ``image16x9:`` - indicates it's an image that is in the 16x9 aspect ratio
* ``Date`` - A String that matches one of the following:
    * Zulu-Formatted Date: "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    * ISO-Formatted Date: "yyyy-MM-dd'T'HH:mm:ss"
    * Long Number - converted to a date that is greater than Jan 1, 2000
* ``Long`` - an Integer Number (this is both INT and LONG, e.g. -42, 42, 123456789012)
* ``Double`` - a Decimal number (e.g. 123.45)
* ``Boolean`` - a True/False value
* ``Array`` - An array of something (e.g. array of integers, array of objects, array of arrays)
* ``Class`` - Some type of nested object
