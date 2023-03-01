package org.example.infrastructurelogic;

import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class JavaConfig implements Config {

    @Getter
    private Reflections scanner;
    private final Map<Class, Class> ifc2ImplClass;

    public JavaConfig(String packageToScan, Map<Class, Class> ifc2ImplClass) {
        // сканирует пакет
        scanner = new Reflections(packageToScan);
        this.ifc2ImplClass = ifc2ImplClass;
    }

    @Override
    @SneakyThrows
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        // Принимает ключ, если есть, то возвращает значение этого ключа. Если нет, то выполнение кода в блоке лямбды
        return ifc2ImplClass.computeIfAbsent(ifc, aClass -> {
            // сканирует пакет
            // дай мне все подвиды данного интерфейса
            // получить все классы которые реализуют данный интерфейс
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
            if (classes.size() != 1) {
                throw new RuntimeException(ifc + " has 0 or more than 1 impl. Please update ur config");
            }
            return classes.iterator().next();
        });
    }
}
