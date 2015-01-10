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
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl.Direction;
import static gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl.Direction.DOWN;
import static gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl.Direction.UP;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 *
 * @author dmavroeidis
 */
public class CreateNewBLRelations {

    public static void main(String args[]) {

        ConceptGraph();
        //ConceptRecursive();
//        ConceptNeo4j();
        //testBug();
        System.exit(0);

    }

    public static void ConceptRecursive() {
        // For each concept get its basic level and store it in relations
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        long startTime = System.nanoTime();

        // Get all concepts
        List<Concept> concepts = cDao.findAllConcepts();
        long endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting concepts in ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds!");

        // Set up percentage counter
        int counter = 0;
        int conceptCount = concepts.size();
        int conceptCountPer100 = conceptCount / 100;
        int countCounts = 1;

        startTime = System.nanoTime();
        // For each concept, get its basic level
        for (Concept concept : concepts) {
            List<Map.Entry<Concept, ConceptDaoImpl.Direction>> basicLevelConcepts =
                    cDao.getBasicLevelConcepts(concept);

            counter += 1;
            if (conceptCountPer100 * countCounts == counter) {
                System.out.println(countCounts + "%");
                countCounts += 1;
            }

            for (Map.Entry<Concept, ConceptDaoImpl.Direction> item
                    : basicLevelConcepts) {
                if (!(item.getValue() == ConceptDaoImpl.Direction.NONE)) {
                    Relation relation1 = new Relation();
                    RelationType relationType1 = new RelationType();
                    relationType1.setForwardName(
                            RelationType.RelationNameForward.TYPE_TOKEN);
                    relationType1.setBackwardName(
                            RelationType.RelationNameBackward.TOKEN_TYPE);
                    relation1.setType(relationType1);
                    // This is the original concept
                    RelationArgument relationArgument1 = raDao.
                            getRelationArgumentByConcept(concept);
                    // This is the BL concept
                    RelationArgument relationArgument2 = raDao.
                            getRelationArgumentByConcept(item.getKey());
                    if (item.getValue() == DOWN) {
                        relation1.setLeftArgument(relationArgument1);
                        relation1.setRightArgument(relationArgument2);
                    } else if (item.getValue() == UP) {
                        relation1.setLeftArgument(relationArgument2);
                        relation1.setRightArgument(relationArgument1);
                    }
                    relation1.setLinguisticSupport(
                            Relation.LinguisticallySupported.UNKNOWN);
                    if (!rDao.areRelated(relationArgument1, relationArgument2)) {
                        rDao.persist(relation1);
                    }
                }
            }
        }
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished adding relations in ");
        System.out.print((endTime - startTime) / 60000000000L);
        System.out.println(" minutes!");
    }

