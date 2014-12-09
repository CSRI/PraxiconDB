/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon;

import gr.csri.poeticon.praxicon.db.dao.ConceptDao;
import gr.csri.poeticon.praxicon.db.dao.LanguageRepresentationDao;
import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.RelationDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.ConceptDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.LanguageRepresentationDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationDaoImpl;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Relation;
import gr.csri.poeticon.praxicon.db.entities.RelationArgument;
import gr.csri.poeticon.praxicon.db.entities.RelationType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author dmavroeidis
 *
 */
public class SimpleTest {

    public static void main(String args[]) {

        // Start by checking most of the methods.
        // Concepts:
        ConceptDao cDao = new ConceptDaoImpl();
        RelationDao rDao = new RelationDaoImpl();
        LanguageRepresentationDao lrDao = new LanguageRepresentationDaoImpl();

        // Get the number of all concepts
        List<Concept> concepts = cDao.findAllConcepts();
        System.out.println("\n\nNumber of all concepts: " + concepts.size());

        // Get the language representations of all concepts having "spoon" as 
        // language representation.
        String toSearch = "spoon";
        List<Concept> conceptsSpoon = cDao.
                findConceptsByLanguageRepresentationExact(toSearch);
        System.out.println("\n\nLanguage Representations of spoon: ");
        System.out.println("---------------------------------");
        for (Concept concept : conceptsSpoon) {
            System.out.print(concept.getLanguageRepresentationName());
            System.out.print(" - ");
            System.out.print(concept.getExternalSourceId());
            System.out.print(" - ");
            System.out.println(concept.getSpecificityLevel());
        }

        // Check whether concepts "shape" and "round_shape" are related.
        System.out.println("\n\nCheck whether two concepts are related: ");
        System.out.println("--------------------------------------- ");
        Concept conceptRoundShape = cDao.findConceptByExternalSourceIdExact(
                "round_shape%1:25:00::");
        Concept conceptShape = cDao.findConceptByExternalSourceIdExact(
                "shape%1:03:00::");
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument relationArgumentConceptShape = raDao.
                getRelationArgumentByConcept(conceptShape);
        RelationArgument relationArgumentConceptRoundShape = raDao.
                getRelationArgumentByConcept(conceptRoundShape);
        boolean areRelated;
        areRelated = rDao.areRelated(relationArgumentConceptShape,
                relationArgumentConceptRoundShape);
        System.out.print("shape and round_shape are ");
        System.out.println((areRelated) ? "related" : "not related");

        // Get all relations of a Concept
        System.out.println("\n\nAll relations of concept shape: ");
        System.out.println("-------------------------------");
        List<Relation> allRelationsOfConceptShape = rDao.
                getAllRelationsOfConcept(conceptShape);
        for (Relation relation : allRelationsOfConceptShape) {
            System.out.println(relation);
        }

        // Get children concepts and specificity level of the first concept 
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nChildren of the first occurence of a concept" +
                " having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "----------------------------------");
        List<Concept> childrenOfSpoon = cDao.getChildrenOfConcept(
                conceptsSpoon.get(0));
        for (Concept concept : childrenOfSpoon) {
            System.out.print(concept.getExternalSourceId());
            System.out.print(" - ");
            System.out.println(concept.getSpecificityLevel());
        }

        // Get parent concepts and specificity level of the first concept 
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nParent concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "-----------------------------------------");
        List<Concept> parents = new ArrayList<>();
        HashSet<Concept> sisters = new HashSet<>();
        parents = cDao.getParentsOfConcept(conceptsSpoon.get(0));
        for (Concept parent : parents) {
            System.out.println(parent + " - \t" + parent.getSpecificityLevel());
            sisters.addAll(cDao.getChildrenOfConcept(parent));
        }

        // Get sister concepts and specificity level of the first concept 
        // in the list of concepts that have language representation spoon.
        System.out.println("\n\nSister concepts of the first occurence of a " +
                "concept having language representation spoon: ");
        System.out.println("------------------------------------------------" +
                "-----------------------------------------");
        for (Concept sister : sisters) {
            System.out.println(sister + " - \t" + sister.getSpecificityLevel());
        }

        // Get all Language Representation texts count.
        // Would be faster to implement a NamedQuery for that, but wanted to
        // test the getAllLanguageRepresentationText() method.
        System.out.println("\n\nCount of all Language Representation Texts:");
        System.out.println("-------------------------------------------");
        List<String> languageRepresentationTexts = new ArrayList<>();
        languageRepresentationTexts = lrDao.getAllLanguageRepresentationText();
        System.out.println(languageRepresentationTexts.size());

        // Get all Basic Level Concepts.
        System.out.println("\n\nAll Basic Level Concepts:");
        System.out.println("-------------------------------------------");
        List<Concept> basicLevelConcepts = new ArrayList<>();
        basicLevelConcepts = cDao.findAllBasicLevelConcepts();
        for (Concept concept : basicLevelConcepts){
            System.out.println(concept);
        }
        
        String stringToSearch = "entity%1:03:00::";
        System.out.println("\n\nBasic Level of concept " + stringToSearch);
        System.out.println("-------------------------------------------");
        Concept concept = cDao.findConceptByExternalSourceIdExact(
                stringToSearch);
        long startTime = System.nanoTime();
        List<Concept> basicLevelOfConceptSoupSpoon = cDao.getBasicLevel(concept);
        long endTime = System.nanoTime();
        System.out.print(
                "Time of getBasicLevel() for concept: " + stringToSearch + " ");
        System.out.print((endTime - startTime) / 1000000000);
        System.out.println(" seconds");

        if (basicLevelOfConceptSoupSpoon.isEmpty()) {
            System.out.println("Concept " + stringToSearch +
                    " doesn't have a Basic Level Concept");
        } else {
            for (Concept item : basicLevelOfConceptSoupSpoon) {
                System.out.println(item);
            }
        }

        System.out.println("\n\nAll relations with relation type: HAS_INSTANCE");
        System.out.println("-----------------------------------------------");
        List<Relation> hasInstanceRelations = rDao.getRelationsByRelationType(
                RelationType.RelationNameForward.HAS_INSTANCE);

        for (Relation relation : hasInstanceRelations) {
            //System.out.println(relation);
            if (relation.getLeftArgument().isConcept() && relation.
                    getRightArgument().isConcept()) {
                if ((relation.getLeftArgument().getConcept().
                        getLanguageRepresentations().size() != 0 && relation.
                        getRightArgument().getConcept().
                        getLanguageRepresentations().size() != 0) && (relation.
                        getLeftArgument().getConcept().getConceptType() ==
                        Concept.Type.MOVEMENT ||
                        relation.getRightArgument().getConcept().
                        getConceptType() == Concept.Type.MOVEMENT)) {
                    System.out.println(relation.getLeftArgument().getConcept().
                            getConceptType() + " " + relation.getType().
                            getForwardNameString() + " " + relation.
                            getRightArgument().getConcept().getConceptType());
                }
            } else {
                System.out.println("One of the arguments is not a Concept");
            }
        }
    }

//    public static List<Concept> getObjectsOfRelation(RelationType relationType) {
//        
//        //create the JPQL query
//        Query q = EntityMngFactory.getEntityManager().createQuery(
//            "SELECT DISTINCT r.object FROM Relation r, RelationType rType " +
//            "WHERE r.Type = rType.id AND rType.forwardName = ?1");
//        q.setParameter(1, relationType);
//        
//        List<Concept> concepts = new ArrayList<>();
//        for (RelationArgument item : q.getResultList()){
//            System.out.println(item);
//        }
//        //Get the results
//        return concepts;
//    }
}
