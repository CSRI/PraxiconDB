Praxicon DB
===========

The PRAXICON database is a 3rd normal form database that stores information 
on embodied concepts. The design of the database ensures that any application 
that uses the PRAXICON knowledge base and its tools will have easy access to 
the stored data, with minimum overhead. To this end, we have included some 
features that are not currently used by the POETICON project applications, 
but will be useful for future extensions of the PRAXICON and use in different 
applications (e.g. in machine translation).

The core entity of the database is the Concept table. It connects 
concepts/words of different levels of abstraction to their sensorimotor and 
visual instantiations. Each concept can have a motoric and/or visual 
representation. Also each Concept can be member of some relations. These 
relations can be described as a triplet of the following form: 
Concept A – Relation – Concept B, 
which means that Concept A is related with some relation to Concept B. 
These relations are always bidirectional, so the aforementioned triplet 
also stands for Concept B is related (with the reverse relation) with Concept A.

These triplets are the simplest form of a relation. Relations can be combined 
to Relations Chains in order to form more complex relations. Relations Chains 
can further be combined to Intersections of Relations Chains and these 
Intersections can form Unions of Intersections. Justification of these complex 
relations can be found further in the document. Each Concept can have one 
or more Unions of Intersections.


Getting the Software
--------------------

Currently, you can get the PraxiconDB software from the local git repository:

git clone git@192.168.1.200:dmavroeidis/praxicondb.git


Installation and Usage
----------------------

For installation and usage instructions please refer to the documentation in the
manuals under `docs/`.
