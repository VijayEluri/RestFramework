package evs.rest.core.persistence;

import java.util.Set;

import javax.validation.ConstraintViolation;

public class RestPersistenceValidationException extends Exception {
	
	private Set<ConstraintViolation<Object>> violations;

	public RestPersistenceValidationException(
			Set<ConstraintViolation<Object>> violations) {
		this.violations = violations;
	}
	
	public Set<ConstraintViolation<Object>> getViolations() {
		return this.violations;
	}

}
