package net.milanaleksic.baobab.converters;

import com.google.common.base.*;
import com.google.common.collect.*;
import net.milanaleksic.baobab.TransformationContext;
import net.milanaleksic.baobab.model.ModelBindingMetaData;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * <p>
 * Adding is always delegated to hierarchy root context. Fetching all mapped objects
 * involves aggregating all tree elements up to current context (to cover independent
 * trees).
 * </p>
 */
public class TransformationWorkingContext {

    private final Map<String, Object> mappedObjects;

    private ModelBindingMetaData modelBindingMetaData;

    private boolean doNotCreateModalDialogs;

    private Object workItem;

    private final TransformationWorkingContext parentContext;

    public TransformationWorkingContext() {
        this(null);
    }

    public TransformationWorkingContext(@Nullable TransformationWorkingContext parentContext) {
        this.parentContext = parentContext;
        mappedObjects = parentContext == null ? Maps.<String, Object>newHashMap() : ImmutableMap.<String, Object>of();
    }

    public void setDoNotCreateModalDialogs(boolean doNotCreateModalDialogs) {
        this.doNotCreateModalDialogs = doNotCreateModalDialogs;
    }

    public boolean isDoNotCreateModalDialogs() {
        return doNotCreateModalDialogs;
    }

    public Object getWorkItem() {
        return workItem;
    }

    public TransformationContext createTransformationContext() {
        return new TransformationContext(workItem, getMutableRootMappedObjects(), modelBindingMetaData);
    }

    public ModelBindingMetaData getModelBindingMetaData() {
        return modelBindingMetaData;
    }

    public void setModelBindingMetaData(ModelBindingMetaData modelBindingMetaData) {
        this.modelBindingMetaData = modelBindingMetaData;
    }

    public Object getMappedObject(String key) {
        return getMutableRootMappedObjects().get(key);
    }

    public void mapAll(Map<String, Object> mappedObjects) {
        getMutableRootMappedObjects().putAll(mappedObjects);
    }

    public void mapObject(String key, Object object) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Object is not named");
        if (key.startsWith("_"))
            return;
        getMutableRootMappedObjects().put(key, object);
    }

    Map<String, Object> getMutableRootMappedObjects() {
        TransformationWorkingContext iterator = this;
        while (iterator != null && iterator.getParentContext() != null)
            iterator = iterator.getParentContext();
        return iterator == null ? null : iterator.mappedObjects;
    }

    public void setWorkItem(Object workItem) {
        this.workItem = workItem;
    }

    TransformationWorkingContext getParentContext() {
        return parentContext;
    }

}
