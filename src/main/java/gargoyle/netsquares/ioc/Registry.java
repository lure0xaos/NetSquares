package gargoyle.netsquares.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.WeakHashMap;

public final class Registry<C> {

    private final WeakHashMap<Signature, C> repo;
    private final Class<C> type;
    private final String factoryMethod;
    private Class<?>[] argTypes;

    private Registry(final Class<C> type, final String factoryMethod) {
        this.type = type;
        this.factoryMethod = factoryMethod;
        repo = new WeakHashMap<Signature, C>();
    }

    public static <C> Registry<C> forClass(final Class<C> type) {
        return new Registry<C>(type, null);
    }

    public static <C> Registry<C> forClass(final Class<C> type, final String factoryMethod) {
        return new Registry<C>(type, factoryMethod);
    }

    public C provide(final Object... args) throws ClassNotFoundException {
        if (argTypes == null) {
            argTypes = guessTypes(args);
        }
        final Signature<C> signature = new Signature<C>(type, factoryMethod, argTypes, args);
        synchronized (repo) {
            C obj = repo.get(signature);
            if (obj == null) {
                obj = create(signature);
                repo.put(signature, obj);
            }
            return obj;
        }
    }

    private C create(final Signature<C> signature) throws ClassNotFoundException {
        try {
            if (factoryMethod == null) {
                if (signature.args == null) {
                    return signature.type.getConstructor(argTypes).newInstance();
                } else
                    return signature.type.getConstructor(argTypes).newInstance(signature.args);
            } else {
                if (signature.args == null) {
                    return (C) signature.type.getMethod(factoryMethod, argTypes).invoke(null);
                } else
                    return (C) signature.type.getMethod(factoryMethod, argTypes).invoke(null, signature.args);
            }
        } catch (final InstantiationException e) {
            throw new ClassNotFoundException(signature.toString(),e.getCause());
        } catch (final IllegalAccessException e) {
            throw new ClassNotFoundException(signature.toString(),e.getCause());
        } catch (final InvocationTargetException e) {
            throw new ClassNotFoundException(signature.toString(),e.getTargetException());
        } catch (final NoSuchMethodException e) {
            throw new ClassNotFoundException(signature.toString(),e.getCause());
        }
    }

    private Class<?>[] guessTypes(final Object[] args) {
        final Class<?>[] classes = new Class[args.length];
        if (factoryMethod == null) {
            for (final Constructor c : type.getConstructors()) {
                final Class[] parameterTypes = c.getParameterTypes();
                boolean matches = true;
                if (parameterTypes.length == args.length) {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        final Class<?> cc = parameterTypes[i];
                        if (cc.isAssignableFrom(args[i].getClass())) {
                            classes[i] = cc;
                        } else {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        return classes;
                    }
                }
            }
        } else {
            for (final Method c : type.getMethods()) {
                if (factoryMethod.equals(c.getName())) {
                    boolean matches = true;
                    final Class[] parameterTypes = c.getParameterTypes();
                    if (parameterTypes.length == args.length) {
                        for (int i = 0; i < parameterTypes.length; i++) {
                            final Class<?> cc = parameterTypes[i];
                            if (cc.isAssignableFrom(args[i].getClass())) {
                                classes[i] = cc;
                            } else {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            return classes;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < args.length; i++) {

        }
        return classes;
    }

    private static class Signature<T> {
        Class<T> type;
        Object[] args;
        String factoryMethod;
        Class<?>[] argTypes;

        Signature(final Class<T> type, final String factoryMethod, final Class<?>[] argTypes, final Object[] args) {
            this.type = type;
            this.factoryMethod = factoryMethod;
            this.argTypes = argTypes;
            this.args = args;
        }

        @Override
        public String toString() {
            return "Signature{" +
                    "type=" + type +
                    ", args=" + Arrays.toString(args) +
                    ", factoryMethod='" + factoryMethod + '\'' +
                    '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Signature<?> signature = (Signature<?>) o;

            if (!type.equals(signature.type)) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(args, signature.args)) return false;
            if (factoryMethod != null ? !factoryMethod.equals(signature.factoryMethod) : signature.factoryMethod != null)
                return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(argTypes, signature.argTypes);

        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + Arrays.hashCode(args);
            result = 31 * result + (factoryMethod != null ? factoryMethod.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(argTypes);
            return result;
        }
    }
}
