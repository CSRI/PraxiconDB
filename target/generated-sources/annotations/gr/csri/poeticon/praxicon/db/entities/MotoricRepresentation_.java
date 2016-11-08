package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.entities.MotoricRepresentation.PerformingAgent;
import java.net.URI;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MotoricRepresentation.class)
public abstract class MotoricRepresentation_ {

	public static volatile SingularAttribute<MotoricRepresentation, PerformingAgent> performingAgent;
	public static volatile SingularAttribute<MotoricRepresentation, Concept> concept;
	public static volatile SingularAttribute<MotoricRepresentation, RelationSet> relationSet;
	public static volatile SingularAttribute<MotoricRepresentation, String> comment;
	public static volatile ListAttribute<MotoricRepresentation, VisualRepresentation> visualRepresentations;
	public static volatile SingularAttribute<MotoricRepresentation, Long> id;
	public static volatile SingularAttribute<MotoricRepresentation, String> source;
	public static volatile SingularAttribute<MotoricRepresentation, URI> uri;

}

