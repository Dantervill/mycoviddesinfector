package org.example.infrastructurelogic;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    @Setter
    private ObjectFactory factory;
    private final Map<Class, Object> cache = new ConcurrentHashMap<>();
    @Getter
    private Config config;

    public ApplicationContext(Config config) {
        this.config = config;
    }

    public <T> T getObject(Class<T> type) {
        // Может быть этот синглтон уже создан. Если да, возьми его из кэша
        if (cache.containsKey(type)) {
            return (T) cache.get(type);
        }

        Class<? extends T> implClass = type;
        // если этот тип является интерфейсом
        if (type.isInterface()) {
            // получаем его имплементацию
            implClass = config.getImplClass(type);
        }

        // создаем имплементацию
        T t = factory.createObject(implClass);

        // либо кэшируем, либо нет в зависимости от того синглтон это или нет
        if (implClass.isAnnotationPresent(Singleton.class)) {
            cache.put(type, t);
        }

        return t;
    }
}
