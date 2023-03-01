package org.example.infrastructurelogic;

import org.reflections.Reflections;

/**
 * Может читать конфигурацию с xml, groovy и т.д.
 */
public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> ifc);
    Reflections getScanner();
}
