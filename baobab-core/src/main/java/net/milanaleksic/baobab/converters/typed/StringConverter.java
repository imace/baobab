package net.milanaleksic.baobab.converters.typed;

import com.google.common.base.Strings;
import net.milanaleksic.baobab.*;
import net.milanaleksic.baobab.providers.ResourceBundleProvider;
import org.codehaus.jackson.JsonNode;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.*;

/**
 * User: Milan Aleksic
 * Date: 4/19/12
 * Time: 3:23 PM
 */
public class StringConverter extends TypedConverter<String> {

    private static final Pattern resourceMessage = Pattern.compile("\\[(.*)\\]");

    @Inject
    private ResourceBundleProvider resourceBundleProvider;

    @Override
    public String getValueFromJson(JsonNode node) {
        String fieldValue = node.asText();
        if (Strings.isNullOrEmpty(fieldValue))
            return fieldValue;

        // TODO: this should be done better with state machine instead of regex - to allow multiple replacements of different templates
        Matcher matcher = resourceMessage.matcher(fieldValue);
        if (matcher.find()) {
            try {
                return matcher.replaceAll(resourceBundleProvider.getResourceBundle().getString(matcher.group(1)));
            } catch (MissingResourceException ignored) {
                // ignored
            } catch (Exception e) {
                throw new TransformerException("Problem while searching for the string resource", e);
            }
        }

        return fieldValue;
    }

}
