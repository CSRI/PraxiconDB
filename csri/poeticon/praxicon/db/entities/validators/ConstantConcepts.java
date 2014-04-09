/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csri.poeticon.praxicon.db.entities.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
/**
 *
 * @author dmavroeidis
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = ConceptValidator.class)
@Documented
public @interface ConstantConcepts
{
    String message() default "Constant Concept constraint has been violated";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean value();
}
