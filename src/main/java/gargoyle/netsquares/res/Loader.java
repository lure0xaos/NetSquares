package gargoyle.netsquares.res;

@FunctionalInterface
public interface Loader<L, R> {
    R load(NSContext context, L location, String suffix) throws Exception;
}
