package evs.rest.demo.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.log4j.Logger;

import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;

@Constraint(validatedBy=PlacementConstraint.PlacementValidator.class)
//@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PlacementConstraint {
	String message() default "Total of placement.amount * item.size must not excee rack.place";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
	
	class PlacementValidator implements ConstraintValidator<PlacementConstraint, Placement> {
		private static Logger logger = Logger.getLogger(PlacementValidator.class);

		@Override
		public void initialize(PlacementConstraint constraint) {
		}

		@Override
		public boolean isValid(Placement placement, ConstraintValidatorContext context) {
			Rack rack = placement.getRack();
			
			Integer placedSize = 0;
			
			/*TODO
			if(rack.getPlacements() != null) {
				for(Placement pl : rack.getPlacements()) {
					placedSize += pl.getItem().getSize() * pl.getAmount();
				}
			}*/
			
			Integer newSize = placement.getItem().getSize() * placement.getAmount();
			
			if(placedSize + newSize <= rack.getPlace()) {
				return true; //OK
			}
			else {
				return false; //max place exceeded
			}
		}

	}
}
