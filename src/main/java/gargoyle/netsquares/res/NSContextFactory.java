package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Lazy;

public final class NSContextFactory {
    private static final Lazy<NSContextImpl> context = new Lazy<>(NSContextImpl::new);

    private NSContextFactory() {
    }

    public static NSContext currentContext() {
        return getContext();
    }

    static NSContextImpl getContext() {
        return context.get();
    }
}
