/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationTypeDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import static gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl.Direction.DOWN;
import static gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl.Direction.UP;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationTypeDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

/**
 *
 * @author dmavroeidis
 */
public class CreateNewBLRelations {

    public static void main(String args[]) {
        BuildGraph();
        System.exit(0);
    }

    /**
     * Tries to recursively find the basic level concepts of a given concept and
     * add them to the database. This method takes forever* to finish.
     * *forever=~10 days on a typical core i7 system.
     */
    public static void ConceptRecursive() {
        // For each concept get its basic level and store it in relations
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        long startTime = System.nanoTime();

        // Get all concepts
        List<Concept> concepts = cDao.getAllConcepts();
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
                    cDao.getBasicLevelConceptsOld(concept);

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
                    relation1.setRelationType(relationType1);
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

    /**
     * *
     * Builds a graph based on the TYPE_TOKEN relations in Praxicon.
     */
    public static void BuildGraph() {

        DirectedGraph<Concept, DefaultEdge> conceptGraph =
                new DefaultDirectedGraph<>(DefaultEdge.class);

        // Get all non-BL concepts
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();

        // Get concepts from the database
        long startTime = System.nanoTime();
        List<Concept> concepts = cDao.getAllConcepts();
        long endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting concepts in ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds!");

        // Get relations from the database
        startTime = System.nanoTime();
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
            }

            // Get right concept
            Concept rightConcept = new Concept();
            if (relation.getRightArgument().isConcept()) {
                rightConcept = relation.getRightArgument().getConcept();
            }

            //System.out.println(relation);
            // Add edges with 2 concepts
            conceptGraph.addEdge(leftConcept, rightConcept);
//            System.out.println(leftConcept + "\t" + rightConcept);
        }
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished adding edges in ");
        System.out.print((endTime - startTime) / 1000000);
        System.out.println(" miliseconds!\n\n\n");

