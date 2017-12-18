package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.base.BaseRWProperty;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Objects;

public final class BeanProperty<B, T> extends BaseRWProperty<T> {
    private final B bean;
    private final PropertyDescriptor propertyDescriptor;
    private final String propertyName;

    public BeanProperty(B bean, String propertyName) {
        this.bean = Objects.requireNonNull(bean);
        this.propertyName = Objects.requireNonNull(propertyName);
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (Objects.equals(propertyName, propertyDescriptor.getName())) {
                this.propertyDescriptor = propertyDescriptor;
                return;
            }
        }
        throw new IllegalArgumentException(propertyName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        try {
            return (T) propertyDescriptor.getReadMethod().invoke(bean);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public B getBean() {
        return bean;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    protected void _set(T value) {
        try {
            propertyDescriptor.getWriteMethod().invoke(bean, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
