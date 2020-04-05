package order.services.tool.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = LocationPatternValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationPattern {
    String message() default "Invalid Latitude and Longitude.";

    Class< ? >[] groups() default {};

    Class< ? extends Payload>[] payload() default {};
}
