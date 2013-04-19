package net.milanaleksic.guitransformer.util;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.*;
import net.milanaleksic.guitransformer.integration.loader.Loader;

import java.util.Map;

/**
 * User: Milan Aleksic
 * Date: 4/19/13
 * Time: 9:46 AM
 */
public class Configuration {

    private static final Config reference = ConfigFactory.load();

    public static void loadStringToClassMappingToBuilder(String configName, ImmutableMap.Builder<String, Class<?>> builder) {
        final Config configuration = reference.getConfig(configName);
        for (Map.Entry<String, Object> entry : configuration.root().unwrapped().entrySet())
            try {
                builder.put(entry.getKey(), Class.forName(entry.getValue().toString()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    @SuppressWarnings("unchecked")
    public static <T> void loadClassToInstanceMappingToBuilder(String configName, ImmutableMap.Builder<Class<?>, T> builder, Optional<Loader> loader) {
        final Config configuration = reference.getConfig(configName);
        if (configuration.isEmpty())
            return;
        for (Map.Entry<String, Object> entry : configuration.root().unwrapped().entrySet()) {
            try {
                final Class<?> classWhichIsMaybeWrapper = Class.forName(entry.getKey());
                final Class<?> clazz = Class.forName(entry.getValue().toString());

                // To be both Guice- and Spring- able, class must be initialized via no-arg constructor
                // thus, it is not allowed to use constructor injection, only property injection
                if (clazz.getConstructor() == null)
                    throw new RuntimeException("Transformer supports only extension classes with default constructor");
                T raw = (T) ObjectUtil.createInstanceForType(clazz);
                if (loader.isPresent())
                    loader.get().load(raw);
                try {
                    Class primitiveClass = (Class) classWhichIsMaybeWrapper.getField("TYPE").get(null);
                    builder.put(primitiveClass, raw);
                } catch (NoSuchFieldException ignored) {
                    builder.put(classWhichIsMaybeWrapper, raw);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void loadStringToInstanceMappingToBuilder(String configName, ImmutableMap.Builder<String, T> builder, Optional<Loader> loader) {
        final Config configuration = reference.getConfig(configName);
        if (configuration.isEmpty())
            return;
        for (Map.Entry<String, Object> entry : configuration.root().unwrapped().entrySet()) {
            try {
                T raw = (T) ObjectUtil.createInstanceForType(Class.forName(entry.getValue().toString()));
                if (loader.isPresent())
                    loader.get().load(raw);
                builder.put(entry.getKey(), raw);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
