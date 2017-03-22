/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.*;
import gr.csri.poeticon.praxicon.db.dao.implSQL.*;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation;
import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationSet;
import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


/**
 *
 * @author dmavroeidis
 * edit: Lorenzo Gregori
 * -- based on neo4j 3.1.2
 * -- representation of RelationSet through Nodes connected with each Relation start node (RS_LEFT) and Relation end node (RS_RIGHT)
 */
public class CreateNeo4JDB {

    private static final String DB_PATH =
            "misc/graph-db/praxicon.graph.db";

    String myString;
    GraphDatabaseService graphDb;
    // A list of nodes to hold concepts
    List<Node> conceptNodes;
    List<Long> conceptIds;
    // A temporary node
    Node conceptNode;
    // A list of vertices to hold relations
    List<Relationship> relationVertices;
    // A temporary edge
    Relationship relationEdge;

    public static void main(final String[] args) {
        CreateNeo4JDB myNeoInstance = new CreateNeo4JDB();
        myNeoInstance.dropDb();
        myNeoInstance.createGraph();
        myNeoInstance.shutDown();
        System.exit(0);
    }
    
    private void dropDb()
    {
        File f = new File(DB_PATH);
        
        if (f.exists() && f.isDirectory())
        {
            try {
                FileUtils.deleteDirectory(f);
            } catch (IOException ex) {
                Logger.getLogger(CreateNeo4JDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void shutDown() {
        graphDb.shutdown();
        System.out.println("graphDB shut down.");
    }

    private void createGraph()
    {
        // Create graph
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
        Transaction tx = graphDb.beginTx();

        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        LanguageRepresentationDao lrDao = new LanguageRepresentationDaoImpl();
        MotoricRepresentationDao mrDao = new MotoricRepresentationDaoImpl();
        VisualRepresentationDao vrDao = new VisualRepresentationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationSetDao rsDao = new RelationSetDaoImpl();
        RelationTypeDao rtDao = new RelationTypeDaoImpl();
        
        System.out.println();
        System.out.println("---- MySQL ----");
        
        System.out.print("Downloading Concepts... ");
        List<Concept> concepts = cDao.getAllConcepts();
        System.out.println("" + concepts.size());
        
        System.out.print("Downloading LanguageRepresentations... ");
        List<LanguageRepresentation> langRepr = lrDao.findAll();
        System.out.println("" + langRepr.size());
        
        System.out.print("Downloading VisualRepresentations... ");
        List<VisualRepresentation> visRepr = vrDao.findAll();
        System.out.println("" + visRepr.size());
        
        System.out.print("Downloading MotoricRepresentations... ");
        List<MotoricRepresentation> motRepr = mrDao.findAll();
        System.out.println("" + motRepr.size());
        
        System.out.print("Downloading Relations... ");
        List<Relation> rels = rDao.findAll();
        System.out.println("" + rels.size());
        
        System.out.print("Downloading RelationSets... ");
        List<RelationSet> relsets = rsDao.findAll();
        System.out.println("" + relsets.size());
        
        System.out.println();
        System.out.println("---- Neo4J ----");
        try 
        {
            System.out.print("Uploading LanguageRepresentations... ");
            for (LanguageRepresentation repr : langRepr)
            {
                conceptNode = graphDb.createNode();
                conceptNode.setProperty("id", repr.getId());
                conceptNode.setProperty("text", repr.getText());
                conceptNode.setProperty("lang", repr.getLanguage().toString());
                conceptNode.setProperty("pos", repr.getPartOfSpeech().toString());
                conceptNode.addLabel(Label.label("LanguageRepresentation"));
            }
            System.out.println("OK");
            System.out.print("Uploading VisualRepresentations... ");
            for (VisualRepresentation repr : visRepr)
            {
                conceptNode = graphDb.createNode();
                conceptNode.setProperty("id", repr.getId());
                conceptNode.setProperty("name", repr.getName());
                conceptNode.setProperty("mediaType", repr.getMediaType().toString());
                conceptNode.setProperty("source", repr.getSource());
                conceptNode.setProperty("uri", repr.getUri().toString());
                conceptNode.addLabel(Label.label("VisualRepresentation"));
            }
            System.out.println("OK");
            System.out.print("Uploading MotoricRepresentations... ");
            for (MotoricRepresentation repr : motRepr)
            {
                conceptNode = graphDb.createNode();
                conceptNode.setProperty("id", repr.getId());
                conceptNode.setProperty("performingAgent", repr.getPerformingAgent().toString());
                conceptNode.setProperty("source", repr.getSource());
                conceptNode.setProperty("uri", repr.getUri().toString());
                conceptNode.addLabel(Label.label("MotoricRepresentation"));
            }
            System.out.println("OK");
            System.out.print("Uploading Concepts with Representations... ");
            int i = 1; int max = concepts.size(); int prevPerc = 0;
            for (Concept concept : concepts)
            {
                conceptNode = graphDb.createNode();
                conceptNode.setProperty("id", concept.getId());
                conceptNode.setProperty("name", concept.getName());
                conceptNode.setProperty("conceptType", concept.getConceptType().toString());
                conceptNode.setProperty("conceptExternalSourceId", concept.getExternalSourceId());
                conceptNode.setProperty("conceptPragmaticStatus", concept.getPragmaticStatus().toString());
                conceptNode.setProperty("conceptSpecificityLevel", concept.getSpecificityLevel().toString());
                conceptNode.setProperty("conceptUniqueInstance", concept.getUniqueInstance().toString());
                conceptNode.setProperty("conceptSource", concept.getSource());
                conceptNode.setProperty("conceptStatus", concept.getStatus().toString());
                conceptNode.addLabel(Label.label("Concept")); 
                List<LanguageRepresentation> lr = concept.getLanguageRepresentations();
                for (LanguageRepresentation lrx : lr)
                {
                    Node n = graphDb.findNodes(Label.label("LanguageRepresentation"), "id", lrx.getId()).next();
                    conceptNode.createRelationshipTo(n, RelationshipType.withName("LANGUAGE_REPR"));
                }
                List<VisualRepresentation> vr = concept.getVisualRepresentations();
                for (VisualRepresentation vrx : vr)
                {
                    Node n = graphDb.findNodes(Label.label("VisualRepresentation"), "id", vrx.getId()).next();
                    conceptNode.createRelationshipTo(n, RelationshipType.withName("VISUAL_REPR"));
                }
                List<MotoricRepresentation> mr = concept.getMotoricRepresentations();
                for (MotoricRepresentation mrx : mr)
                {
                    Node n = graphDb.findNodes(Label.label("MotoricRepresentation"), "id", mrx.getId()).next();
                    conceptNode.createRelationshipTo(n, RelationshipType.withName("MOTORIC_REPR"));
                }
                
                int perc = new Double((i * 1.0 / max) * 100).intValue();
                if (perc > prevPerc && perc % 5 == 0) { prevPerc = perc; System.out.print(perc + "% ");}
                i++;
            }
            System.out.println(" OK");
            System.out.print("Uploading RelationSets... ");
            i = 1; max = relsets.size(); prevPerc = 0;
            for (RelationSet rset : relsets)
            {
                conceptNode = graphDb.createNode();
                conceptNode.setProperty("id", rset.getId());
                conceptNode.setProperty("name", rset.getName());
                conceptNode.addLabel(Label.label("RelationSet"));
                List<Relation> rxs = rset.getRelationsSet();
                int ri = 0;
                for (Relation rx : rxs)
                {
                    RelationArgument larg = rx.getLeftArgument();
                    RelationArgument rarg = rx.getRightArgument();
                    Long lid, rid;
                    Node nl = null, nr = null;
                    if (larg.isConcept())
                    {
                        lid = larg.getConcept().getId();
                        nl = graphDb.findNodes(Label.label("Concept"), "id", lid).next();
                    }
                    else if (larg.isRelationSet())
                    {
                        lid = larg.getRelationSet().getId();
                        nl = graphDb.findNodes(Label.label("RelationSet"), "id", lid).next();
                    }
                    if (rarg.isConcept())
                    {
                        rid = rarg.getConcept().getId();
                        nr = graphDb.findNodes(Label.label("Concept"), "id", rid).next();
                    }
                    else if (rarg.isRelationSet())
                    {
                        rid = rarg.getRelationSet().getId();
                        nr = graphDb.findNodes(Label.label("RelationSet"), "id", rid).next();
                    }
                    if (nl != null && nr != null)
                    {
                        Relationship rsx = conceptNode.createRelationshipTo(nl, RelationshipType.withName("RS_LEFT"));
                        rsx.setProperty("n", ri);
                        Relationship rsx2 = conceptNode.createRelationshipTo(nr, RelationshipType.withName("RS_RIGHT"));
                        rsx2.setProperty("n", ri);
                        ri++;
                    }
                }
                int perc = new Double((i * 1.0 / max) * 100).intValue();
                if (perc > prevPerc && perc % 5 == 0) { prevPerc = perc; System.out.print(perc + "% ");}
                i++;
            }
            System.out.println(" OK");
            System.out.print("Uploading Relations... ");
            i = 1; max = rels.size(); prevPerc = 0;
            for (Relation rel : rels)
            {
                RelationArgument larg = rel.getLeftArgument();
                RelationArgument rarg = rel.getRightArgument();
                Long lid, rid;
                Node nl = null, nr = null;
                if (larg.isConcept())
                {
                    lid = larg.getConcept().getId();
                    nl = graphDb.findNodes(Label.label("Concept"), "id", lid).next();
                }
                else if (larg.isRelationSet())
                {
                    lid = larg.getRelationSet().getId();
                    nl = graphDb.findNodes(Label.label("RelationSet"), "id", lid).next();
                }
                if (rarg.isConcept())
                {
                    rid = rarg.getConcept().getId();
                    nr = graphDb.findNodes(Label.label("Concept"), "id", rid).next();
                }
                else if (rarg.isRelationSet())
                {
                    rid = rarg.getRelationSet().getId();
                    nr = graphDb.findNodes(Label.label("RelationSet"), "id", rid).next();
                }
                if (nl != null && nr != null)
                {
                    Relationship rx = nl.createRelationshipTo(nr, RelationshipType.withName(rel.getRelationType().getForwardNameString()));
                    rx.setProperty("linguisticallySupported", rel.getLinguisticallySupported().toString());
                }
                
                int perc = new Double((i * 1.0 / max) * 100).intValue();
                if (perc > prevPerc && perc % 5 == 0) { prevPerc = perc; System.out.print(perc + "% ");}
                i++;
            }
            System.out.println(" OK");
        }
        catch (Error e)
        {
            System.out.println("Error occured: ");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
            
        tx.success();

        if (cDao.getEntityManager().isOpen()) {
            cDao.close();
        }
        if (rDao.getEntityManager().isOpen()) {
            rDao.close();
        }
        if (lrDao.getEntityManager().isOpen()) {
            lrDao.close();
        }
        if (vrDao.getEntityManager().isOpen()) {
            vrDao.close();
        }
        if (mrDao.getEntityManager().isOpen()) {
            mrDao.close();
        }
        if (rDao.getEntityManager().isOpen()) {
            rDao.close();
        }
        if (raDao.getEntityManager().isOpen()) {
            raDao.close();
        }
        if (rsDao.getEntityManager().isOpen()) {
            rsDao.close();
        }
        if (rtDao.getEntityManager().isOpen()) {
            rtDao.close();
        }
        for (Frame frame : Frame.getFrames()) {
            frame.dispose();
        }
        tx.close();
    }
}
