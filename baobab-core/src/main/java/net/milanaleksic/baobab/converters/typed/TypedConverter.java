package net.milanaleksic.baobab.converters.typed;

import net.milanaleksic.baobab.converters.Converter;
import org.codehaus.jackson.JsonNode;

import java.util.Map;

/**
 * User: Milan Aleksic
 * Date: 4/19/12
 * Time: 2:59 PM
 */
public abstract class TypedConverter<T> implements Converter {

    @Override
    public Object getValueFromJson(Object targetObject, JsonNode value, Map<String, Object> mappedObjects) {
        return getValueFromJson(value);
    }

    protected abstract T getValueFromJson(JsonNode node);

    @Override
    public void cleanUp() {}

}
