package gr.csri.poeticon.praxicon.db.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RelationSet.class)
public abstract class RelationSet_ {

	public static volatile ListAttribute<RelationSet, MotoricRepresentation> motoricRepresentations;
	public static volatile SingularAttribute<RelationSet, String> name;
	public static volatile ListAttribute<RelationSet, LanguageRepresentation> languageRepresentations;
	public static volatile ListAttribute<RelationSet, VisualRepresentation> visualRepresentations;
	public static volatile SingularAttribute<RelationSet, Long> id;
	public static volatile ListAttribute<RelationSet, RelationSet_Relation> relations;

}

