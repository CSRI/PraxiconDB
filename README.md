PraxiconDB
==========
The PRAXICON is a conceptual knowledge base in which concepts have both symbolic
(e.g. natural language) and corresponding sensorimotor representations, while 
associations among them are pragmatic in nature and recursive. The resource has
been developed to allow artificial agents/systems:
- to tie concepts/words of different levels of abstraction to their 
    sensorimotor instantiations (catering thus for disambiguation), and
- to untie sensorimotor representations from their physical specificities
    correlating them to conceptual structures of different levels of abstraction
    (catering thus for intentionality indication).

The PRAXICON database is a 3rd normal form database that stores information on
embodied concepts. The design of the database ensures that any application that
uses the PRAXICON knowledge base and its tools will have easy access to the 
stored data, with minimum overhead. To this end, we have included some features
that are not currently used by the POETICON project applications, but will be 
useful for future extensions of the PRAXICON and use in different applications
(e.g. in machine translation).

Description
-----------
The core entity of the database is the Concept table. It connects concepts/words
of different levels of abstraction to their sensorimotor and visual 
instantiations. Each concept has a linguistic representation and can 
optionally have a motoric and/or a visual representation.

Each Concept can be member of one or more Relations. Relations usually comprise
of a triplet having the following form: "Concept A – Relation – Concept B".
This means that Concept A (left relation argument) is related with some 
Relation to Concept B. These Relations are always bidirectional, so the 
aforementioned triplet also stands for Concept B is related (with the reverse 
relation) with Concept A. These triplets are the simplest form of a Relation.

This simple form of a Relation cannot by itself express the perplexity of 
the world we live in. We need to be able to combine simple Relations into more 
complex structures. These are called Relation Sets. A Relation Set can contain 
one or more Relations bundled together as one entity.

In addition, a Relation Set can be at the left or right side of a Relation, thus 
creating a recursive schema. More details on how Relations interact with 
Relation Sets and Concepts can be found in the documentation.

Getting the Software
--------------------
There are two ways to get the PraxiconDB:

1. Get the latest stable release from https://github.com/CSRI/PraxiconDB/tags.

2. Get the latest development version by creating a local fork of the project 
on your Git account and clone it locally (https://github.com/CSRI/PraxiconDB.git).

Installation and Usage
----------------------
For installation and usage instructions please refer to the documentation 
in the manuals under `docs/`.

Reporting Issues
----------------
If you spot any bugs, please report them to the GitHub's issue tracker:
https://github.com/CSRI/PraxiconDB/issues.

Development
-----------
If you would like to contribute to the project, just fork it, and send 
pull requests with your changes. If you require assistance, please 
contact the authors.
