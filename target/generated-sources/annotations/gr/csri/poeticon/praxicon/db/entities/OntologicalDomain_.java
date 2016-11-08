package gr.csri.poeticon.praxicon.db.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OntologicalDomain.class)
public abstract class OntologicalDomain_ {

	public static volatile ListAttribute<OntologicalDomain, Concept> concepts;
	public static volatile SingularAttribute<OntologicalDomain, String> domainName;
	public static volatile SingularAttribute<OntologicalDomain, Long> id;

}

