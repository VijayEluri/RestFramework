package evs.rest.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import evs.rest.core.marshal.RestFormat;


/**
 * supported @RestFormat formats of a given @RestService
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestAcceptedFormats {

	RestFormat[] value();


}
