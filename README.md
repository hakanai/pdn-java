
pdn-java
========

Java implementation of the Paint.NET file format.

I wanted to implement this with Kaitai Struct but there
were some places where I couldn't figure out how to make
it work, so I resorted to writing in pure Java.

Working:

* Basic image reading
* Layer flattening

What's not working:

* Colour depths other than 32-bit RGBA
* Blend ops other than default
* Data region lengths larger than max 32-bit signed int
* NRBF class records with no type information

What could be better:

* Documentation
* Example files
* Visibility of many classes is wider than necessary
