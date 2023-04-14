package utilities;

import org.apache.commons.text.StringSubstitutor;
import java.util.Map;
import static org.apache.commons.text.StringSubstitutor.createInterpolator;

public final class StringUtil {
    private StringUtil() {
        // util class.
    }

    public static String interpolate(final String value) {
        return interpolate(value, null);
    }

    public static String interpolate(final String value, final Map<String, Object> valuesMap) {
        var result = value;
        if (value.contains("${")) {
            final var substitute = (valuesMap == null)
                    ? createInterpolator()
                    : new StringSubstitutor(valuesMap);
            substitute.setEnableSubstitutionInVariables(true);
            result = substitute.replace(value);
        }
        return result;
    }
}
