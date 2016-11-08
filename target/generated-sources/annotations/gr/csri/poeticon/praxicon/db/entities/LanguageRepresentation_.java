package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Language;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Operator;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.PartOfSpeech;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.Productivity;
import gr.csri.poeticon.praxicon.db.entities.LanguageRepresentation.UseStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LanguageRepresentation.class)
public abstract class LanguageRepresentation_ {

	public static volatile SingularAttribute<LanguageRepresentation, String> negation;
	public static volatile SingularAttribute<LanguageRepresentation, Productivity> productivity;
	public static volatile ListAttribute<LanguageRepresentation, Concept_LanguageRepresentation> concepts;
	public static volatile SingularAttribute<LanguageRepresentation, PartOfSpeech> partOfSpeech;
	public static volatile ListAttribute<LanguageRepresentation, RelationSet> relationSets;
	public static volatile SingularAttribute<LanguageRepresentation, Language> language;
	public static volatile SingularAttribute<LanguageRepresentation, String> comment;
	public static volatile SingularAttribute<LanguageRepresentation, Long> id;
	public static volatile SingularAttribute<LanguageRepresentation, String> text;
	public static volatile SingularAttribute<LanguageRepresentation, Operator> operator;
	public static volatile SingularAttribute<LanguageRepresentation, UseStatus> useStatus;

}

