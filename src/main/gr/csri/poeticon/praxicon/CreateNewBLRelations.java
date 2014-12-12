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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 *
 * @author dmavroeidis
 */
public class CreateNewBLRelations {

    public static void main(String args[]) {

        //ConceptGraph();
        ConceptRecursive();

    }

    public void persist(Object object) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("PraxiconDBPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("transaction error");
        } finally {
            em.flush();
            em.close();
        }
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
                System.out.println("\n\n\n\n\n\n\n\n\n\n" + 1 * countCounts +
                        "%\n\n\n\n\n\n\n\n\n");
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

        // Get the reverse of concept graph.
        // This is necessary so as to calculate both downward (conceptGraph)
        // and upward (reverseConceptGraph) basic level concepts.
        EdgeReversedGraph reverseConceptGraph = new EdgeReversedGraph(
                conceptGraph);

        insertBLRelations(conceptGraph, concepts);
        insertBLRelations(reverseConceptGraph, concepts);

        // Following command fills the stack.
        //Collection allShortestPaths = shortestPaths.getShortestPaths();
        //double diameter = shortestPaths.getDiameter();
        //System.out.println("\n\n\ngraph's diameter: " + diameter);
        // Gets topological 
//        TopologicalOrderIterator iter = new TopologicalOrderIterator(conceptGraph);
//        Concept c = new Concept();
//        while (iter.hasNext()){
//            c = (Concept)iter.next();
//            System.out.println(c);
//        }
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
            List<Concept> concepts) {

        RelationDao rDao = new RelationDaoImpl();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        List<Entry<Concept, Concept>> newBasicLevelConnections;
        newBasicLevelConnections = new ArrayList<>();

        TopologicalOrderIterator<Concept, Integer> iter =
                new TopologicalOrderIterator(
                        conceptGraph);
        //Concept c = new Concept();
        int counter = 0;
        int conceptCount = concepts.size();
        int conceptCountPer100 = conceptCount / 100;
        int countCounts = 1;
        long startTime = System.nanoTime();
        while (iter.hasNext()) {
            counter += 1;
            if (conceptCountPer100 * countCounts == counter) {
                System.out.println(1 * countCounts + "%");
                countCounts += 1;
            }

            Concept c = iter.next();

            if (c.getSpecificityLevel() == Concept.SpecificityLevel.BASIC_LEVEL) {
                TopologicalOrderIterator<Concept, Integer> new_iter =
                        new TopologicalOrderIterator(conceptGraph);
                while (new_iter.hasNext()) {
                    Concept co = new_iter.next();
                    List shortestPath = new ArrayList();
                    shortestPath = DijkstraShortestPath.findPathBetween(
                            conceptGraph, c, co);
                    // If concepts are connected
                    if ((shortestPath != null) && (!shortestPath.isEmpty())) {
                        java.util.Map.Entry<Concept, Concept> pair1 =
                                new java.util.AbstractMap.SimpleEntry<>(c, co);
                        newBasicLevelConnections.add(pair1);
                    }
                }
            }
        }
        long endTime = System.nanoTime();
        System.out.print("\n\n\nFinished calculating shortest paths in ");
        System.out.print((endTime - startTime) / (60000000000D));
        System.out.println(" minutes!\n\n\n");

        startTime = System.nanoTime();
        for (java.util.Map.Entry<Concept, Concept> item
                : newBasicLevelConnections) {
            // Get relation arguments of concepts
            RelationArgument relationArgument1 = raDao.
                    getRelationArgumentByConcept(item.getValue());
            RelationArgument relationArgument2 = raDao.
                    getRelationArgumentByConcept(item.getKey());

            // Create new relation
            Relation newRelation = new Relation();
            RelationType newRelationType = new RelationType(
                    RelationType.RelationNameForward.HAS_BASIC_LEVEL,
                    RelationType.RelationNameBackward.BASIC_LEVEL_OF);
            newRelation.setLeftArgument(relationArgument1);
            newRelation.setRightArgument(relationArgument2);
            newRelation.setLinguisticSupport(
                    Relation.LinguisticallySupported.UNKNOWN);
            newRelation.setType(newRelationType);
            rDao.persist(newRelation);
        }
        endTime = System.nanoTime();
        System.out.print("\n\n\nFinished adding relations to the database in ");
        System.out.print((endTime - startTime) / (60000000000D));
        System.out.println(" minutes!\n\n\n");
    }

}
