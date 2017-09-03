# ba_edtEvaluation

This project is the implementation of the poster contribution

    "Binary Vector based Propositionalization Strategy for Multivalued Relations in Linked Data"

for the ISWC2017 conference.

In order to reproduce the results we like to share this code.
The two main classes can be found under the following path

    EdtEvaluation/src/test/java/de/uni/due/paluno/dt/linkeddata/expansion

For expanding the rdf graphs the Class 
  
    Expanding.java 
 
is used. This class is supposed to be run first. It produces various features. We pay special attention to multi-valued attribues.
Those are then further processed into a binary vector reprenstaion.
 
The main step of transforming the multivalued attributes into a binary vector is handled by
 
    MuliValueFormatter.java

For loading the DBPedia entries Ristoski's and Paulheim's code was used. It is referenced in their paper

    "RDF2Vec: RDF Graph Embeddings for Data Mining"

and can be found here

    http://data.dws.informatik.uni-mannheim.de/rdf2vec/