    public static void ConceptGraph() {

        //DirectedAcyclicGraph conceptGraph = new  DirectedAcyclicGraph(null);
        DirectedAcyclicGraph<Concept, DefaultEdge> conceptGraph =
                new DirectedAcyclicGraph<>(DefaultEdge.class);

        // Get all non-BL concepts
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        // Get concepts from the database
        long startTime = System.nanoTime();
        List<Concept> concepts = cDao.findAllConcepts();
        long endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting concepts in ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds!");

        // Get relations from the database
        startTime = System.nanoTime();
        //List<Relation> relationsTypeToken = rDao.getAllRelations();
        List<Relation> relationsTypeToken = rDao.getRelationsByRelationType(
                RelationType.RelationNameForward.TYPE_TOKEN);
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting relations in ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds!");

        // Add concepts (vertices) to the graph
        startTime = System.nanoTime();
        for (Concept concept : concepts) {
            conceptGraph.addVertex(concept);
        }
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished adding concepts in ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds!");

        // Add relations (edges) to the graph
        startTime = System.nanoTime();
        for (Relation relation : relationsTypeToken) {
            // Get left concept
            Concept leftConcept = new Concept();
            if (relation.getLeftArgument().isConcept()) {
                leftConcept = relation.getLeftArgument().getConcept();
                //System.out.println(leftConcept);
            }

            Concept rightConcept = new Concept();
            if (relation.getRightArgument().isConcept()) {
                rightConcept = relation.getRightArgument().getConcept();
                //System.out.println(rightConcept);
            }

            //System.out.println(relation);
            // Add edges with 2 concepts
            conceptGraph.addEdge(leftConcept, rightConcept);
        }
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished adding edges in ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds!\n\n\n");

        // Now insert all BL relations.
        insertBLRelations(conceptGraph, concepts, DOWN);
//        insertBLRelations(reverseConceptGraph, concepts, UP);
//        insertBLRelations(reverseConceptGraph, concepts, UP);

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

    public static void insertBLRelations(DirectedGraph conceptGraph,
            List<Concept> concepts, Direction direction) {

        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        List<Entry<Concept, Concept>> newBasicLevelConnections =
                new ArrayList<>();

        // Get the reverse of concept graph.
        // This is necessary so as to calculate both downward (conceptGraph)
        // and upward (reverseConceptGraph) basic level concepts.
//        EdgeReversedGraph reverseConceptGraph = new EdgeReversedGraph(
//                conceptGraph);
        ConnectivityInspector ci = new ConnectivityInspector(conceptGraph);

        // Find all leaves.
        DepthFirstIterator graphIter = new DepthFirstIterator(conceptGraph);
        List<Concept> islands = new ArrayList<>();
        List<Concept> leaves = new ArrayList<>();
        List<Concept> roots = new ArrayList<>();
        List<Concept> internals = new ArrayList<>();
        int maxOutDegree = 0;
        Concept maxOutDegreeConcept = new Concept();
        Concept maxInDegreeConcept = new Concept();
        int maxInDegree = 0;
        int count0BLPaths = 0;
        int count1BLPaths = 0;
        int count2BLPaths = 0;
        int count3BLPaths = 0;
        int countPaths = 0;

        // Get leaves and roots
        while (graphIter.hasNext()) {
            Object o = graphIter.next();
            Concept c = (Concept)o;
            int outDegree = conceptGraph.outDegreeOf(c);
            if (outDegree > maxOutDegree) {
                maxOutDegree = outDegree;
                maxOutDegreeConcept = c;
            }
            int inDegree = conceptGraph.inDegreeOf(c);
            if (inDegree > maxInDegree) {
                maxInDegree = inDegree;
                maxInDegreeConcept = c;
            }

            if (outDegree == 0 && inDegree == 0) {
                islands.add(c);
//                System.out.print("Island: " + c + " " + c.getSpecificityLevel());
//                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
//                        outDegree);
            } else if (outDegree == 0 && inDegree > 0) {
                leaves.add(c);
//                System.out.print("Leaf: " + c + " " + c.getSpecificityLevel());
//                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
//                        outDegree);
            } else if (outDegree > 0 && inDegree == 0) {
                roots.add(c);
//                System.out.print("Root: " + c + " " + c.getSpecificityLevel());
//                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
//                        outDegree);
            } else {
                internals.add(c);
//                System.out.print("Internal: " + c + " " + c.
//                        getSpecificityLevel());
//                System.out.println(" InDegree: " + inDegree + " OutDegree: " +
//                        outDegree);
            }

        }

        System.out.println("Totals: ");
        System.out.println("Islands: " + islands.size());
        System.out.println("Roots: " + roots.size());
        System.out.println("Leaves: " + leaves.size());
        System.out.println("Internals: " + internals.size());
        System.out.println("MaxOutDegree: " + maxOutDegree + " for concept: " +
                maxOutDegreeConcept);
        System.out.println("MaxInDegree: " + maxInDegree + " for concept: " +
                maxInDegreeConcept);

        long counter = 0;
        long leafCount = leaves.size();
        long conceptCountPer100 = leafCount / 100;
        long countCounts = 1;

        //List<DijkstraShortestPath> allShortestPaths = new ArrayList<>();
        long startTime = System.nanoTime();
        List<List<Concept>> allPaths = new ArrayList<>();
        // For each leaf, we find all paths to each root. 
        for (Concept leaf : leaves) {
//        for (Concept root : roots) {
            counter += 1;
            if (conceptCountPer100 * countCounts == counter) {
                System.out.println(countCounts + "%");
                countCounts += 1;
            }

//            System.out.println("Leaf: " + leaf);
            //System.out.println("Starting getting paths...");
            List<List<Concept>> paths = new ArrayList<>();

            ConnectivityInspector conI = new ConnectivityInspector(conceptGraph);
            for (Concept root : roots) {
                if (conI.pathExists(root, leaf)) {
                    //System.out.println("Root: " + root);
//                    for (List<Concept> item : getAllPaths(conceptGraph, root,
//                            leaf)) {
//                        System.out.println(item);
//                        paths.add(item);
//                    }
                    paths = getAllPaths(conceptGraph, root, leaf);
                    for (List<Concept> item : paths) {
                        System.out.println("Path for pair: " + root + " " +
                                leaf + ": \n" + item);
                    }
                }

            }
//            for (List<Concept>){

//            }
//            //System.out.println("All Paths:");
//            for (List<Concept> item : paths) {
//                System.out.println("Path: " + item);
//            }
            //          for (Concept leaf : leaves) {
//            for (Concept root : roots) {
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
////                    //List shortestPathBFList = shortestPathsBF.getPaths(leaf);
////                    System.out.print("All paths from ");
////                    System.out.print(root);
////                    System.out.print(" to ");
////                    System.out.println(leaf + ":");
////                    for (Object pathBF : shortestPathBFList) {
////                        System.out.println(pathBF);
////                    }
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
        }

        long endTime = System.nanoTime();
        System.out.print("\n\n\nFinished adding relations to the database in ");
        System.out.print((endTime - startTime) / (60000000000D));
        System.out.println(" minutes!\n\n\n");
        System.out.println("\n\n\n\n\nFinished execution!\n\n\n");
        System.out.println("There are " + countPaths + " paths.");
        System.out.println("We found " + count0BLPaths + " paths with no BLs.");
        System.out.println("We found " + count1BLPaths + " paths with 1 BL.");
        System.out.println("We found " + count2BLPaths + " paths with 2 BLs.");
        System.out.println("We found " + count3BLPaths +
                " paths with 3 or more BLs.");
    }

    public static List<List<Concept>> getAllPaths(DirectedGraph conceptGraph,
            Concept root, Concept leaf) {
        List<List<Concept>> finalPathList = new ArrayList<List<Concept>>();
        Stack<List<Concept>> pathStack = new Stack<>();
        List<Concept> path = new ArrayList<>();
        List<Concept> tmpPath = new ArrayList<>();
        List<Concept> nodeList = new ArrayList<>();
        Concept tmpConcept = null;
        Concept adjConcept = null;
        int counter = 0;

        // 1. Add the first node to the path
        path.add(leaf);
        // 2. Add the path to the stack
        pathStack.push(path);
        int index = 0;

        // 3. While the stack is not empty
        while (!pathStack.isEmpty()) {
            // 4. Get the last node of the popped path in the stack
            tmpPath = pathStack.pop();
            System.out.println("\nTEMPORARY PATH:");
            System.out.println(tmpPath);
            index = tmpPath.size() - 1;
            tmpConcept = tmpPath.get(index);
            // Get the adjucent edges & the corresponding iterator
            Set<DefaultEdge> tmpEdges = conceptGraph.incomingEdgesOf(tmpConcept);
            Iterator edgeIterator = tmpEdges.iterator();
            // 5. For each adjacent node
            System.out.println("\nadjacent nodes of " + tmpConcept);
            while (edgeIterator.hasNext()) {
                Object tmpEdge = (DefaultEdge)edgeIterator.next();
                adjConcept = (Concept)conceptGraph.getEdgeSource(tmpEdge);
//                counter++;
                System.out.println("node: " + adjConcept);
                // 6. If the node is not in the path
                if (!tmpPath.contains(adjConcept)) {
                    // 7. If this is the last node
                    if (adjConcept.equals(root)) {
                        // 8. Add the path to the final list of returned paths 
                        tmpPath.add(adjConcept);
                        System.out.println("\nFound a path:");
                        System.out.println(tmpPath);
                        finalPathList.add(tmpPath);
                        //path.clear();
                    } else {
                        // If this is not the last node
                        // 9. Clear the list of nodes
                        // Add the existing path to the list of nodes
                        nodeList = tmpPath;

                        // 10. Add the new node to the list of nodes
                        nodeList.add(adjConcept);
                        System.out.println(
                                "\nDidn't find path. Existing path is: ");
                        System.out.println(nodeList);
                        // 11. Push the list of nodes to the path stack
                        pathStack.push(nodeList);
                    }
                } else {
                    // Found cycle
                    // 12. Output the cycle
                    System.out.println("\n\n\nFound cycle!\n\n\n");
                }
            }

        }
        //tmpPath.clear();
        System.out.println("\n\nRETURNED: " + finalPathList);
        return finalPathList;
    }


    public static void testBug() {

        List<Concept> tmpPath = new ArrayList<>();
        List<Concept> nodeList = new ArrayList<>();
        DirectedAcyclicGraph<Concept, DefaultEdge> conceptGraph =
                new DirectedAcyclicGraph<>(DefaultEdge.class);

        // Get all non-BL concepts
        ConceptDao cDao = new ConceptDaoImpl();

        List<Concept> concepts = cDao.findAllConcepts();

        tmpPath.add(concepts.get(0));
        tmpPath.add(concepts.get(1));

        nodeList.add(concepts.get(2));
        nodeList.add(concepts.get(3));

        nodeList.clear();
        // Add the existing path to the list of nodes
        for (Concept item : tmpPath) {
            System.out.println("item in tmpPath list: " + item);
            nodeList.add(item);
        }
        
        
        // 10. Add the new node to the list of nodes
        //nodeList.add(adjConcept);

        //nodeList.clear();
  //      for (Concept item : tmpPath) {
//
  //          nodeList.add(item);
//        }

    //    for (Concept item : tmpPath) {
//
  //          System.out.println(item);
    //    }

    }

}
