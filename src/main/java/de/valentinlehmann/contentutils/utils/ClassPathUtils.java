package de.valentinlehmann.contentutils.utils;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import lombok.RequiredArgsConstructor;
import org.reflections8.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ClassPathUtils {
    private final ContentUtilsPlugin plugin;

    private <T> Set<Class<? extends T>> getAllClassesInPackage(String packageName, Class<T> type) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(type);
    }

    public <T> void createInstanceAndApplyAction(String packageName, Class<T> type, Consumer<T> action, Object... constructorArgs) {
        Class<?>[] constructorArgTypes = new Class[constructorArgs.length];

        for (int i = 0; i < constructorArgs.length; i++) {
            constructorArgTypes[i] = constructorArgs[i].getClass();
        }

        getAllClassesInPackage(packageName, type).forEach(clazz -> {
            try {
                action.accept(clazz.getConstructor(constructorArgTypes).newInstance(constructorArgs));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                this.plugin.getLogger().severe(String.format("Konnte Klassen im Package %s nicht registrieren! Fehler:", packageName));
                e.printStackTrace();
            }
        });
    }
}
