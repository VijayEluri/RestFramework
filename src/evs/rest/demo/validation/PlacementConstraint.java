package evs.rest.demo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.apache.log4j.Logger;

import evs.rest.demo.domain.Placement;
import evs.rest.demo.domain.Rack;

@Constraint(validatedBy=PlacementConstraint.PlacementValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PlacementConstraint {
	String message() default "Total of placement.amount * item.size must not excee rack.place";
	
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
	
	class PlacementValidator implements ConstraintValidator<PlacementConstraint, Placement> {
		private static Logger logger = Logger.getLogger(PlacementValidator.class);

		
		public void initialize(PlacementConstraint constraint) {
			logger.debug("init");
		}

		
		public boolean isValid(Placement placement, ConstraintValidatorContext context) {
			logger.debug("validation");
			Rack rack = placement.getRack();
			
			if(Rack.calculatePlacedSize(rack) <= rack.getPlace()) {
				logger.debug("size ok");
				return true; //OK
			}
			else {
				logger.debug("maxPlace limit exceeded");
				return false; //max place exceeded
			}
		}

	}
}
