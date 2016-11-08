package gr.csri.poeticon.praxicon.db.entities;

import gr.csri.poeticon.praxicon.db.entities.VisualRepresentation.MediaType;
import java.net.URI;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(VisualRepresentation.class)
public abstract class VisualRepresentation_ {

	public static volatile SingularAttribute<VisualRepresentation, Concept> concept;
	public static volatile SingularAttribute<VisualRepresentation, RelationSet> relationSet;
	public static volatile SingularAttribute<VisualRepresentation, MotoricRepresentation> motoricRepresentation;
	public static volatile SingularAttribute<VisualRepresentation, String> name;
	public static volatile SingularAttribute<VisualRepresentation, MediaType> mediaType;
	public static volatile SingularAttribute<VisualRepresentation, String> comment;
	public static volatile SingularAttribute<VisualRepresentation, Long> id;
	public static volatile SingularAttribute<VisualRepresentation, String> source;
	public static volatile SingularAttribute<VisualRepresentation, URI> uri;

}