        List<List<Concept>> paths = new ArrayList<>();
        startTime = System.nanoTime();
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23356));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23357));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23358));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23359));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23361));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23370));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23371));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23372));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23373));
        System.out.println(paths);
        paths = getAllPaths(conceptGraph, concepts.get(0), concepts.get(23374));
        System.out.println(paths);

        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished getting paths in ");
        System.out.print((endTime - startTime) / 1000);
        System.out.println(" microseconds!\n\n\n");
        // Now insert all BL relations.
        insertBLRelations(conceptGraph, concepts);

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

    /**
     * *
     * Identifies all basic-level concepts in a path from root to leaf,
     * connects the non-basic-level ones to their basic-level ones and stores
     * them to the database. This is done only once after a new database is
     * introduced to facilitate finding the basic level concepts of a concept.
     *
     * @param conceptGraph
     * @param concepts
     */
    public static void insertBLRelations(DirectedGraph conceptGraph,
            List<Concept> concepts) {

        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();

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
            } else if (outDegree == 0 && inDegree > 0) {
                leaves.add(c);
            } else if (outDegree > 0 && inDegree == 0) {
                roots.add(c);
            } else {
                internals.add(c);
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
        long startTime = System.nanoTime();
        List<List<Concept>> allPaths = new ArrayList<>();
        // For each leaf, we find all paths to each root.
        for (Concept leaf : leaves) {
            counter += 1;
            if (conceptCountPer100 * countCounts == counter) {
                System.out.println(countCounts + "%");
                countCounts += 1;
            }

            List<List<Concept>> paths = new ArrayList<>();
            int count = 0;
            ConnectivityInspector conI = new ConnectivityInspector(conceptGraph);
            for (Concept root : roots) {
                if (conI.pathExists(root, leaf)) {
                    paths = getAllPaths(conceptGraph, root, leaf);

                    for (List<Concept> blPath : paths) {
                        List<Concept> blConcepts = new ArrayList<>();
                        boolean blFound = false;
                        // Get basic level concepts in the path
                        for (Concept concept : blPath) {
                            if (concept.getSpecificityLevel() ==
                                    Concept.SpecificityLevel.BASIC_LEVEL ||
                                    concept.getSpecificityLevel() ==
                                    Concept.SpecificityLevel.BASIC_LEVEL_EXTENDED) {
                                blConcepts.add(concept);
                                blFound = true;
                            }
                        }

                        // Start adding relations
                        if (blFound) {
                            // Reset the switch to use it in this loop
                            blFound = false;
                            // For each BL concept in the path
                            for (Concept blConcept : blConcepts) {
                                // For each concept in the path starting from the leaf
                                for (Concept concept : blPath) {
                                    // If the concept is not the BL concept
                                    // under consideration
                                    if (!blConcept.equals(concept)) {
                                        // If the concept is not BL
                                        // (this check is needed in case we have
                                        // more than 1 BLs in the path).
                                        if (concept.getSpecificityLevel() ==
                                                Concept.SpecificityLevel.SUBORDINATE ||
                                                concept.getSpecificityLevel() ==
                                                Concept.SpecificityLevel.SUPERORDINATE ||
                                                concept.getSpecificityLevel() ==
                                                Concept.SpecificityLevel.UNKNOWN) {
                                            // Get relation arguments of concepts
                                            RelationArgument relationArgument2 =
                                                    raDao.
                                                    getRelationArgumentByConcept(
                                                            concept);
                                            RelationArgument relationArgument1 =
                                                    raDao.
                                                    getRelationArgumentByConcept(
                                                            blConcept);

                                            // Create new relation
                                            Relation newRelation =
                                                    new Relation();
                                            RelationTypeDao rtDao =
                                                    new RelationTypeDaoImpl();
                                            RelationType newRelationType =
                                                    rtDao.
                                                    getRelationTypeByForwardName(
                                                    RelationType.
                                                    RelationNameForward.
                                                    TYPE_TOKEN);
                                            newRelation.setLinguisticSupport(
                                                    Relation.
                                                    LinguisticallySupported.
                                                    UNKNOWN);
                                            newRelation.setRelationType(
                                                    newRelationType);

                                            if (!blFound) {
                                                newRelation.setLeftArgument(
                                                        relationArgument1);
                                                newRelation.setRightArgument(
                                                        relationArgument2);
                                            } else if (blFound) {
                                                // if the basic level has been found in path,
                                                // reverse the direction of relation.
                                                newRelation.setLeftArgument(
                                                        relationArgument2);
                                                newRelation.setRightArgument(
                                                        relationArgument1);
                                            }

                                            // If the two relation arguments are not related,
                                            // add the relation.
                                            if (!rDao.areRelated(
                                                    relationArgument1,
                                                    relationArgument2)) {
                                                rDao.persist(newRelation);
                                            }
                                        }
                                    } else if (blConcept.equals(concept)) {
                                        blFound = true;
                                    }
                                }
                            }

                            if (blConcepts.size() != 1) {
                                if (blConcepts.isEmpty()) {
                                    count0BLPaths++;
                                } else if (blConcepts.size() == 2) {
                                    count2BLPaths++;
                                } else if (blConcepts.size() >= 3) {
                                    count3BLPaths++;
                                }

                            } else {
                                count1BLPaths++;
                            }
                        }
                    }
                }
            }
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

    /**
     * Gets all paths from a root node to a leaf node using a stack.
     *
     * @param conceptGraph
     * @param root
     * @param leaf
     * @return a list of a list of paths.
     */
    public static List<List<Concept>> getAllPaths(DirectedGraph conceptGraph,
            Concept root, Concept leaf) {
        List<List<Concept>> finalPathList = new ArrayList<>();

        Stack<List<Concept>> pathStack = new Stack<>();
        List<Concept> path = new ArrayList<>();
        List<Concept> tmpPath = new ArrayList<>();

        Concept tmpConcept = null;
        Concept adjConcept = null;

        // 1. Add the first node to the path
        path.add(leaf);
        // 2. Add the path to the stack
        pathStack.push(path);
        int index = 0;

        // 3. While the stack is not empty
        while (!pathStack.isEmpty()) {
            // 4. Get the last node of the popped path in the stack
            tmpPath.addAll(pathStack.pop());
            index = tmpPath.size() - 1;
            tmpConcept = tmpPath.get(index);
            // Get the adjucent edges & the corresponding iterator
            Set<DefaultEdge> tmpEdges = conceptGraph.incomingEdgesOf(tmpConcept);
            Iterator edgeIterator = tmpEdges.iterator();
            // 5. For each adjacent node
            while (edgeIterator.hasNext()) {
                Object tmpEdge = (DefaultEdge)edgeIterator.next();
                adjConcept = (Concept)conceptGraph.getEdgeSource(tmpEdge);
                // 6. If the node is not in the path
                if (!tmpPath.contains(adjConcept)) {
                    // 7. If this is the last node
                    if (adjConcept.equals(root)) {
                        // 8. Add the path to the final list of returned paths
                        tmpPath.add(adjConcept);
                        finalPathList.add(new ArrayList(tmpPath));
                    } else {
                        // If this is not the last node
                        // 9. Clear the list of nodes
                        // Add the existing path to the list of nodes
                        List<Concept> nodeList = new ArrayList(tmpPath);
                        // 10. Add the new node to the list of nodes
                        nodeList.add(adjConcept);
                        // 11. Push the list of nodes to the path stack
                        pathStack.push(nodeList);
                    }
                } else {
                    // Found cycle
                    // 12. Output the cycle
                    System.out.println("\n\n\nFound cycle!");
                    System.out.println(tmpPath);
                    System.out.println(adjConcept);
                    System.out.println("\n\n\n");
                }
            }
            tmpPath.clear();
        }
        return finalPathList;
    }
}
