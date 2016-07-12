/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Small static class for reflection utils
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class ReflectionUtils {

    /**
     * Estract class name from object
     *
     * @param aClass Object to be analyzed
     * @return Simple name class
     */
    public static String getClassName(Object aClass) {
        return aClass.getClass().getSimpleName();
    }

    /**
     * From generic object return all the declared fields as array string
     *
     * @param aClass Generic Class
     * @return String[] Array of all fields as string
     */
    public static String[] getStringFields(Object aClass) {
        Field[] field = aClass.getClass().getDeclaredFields();
        String[] stringField = new String[field.length];

        for (int i = 0; i < field.length; i++) {
            stringField[i] = field[i].getName();
        }
        return stringField;
    }

    /**
     * Find in the target object the setter related to the 'name''s field and
     * update it with the value passed as param,
     *
     * @param name Name of the field to be update
     * @param target Target class for the update
     * @param value Value to be assigned
     * @throws NoSuchMethodException Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/NoSuchMethodException.html">javadoc</a> documentation.
     * @throws IllegalAccessException  Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalAccessException.html">javadoc</a> documentation.
     * @throws IllegalArgumentException Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">javadoc</a> documentation.
     * @throws InvocationTargetException  Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/InvocationTargetException.html">javadoc</a> documentation.
     */
    public static void setProperty(String name, Object target, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Method method;
        String nameToUpperCase = checkCase(name);

        method = target.getClass().getMethod("set" + nameToUpperCase, new Class[]{value.getClass()});

        if (method != null) {
            method.invoke(target, new Object[]{value});
        }
    }

    /**
     * Look in the target class for the specific field 'name' and return the 
     * output of the get method.
     * @param name Name of the field to be searched
     * @param target Target class for looking
     * @return Output value for the 'name' field
     * @throws NoSuchMethodException Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/NoSuchMethodException.html">javadoc</a> documentation.
     * @throws IllegalAccessException  Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalAccessException.html">javadoc</a> documentation.
     * @throws IllegalArgumentException Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">javadoc</a> documentation.
     * @throws InvocationTargetException  Please refer to the <a href="http://docs.oracle.com/javase/7/docs/api/java/lang/reflect/InvocationTargetException.html">javadoc</a> documentation.
     */
    public static String getProperty(String name, Object target) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        String result = new String();
        Method method;
        
        String nameToUpperCase = checkCase(name);

        method = target.getClass().getMethod("get" + nameToUpperCase, (Class<?>) null);
        
        if (method != null) {
            result = (String) method.invoke(target, new Object[0]);
        }
        return result;
    }

    /**
     * TODO: Need to define the correct pattern of uppercase/lowercase letters
     * in the class methods.
     * @param name Name of the method
     * @return Correctly formatted name of the method
     */
    private static String checkCase(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //FIXME
    }
}
