package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.entities.Concept.ConceptType;
import gr.csri.poeticon.praxicon.db.entities.Concept.PragmaticStatus;
import gr.csri.poeticon.praxicon.db.entities.Concept.SpecificityLevel;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.Concept.UniqueInstance;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Concept.class)
public abstract class Concept_ {

	public static volatile SingularAttribute<Concept, SpecificityLevel> specificityLevel;
	public static volatile SingularAttribute<Concept, ConceptType> conceptType;
	public static volatile SingularAttribute<Concept, PragmaticStatus> pragmaticStatus;
	public static volatile SingularAttribute<Concept, String> source;
	public static volatile ListAttribute<Concept, OntologicalDomain> ontologicalDomains;
	public static volatile ListAttribute<Concept, MotoricRepresentation> motoricRepresentations;
	public static volatile SingularAttribute<Concept, String> name;
	public static volatile ListAttribute<Concept, Concept_LanguageRepresentation> languageRepresentations;
	public static volatile SingularAttribute<Concept, String> externalSourceId;
	public static volatile SingularAttribute<Concept, String> comment;
	public static volatile ListAttribute<Concept, VisualRepresentation> visualRepresentations;
	public static volatile SingularAttribute<Concept, Long> id;
	public static volatile SingularAttribute<Concept, UniqueInstance> uniqueInstance;
	public static volatile SingularAttribute<Concept, Status> status;

}

