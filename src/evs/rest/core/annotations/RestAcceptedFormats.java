package evs.rest.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface RestAcceptedFormats {

	RestFormat[] value();


}
