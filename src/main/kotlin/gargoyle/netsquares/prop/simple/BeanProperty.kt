package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.base.BaseRWProperty
import java.beans.BeanInfo
import java.beans.Introspector
import java.beans.PropertyDescriptor

class BeanProperty<B : Any, T : Any>(val bean: B, val propertyName: String) : BaseRWProperty<T>() {
    private lateinit var propertyDescriptor: PropertyDescriptor

    init {
        val beanInfo: BeanInfo = Introspector.getBeanInfo(bean.javaClass)
        var ok = false
        for (propertyDescriptor in beanInfo.propertyDescriptors) {
            if (propertyName == propertyDescriptor.name) {
                this.propertyDescriptor = propertyDescriptor
                ok = true
            }
        }
        if (!ok) {
            error(propertyName)
        }
    }

    override fun _set(value: T) {
        try {
            propertyDescriptor.writeMethod.invoke(bean, value)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e.message, e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(): T {
        return try {
            propertyDescriptor.readMethod.invoke(bean) as T
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e.message, e)
        }
    }
}
