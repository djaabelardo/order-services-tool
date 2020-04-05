package order.services.tool.validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationPatternValidator implements ConstraintValidator<LocationPattern, List<String>>
{

    private static final Pattern LOCATION_PATTERN = Pattern.compile("^-?[0-9]\\d*(\\.\\d+)?$");

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context)
    {
        boolean isValid = true;
        for (String value : values)
        {
            Matcher matcher = LOCATION_PATTERN.matcher(value);
            if (!matcher.find())
            {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

}
