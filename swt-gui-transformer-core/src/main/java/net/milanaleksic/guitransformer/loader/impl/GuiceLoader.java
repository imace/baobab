package net.milanaleksic.guitransformer.loader.impl;

import com.google.inject.*;
import net.milanaleksic.guitransformer.loader.Loader;

public class GuiceLoader implements Loader {

    private final Injector injector;

    @Inject
    public GuiceLoader(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void load(Object raw) {
        injector.injectMembers(raw);
    }
}
