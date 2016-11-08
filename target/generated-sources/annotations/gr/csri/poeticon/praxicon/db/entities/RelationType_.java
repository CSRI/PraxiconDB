package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.entities.RelationType.RelationNameBackward;
import gr.csri.poeticon.praxicon.db.entities.RelationType.RelationNameForward;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RelationType.class)
public abstract class RelationType_ {

	public static volatile SingularAttribute<RelationType, RelationNameForward> forwardName;
	public static volatile SingularAttribute<RelationType, Long> id;
	public static volatile ListAttribute<RelationType, Relation> relations;
	public static volatile SingularAttribute<RelationType, RelationNameBackward> backwardName;

}

