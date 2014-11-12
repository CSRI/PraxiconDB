/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.csri.poeticon.praxicon.db.entities.validators;

import java.util.List;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
//import csri.poeticon.praxicon.db.entities.Concept.ConstantConcepts;
import gr.csri.poeticon.praxicon.db.entities.Concept;
import gr.csri.poeticon.praxicon.db.entities.Concept.Status;
import gr.csri.poeticon.praxicon.db.entities.Concept.UniqueInstance;


/**
 *
 * @author dmavroeidis
 */

public class ConceptValidator implements ConstraintValidator<ConstantConcepts, Concept>
{

    private boolean isValid;    

    @Override
    public void initialize(ConstantConcepts constraintAnnotation) {
        this.isValid = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Concept value, ConstraintValidatorContext context)
    {
        if ((value.getStatus() == Status.CONSTANT) && (value.getUniqueInstance() == UniqueInstance.YES))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
