package org.example.infrastructurelogic;

import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. Мохнатые годы => создаем любые объекты через new
 * 2. Спустя несколько лет => объекты не создаем с помощью new, используем синглтон фабрику,
 * ответственность к-й заключается в создании объектов
 * Итог:
 * Централизованное место для создания объектов:
 * 1. если надо менять имплементацию не надо лезть в код (Гибкость)
 * 2. перед тем как фабрика отдаст объект, она его может настроить согласно нашим конвенциям, которые мы придумаем
 * 3. в будущем можно будет кэшировать синглтоны
 */

public class ObjectFactory {
    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators = new ArrayList<>();
    private final List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;
        for (Class<? extends ObjectConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(ObjectConfigurator.class)) {
            configurators.add(aClass.getDeclaredConstructor().newInstance());
        }
        for (Class<? extends ProxyConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(ProxyConfigurator.class)) {
            proxyConfigurators.add(aClass.getDeclaredConstructor().newInstance());
        }

    }

    @SneakyThrows
    // в Spring этот метод называется getBean() класса ApplicationContext
    // принимает тип
    public <T> T createObject(Class<T> implClass) {
        // создаем объект на основе его интерфейса или конкретной реализации
        T t = create(implClass);

        // настраиваем наши объекты
        configure(t);
        // доп настройка нашего класса
        invokeInitMethods(implClass, t);

        t = wrapWithProxyIfNeeded(implClass, t);

        return t;
    }

    private <T> T wrapWithProxyIfNeeded(Class<T> implClass, T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = (T) proxyConfigurator.replaceWithProxyIfNeeded(t, implClass);
        }
        return t;
    }

    private static <T> void invokeInitMethods(Class<T> implClass, T t) throws IllegalAccessException, InvocationTargetException {
        for (Method method : implClass.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(t);
            }
        }
    }

    private <T> void configure(T t) {
        configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }
}
