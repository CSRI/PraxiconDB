/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author dmavroeidis
 */
public class CreateNeo4JDB {

    private static final String DB_PATH =
            "/home/dmavroeidis/graph_dbs/praxicon_graph_db";

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
        myNeoInstance.createGraph();
        //myNeoInstance.removeData();
        myNeoInstance.shutDown();

        System.exit(0);
    }

    private void removeData() {
//        Transaction tx = graphDb.beginTx();
//        try {
//            myFirstNode.getSingleRelationship(RelTypes.TYPE_TOKEN, Direction.OUTGOING);
//            System.out.println("Removing nodes...");
//            myFirstNode.delete();
//            mySecondNode.delete();
//            tx.success();
//        } finally {
//            tx.close();
//        }
    }

    private void shutDown() {
        graphDb.shutdown();
        System.out.println("graphDB shut down.");
    }

    private static enum RelTypes implements RelationshipType {

        TYPE_TOKEN
    }

    private void createGraph() {

        // Create graph
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        Transaction tx = graphDb.beginTx();
        Label conceptLabel = DynamicLabel.label("Concept");

        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        // Get concepts from the database
        long startTime = System.nanoTime();
        List<Concept> concepts = cDao.findAllConcepts();
        long endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting concepts in ");
        System.out.print((endTime - startTime) / (1000000000));
        System.out.println(" seconds!");

        // Get relations from the database
        startTime = System.nanoTime();
        //List<Relation> relationsTypeToken = rDao.getAllRelations();
        List<Relation> relationsTypeToken = rDao.getRelationsByRelationType(
                RelationType.RelationNameForward.TYPE_TOKEN);
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting relations in ");
        System.out.print((endTime - startTime) / (1000000000));
        System.out.println(" seconds!");

        try {

            // Add concepts (vertices) to the graph database
            startTime = System.nanoTime();
            for (Concept concept : concepts) {

                conceptNode = graphDb.createNode();
                conceptNode.setProperty("conceptType", concept.getConceptType().
                        toString());
                conceptNode.setProperty("conceptExternalSourceId", concept.
                        getExternalSourceId());
                conceptNode.setProperty("conceptId", concept.getId());
                if (concept.getOntologicalDomain() != null) {
                    conceptNode.setProperty("conceptOntologicalDomain", concept.
                            getOntologicalDomain());
                }
                conceptNode.setProperty("conceptPragmaticStatus", concept.
                        getPragmaticStatus().toString());
                conceptNode.setProperty("conceptSpecificityLevel", concept.
                        getSpecificityLevel().toString());
                conceptNode.setProperty("conceptUniqueInstance", concept.
                        getUniqueInstance().toString());
                conceptNode.setProperty("conceptSource", concept.getSource());
                conceptNode.setProperty("conceptStatus", concept.getStatus().
                        toString());

                conceptNode.addLabel(conceptLabel);

//                conceptNodes.add(conceptNode);
//                conceptIds.add(conceptNode.getId());
            }
            endTime = System.nanoTime();
            System.out.print("\n\n\nFinished adding concepts in ");
            System.out.print((endTime - startTime) / 1000000000);
            System.out.println(" seconds!");

//            System.out.println(graphDb.getNodeById(0).getLabels());
//            System.out.println(graphDb.getNodeById(0).getProperty(
//                    "conceptExternalSourceId"));
//            System.out.println(graphDb.getNodeById(1).getLabels());
//            System.out.println(graphDb.getNodeById(1).getProperty(
//                    "conceptExternalSourceId"));
            // Add relations (edges) to the graph database
            startTime = System.nanoTime();
            for (Relation relation : relationsTypeToken) {
                // Get left concept
                Concept leftConcept = new Concept();
                if (relation.getLeftArgument().isConcept()) {
                    leftConcept = relation.getLeftArgument().getConcept();
                }

                // Get right concept
                Concept rightConcept = new Concept();
                if (relation.getRightArgument().isConcept()) {
                    rightConcept = relation.getRightArgument().getConcept();
                }

                Node leftNode;
                Node rightNode;

                leftNode = graphDb.findNodesByLabelAndProperty(conceptLabel,
                        "conceptExternalSourceId", leftConcept.
                        getExternalSourceId()).iterator().next();
                rightNode = graphDb.findNodesByLabelAndProperty(conceptLabel,
                        "conceptExternalSourceId", rightConcept.
                        getExternalSourceId()).iterator().next();

//                System.out.println(leftNode.getProperty(
//                        "conceptExternalSourceId"));
//                System.out.println(rightNode.getProperty(
//                        "conceptExternalSourceId"));
                relationEdge = leftNode.createRelationshipTo(rightNode,
                        RelTypes.TYPE_TOKEN);

                relationEdge.setProperty("relationship-type", "is_a");

//                myString = (rightNode.getProperty("conceptExternalSourceId").
//                        toString()) +
//                        " " + (relationEdge.getProperty("relationship-type").
//                        toString()) +
//                        " " + (leftNode.getProperty("conceptExternalSourceId").
//                        toString());
//                System.out.println(myString);
            }
            endTime = System.nanoTime();
            System.out.print("\n\n\nFinished adding edges in ");
            System.out.print((endTime - startTime) / 1000000000);
            System.out.println(" seconds!\n\n\n");

            tx.success();

        } catch (Error e) {
            System.out.println("Error occured: ");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            tx.close();
            System.exit(1);
        }

        // Now insert all BL relations.
        findBLRelations(graphDb, concepts);
        if (cDao.getEntityManager().isOpen()) {
            cDao.close();
        }

        if (rDao.getEntityManager().isOpen()) {
            rDao.close();
        }
        for (Frame frame : Frame.getFrames()) {
            frame.dispose();
        }
    }

    public static void findBLRelations(GraphDatabaseService graphDb,
            List<Concept> concepts) {

        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        List<Map.Entry<Concept, Concept>> newBasicLevelConnections =
                new ArrayList<>();

        // Find all leaves.
        
        String output = "";
//        for (Path position : graphDb.traversalDescription().depthFirst().relationships(RelTypes.TYPE_TOKEN).traverse(node)){
//            output += position;
//            System.out.println(output);
//        
//            
//        }
        for (Node n : GlobalGraphOperations.at(graphDb).getAllNodes()) {
            System.out.println(n.getProperty("conceptExternalSourceId")+": "+ IteratorUtil.count(n.getRelationships(Direction.INCOMING))); 
        }









//        BreadthFirstIterator graphIter = new BreadthFirstIterator(conceptGraph);
//        List<Concept> islands = new ArrayList<>();
//        List<Concept> leaves = new ArrayList<>();
//        List<Concept> roots = new ArrayList<>();
//        List<Concept> internals = new ArrayList<>();
//        int maxOutDegree = 0;
//        Concept maxOutDegreeConcept = new Concept();
//        Concept maxInDegreeConcept = new Concept();
//        int maxInDegree = 0;
//        int count0BLPaths = 0;
//        int count1BLPaths = 0;
//        int count2BLPaths = 0;
//        int count3BLPaths = 0;
//        int countPaths = 0;
//
//        // Get leaves and roots
//        while (graphIter.hasNext()) {
//            Object o = graphIter.next();
//            Concept c = (Concept)o;
//            int outDegree = conceptGraph.outDegreeOf(c);
//            if (outDegree > maxOutDegree) {
//                maxOutDegree = outDegree;
//                maxOutDegreeConcept = c;
//            }
//            int inDegree = conceptGraph.inDegreeOf(c);
//            if (inDegree > maxInDegree) {
//                maxInDegree = inDegree;
//                maxInDegreeConcept = c;
//            }
//
//            if (outDegree == 0 && inDegree == 0) {
//                islands.add(c);
////                System.out.print("Island: " + c + " " + c.getSpecificityLevel());
////                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
////                        outDegree);
//            } else if (outDegree == 0 && inDegree > 0) {
//                leaves.add(c);
////                System.out.print("Leaf: " + c + " " + c.getSpecificityLevel());
////                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
////                        outDegree);
//            } else if (outDegree > 0 && inDegree == 0) {
//                roots.add(c);
////                System.out.print("Root: " + c + " " + c.getSpecificityLevel());
////                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
////                        outDegree);
//            } else {
//                internals.add(c);
////                System.out.print("Internal: " + c + " " + c.
////                        getSpecificityLevel());
////                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
////                        outDegree);
//            }
//
//        }
//
//        System.out.println("Totals: ");
//        System.out.println("Islands: " + islands.size());
//        System.out.println("Roots: " + roots.size());
//        System.out.println("Leaves: " + leaves.size());
//        System.out.println("Internals: " + internals.size());
//        System.out.println("MaxOutDegree: " + maxOutDegree + " for concept: " +
//                maxOutDegreeConcept);
//        System.out.println("MaxInDegree: " + maxInDegree + " for concept: " +
//                maxInDegreeConcept);
//
//        long counter = 0;
//        long rootCount = roots.size();
//        long conceptCountPer100 = rootCount / 100;
//        long countCounts = 1;
//
//        List<DijkstraShortestPath> allShortestPaths = new ArrayList<>();
//
//        // For each leaf, we find the path to each root. There will normally be
//        // only one path for each pair.
//        long startTime = System.nanoTime();
////        for (Concept leaf : leaves) {
//        for (Concept root : roots) {
//            counter += 1;
//            if (conceptCountPer100 * countCounts == counter) {
//                System.out.println(countCounts + "%");
//                countCounts += 1;
//            }
//            KShortestPaths shortestPathsBF = new KShortestPaths(
//                    conceptGraph, root, 4);
//
//            for (Concept leaf : leaves) {
//                //for (Concept root : roots) {
//
//                DijkstraShortestPath shortestPathDijkstra =
//                        new DijkstraShortestPath(conceptGraph, root, leaf);
//
////                Set<GraphPath> edgeSet = conceptGraph.getAllEdges(root, leaf);
////                System.out.print("All paths from ");
////                System.out.print(root);
////                System.out.print(" to ");
////                System.out.println(leaf + ":");
////                for (GraphPath item : edgeSet) {
////                    System.out.println(item.getEdgeList());
////                }
////                
////                FloydWarshallShortestPaths shortestPathsFW = new FloydWarshallShortestPaths(conceptGraph);
////                int allPaths=shortestPathsFW.getShortestPathsCount();
////                System.out.print("Count of all paths: ");
////                System.out.println(allPaths);
//                // If path exists
//                if (shortestPathDijkstra.getPathLength() !=
//                        Double.POSITIVE_INFINITY) {
//                    countPaths++;
//
//                    List shortestPathBFList = shortestPathsBF.getPaths(leaf);
//
//                    System.out.print("All paths from ");
//                    System.out.print(root);
//                    System.out.print(" to ");
//                    System.out.println(leaf + ":");
//                    for (Object pathBF : shortestPathBFList) {
//                        System.out.println(pathBF);
//                    }
//
//                    //allShortestPaths.add(shortestPathDijkstra);
//                    GraphPath path = shortestPathDijkstra.getPath();
//                    List<Concept> pathGraph = Graphs.getPathVertexList(path);
//
//                    // Remove first vertex, to avoid connecting roots to themselves
//                    List<Concept> blConcepts = new ArrayList<>();
//                    boolean blFound = false;
//                    // Get basic level concepts in the path
//                    for (Object vertex : pathGraph) {
//                        Concept concept = (Concept)vertex;
//                        if (concept.getSpecificityLevel() ==
//                                Concept.SpecificityLevel.BASIC_LEVEL ||
//                                concept.getSpecificityLevel() ==
//                                Concept.SpecificityLevel.BASIC_LEVEL_EXTENDED) {
//                            blConcepts.add(concept);
//                            blFound = true;
//                        }
//                    }
//
//                    // Start adding relations
//                    if (blFound) {
//                        // Reset the switch to use it in this loop
//                        blFound = false;
//                        // For each BL concept in the path
//                        for (Concept blConcept : blConcepts) {
//                            // For each concept in the path 
//                            // starting from the root
//                            for (Object vertex : pathGraph) {
//                                Concept concept = (Concept)vertex;
//                                // If the concept is not the BL concept 
//                                // under consideration
//                                if (!blConcept.equals(concept)) {
//                                    // If the concept is not BL 
//                                    // (this check is needed in case we have 
//                                    // more than 1 BLs in the path).
//                                    if (concept.getSpecificityLevel() ==
//                                            Concept.SpecificityLevel.SUBORDINATE ||
//                                            concept.getSpecificityLevel() ==
//                                            Concept.SpecificityLevel.SUPERORDINATE ||
//                                            concept.getSpecificityLevel() ==
//                                            Concept.SpecificityLevel.UNKNOWN) {
//                                        // Get relation arguments of concepts
//                                        RelationArgument relationArgument1 =
//                                                raDao.
//                                                getRelationArgumentByConcept(
//                                                        concept);
//                                        RelationArgument relationArgument2 =
//                                                raDao.
//                                                getRelationArgumentByConcept(
//                                                        blConcept);
//
//                                        // Create new relation
//                                        Relation newRelation = new Relation();
//                                        RelationType newRelationType =
//                                                new RelationType(
//                                                        RelationType.RelationNameForward.TYPE_TOKEN,
//                                                        RelationType.RelationNameBackward.TOKEN_TYPE);
//                                        newRelation.setLinguisticSupport(
//                                                Relation.LinguisticallySupported.UNKNOWN);
//                                        newRelation.setType(newRelationType);
//
//                                        if (!blFound) {
//                                            newRelation.setLeftArgument(
//                                                    relationArgument1);
//                                            newRelation.setRightArgument(
//                                                    relationArgument2);
//                                            //System.out.println("DOWN");
//
//                                        } else if (blFound) {
//                                            // if the basic level has been found in path,
//                                            // reverse the direction of relation.
//                                            newRelation.setLeftArgument(
//                                                    relationArgument2);
//                                            newRelation.setRightArgument(
//                                                    relationArgument1);
//                                            //System.out.println("UP");
//                                        }
//
//                                        // If the two relation arguments are not related, 
//                                        // add the relation.
//                                        if (!rDao.areRelated(relationArgument1,
//                                                relationArgument2)) {
////                                            System.out.
////                                                    print("Relation between ");
////                                            System.out.print(relationArgument1.
////                                                    getConcept());
////                                            System.out.print(" and ");
////                                            System.out.print(relationArgument2.
////                                                    getConcept());
////                                            System.out.println(
////                                                    " doesn't exist and will be created.");
//                                            //rDao.persist(newRelation);
//                                        } else {
////                                            System.out.
////                                                    print("Relation between ");
////                                            System.out.print(relationArgument1.
////                                                    getConcept());
////                                            System.out.print(" and ");
////                                            System.out.print(relationArgument2.
////                                                    getConcept());
////                                            System.out.println(
////                                                    " already exists.");
//                                        }
//                                    }
//                                } else if (blConcept.equals(concept)) {
//                                    blFound = true;
//                                }
//                            }
//                        }
//                    }
//
//                    if (blConcepts.size() != 1) {
////                        System.out.println(
////                                "This path has " + blConcepts.size() +
////                                " basic level concepts.");
//                        if (blConcepts.size() == 0) {
//                            count0BLPaths++;
//                        } else if (blConcepts.size() == 2) {
//                            count2BLPaths++;
//                        } else if (blConcepts.size() >= 3) {
//                            count3BLPaths++;
//                        }
//
//                    } else {
//                        count1BLPaths++;
//                    }
//                }
//            }
//        }
//
//        long endTime = System.nanoTime();
//        System.out.print("\n\n\nFinished adding relations to the database in ");
//        System.out.print((endTime - startTime) / (60000000000D));
//        System.out.println(" minutes!\n\n\n");
//        System.out.println("\n\n\n\n\nFinished execution!\n\n\n");
//        System.out.println("There are " + countPaths + " paths.");
//        System.out.println("We found " + count0BLPaths + " paths with no BLs.");
//        System.out.println("We found " + count1BLPaths + " paths with 1 BL.");
//        System.out.println("We found " + count2BLPaths + " paths with 2 BLs.");
//        System.out.println("We found " + count3BLPaths +
//                " paths with 3 or more BLs.");
    }

}
