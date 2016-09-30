/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csri.poeticon.praxicon.db.entities.validators;

import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.Concept.UniqueInstance;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author dmavroeidis
 */
public class ConceptValidator implements
        ConstraintValidator<ConstantConcepts, Concept> {

    private boolean isValid;

    @Override
    public void initialize(ConstantConcepts constraintAnnotation) {
        this.isValid = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Concept value, ConstraintValidatorContext context) {
        if ((value.getStatus() == Status.CONSTANT) &&
                (value.getUniqueInstance() == UniqueInstance.YES)) {
            return true;
        } else {
            return false;
        }
    }
}
