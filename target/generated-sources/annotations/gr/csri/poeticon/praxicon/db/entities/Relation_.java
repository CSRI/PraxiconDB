package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.entities.Relation.Inferred;
import gr.csri.poeticon.praxicon.db.entities.Relation.LinguisticallySupported;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Relation.class)
public abstract class Relation_ {

	public static volatile SingularAttribute<Relation, RelationType> relationType;
	public static volatile SingularAttribute<Relation, RelationArgument> rightArgument;
	public static volatile ListAttribute<Relation, RelationSet_Relation> relationSet;
	public static volatile SingularAttribute<Relation, LinguisticallySupported> linguisticallySupported;
	public static volatile SingularAttribute<Relation, Inferred> inferred;
	public static volatile SingularAttribute<Relation, String> comment;
	public static volatile SingularAttribute<Relation, Long> id;
	public static volatile SingularAttribute<Relation, RelationArgument> leftArgument;

}

