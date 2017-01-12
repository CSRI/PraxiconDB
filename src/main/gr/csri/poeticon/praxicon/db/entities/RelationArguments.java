
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.dao.RelationArgumentDao;
import gr.csri.poeticon.praxicon.db.dao.implSQL.RelationArgumentDaoImpl;
import java.util.LinkedHashSet;
import static java.util.Objects.isNull;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dmavroeidis
 */
@XmlRootElement(name = "relationArguments")
@XmlAccessorType(XmlAccessType.FIELD)
public class RelationArguments {

    @XmlElement(name = "relationArgument")
    Set<RelationArgument> relationArguments = new LinkedHashSet<>();

    public Set<RelationArgument> getRelationArguments() {
        return relationArguments;
    }

    public void setRelationArguments(Set<RelationArgument> relationArguments) {
        this.relationArguments = relationArguments;
    }

    public RelationArguments() {
        relationArguments = new LinkedHashSet<>();
    }

    /**
     * Stores all relation arguments of the collection in the database
     */
    public void storeRelationArguments() {
        if (!relationArguments.isEmpty()) {
            RelationArgumentDao rsDao = new RelationArgumentDaoImpl();
            RelationArgument newRelationArgument;
            for (RelationArgument relationArgument : relationArguments) {
                newRelationArgument = storeRelationArgument(relationArgument);
                rsDao.getEntityManager().clear();
                System.out.println("Storing Relation Argument: " +
                        newRelationArgument.toString());
            }
        }
    }

    public RelationArgument storeRelationArgument(
            RelationArgument relationArgument) {

        /*
         * Analyze relation argument:
         * 0. Create a new Relation Argument.
         * 1. Check if the Relation Argument is a Concept or a RelationSet.
         * 1.a. If Concept then:
         * 1.a.1. Store Concept
         * 1.a.2. Check if the Relation Argument with this Concept
         * exists in the database.
         * 1.a.3. Add the Concept to the Relation Argument
         * 1.b. If Relation Set then:
         * 1.b.1. Store Relation Set
         * 1.a.2. Check if the Relation Argument with this Relation Set
         * exists in the database.
         * 1.b.1. Add the Relation Set to the Relation Argument
         * 2. Store the Relation Argument in the database.
         * 3. Return it.
         */
        Concepts newConceptsObject = new Concepts();
        RelationSets newRelationSetObject = new RelationSets();
        RelationArgumentDao raDao = new RelationArgumentDaoImpl();
        RelationArgument newRelationArgument;
        RelationArgument retrievedRelationArgument;
        boolean isConcept = relationArgument.isConcept();
        if (isConcept) {
            Concept newConcept = newConceptsObject.storeConcept(
                    relationArgument.getConcept());
            retrievedRelationArgument = raDao.getRelationArgument(newConcept);
            if (!isNull(retrievedRelationArgument)) {
                newRelationArgument = retrievedRelationArgument;
                raDao.merge(newRelationArgument);
            } else {
                newRelationArgument = new RelationArgument(newConcept);
                raDao.persist(newRelationArgument);
            }
        } else {
            RelationSet newRelationSet = newRelationSetObject.
                    storeRelationSet(relationArgument.getRelationSet());
            retrievedRelationArgument = raDao.
                    getRelationArgument(newRelationSet);
            if (!isNull(retrievedRelationArgument)) {
                newRelationArgument = retrievedRelationArgument;
                raDao.merge(newRelationArgument);
            } else {
                newRelationArgument = new RelationArgument(newRelationSet);
                raDao.persist(newRelationArgument);
            }
        }
        return newRelationArgument;
    }
}
