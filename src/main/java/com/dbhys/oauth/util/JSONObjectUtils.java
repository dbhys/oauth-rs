package com.dbhys.oauth.util;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dbhys.oauth.ParseException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


/**
 * copy from com.nimbusds oauth2-oidc-sdk
 *
 * JSON object helper methods for parsing and typed retrieval of member values.
 */
public final class JSONObjectUtils {


    /**
     * Returns {@code true} if the JSON object is defined and contains the
     * specified key.
     *
     * @param jsonObject The JSON object to check. May be {@code null}.
     * @param key        The key to check. Must not be {@code null}.
     *
     * @return {@code true} if the JSON object is defined and contains the
     *         specified key, else {@code false}.
     */
    public static boolean containsKey(final JSONObject jsonObject, final String key) {

        return jsonObject != null && jsonObject.containsKey(key);
    }


    /**
     * Parses a JSON object.
     *
     * <p>Specific JSON to Java entity mapping (as per JSON Simple):
     *
     * <ul>
     *     <li>JSON numbers mapped to {@code java.lang.Number}.
     *     <li>JSON integer numbers mapped to {@code long}.
     *     <li>JSON fraction numbers mapped to {@code double}.
     * </ul>
     *
     * @param s The JSON object string to parse. Must not be {@code null}.
     *
     * @return The JSON object.
     *
     * @throws ParseException If the string cannot be parsed to a JSON
     *                        object.
     */
    public static JSONObject parse(final String s)
            throws ParseException {

        Object o = JSONUtils.parseJSON(s);

        if (o instanceof JSONObject)
            return (JSONObject)o;
        else
            throw new ParseException("The JSON entity is not an object");
    }


    /**
     * Use {@link #parse(String)} instead.
     *
     * @param s The JSON object string to parse. Must not be {@code null}.
     *
     * @return The JSON object.
     *
     * @throws ParseException If the string cannot be parsed to a JSON
     *                        object.
     */
    @Deprecated
    public static JSONObject parseJSONObject(final String s)
            throws ParseException {

        return parse(s);
    }


    /**
     * Gets a generic member of a JSON object.
     *
     * @param o     The JSON object. Must not be {@code null}.
     * @param key   The JSON object member key. Must not be {@code null}.
     * @param clazz The expected class of the JSON object member value. Must
     *              not be {@code null}.
     *
     * @return The JSON object member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getGeneric(final JSONObject o, final String key, final Class<T> clazz)
            throws ParseException {

        if (! o.containsKey(key))
            throw new ParseException("Missing JSON object member with key \"" + key + "\"");

        if (o.get(key) == null)
            throw new ParseException("JSON object member with key \"" + key + "\" has null value");

        Object value = o.get(key);

        if (! clazz.isAssignableFrom(value.getClass()))
            throw new ParseException("Unexpected type of JSON object member with key \"" + key + "\"");

        return (T)value;
    }


    /**
     * Gets a boolean member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static boolean getBoolean(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, Boolean.class);
    }


    /**
     * Gets a boolean member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or.
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static boolean getBoolean(final JSONObject o, final String key, final boolean def)
            throws ParseException {

        if (o.get(key) != null) {
            return getBoolean(o, key);
        }

        return def;
    }


    /**
     * Gets an number member of a JSON object as {@code int}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static int getInt(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, Number.class).intValue();
    }


    /**
     * Gets an number member of a JSON object as {@code int}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static int getInt(final JSONObject o, final String key, final int def)
            throws ParseException {

        if (o.get(key) != null) {
            return getInt(o, key);
        }

        return def;
    }


    /**
     * Gets a number member of a JSON object as {@code long}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static long getLong(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, Number.class).longValue();
    }


    /**
     * Gets a number member of a JSON object as {@code long}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static long getLong(final JSONObject o, final String key, final long def)
            throws ParseException {

        if (o.get(key) != null) {
            return getLong(o, key);
        }

        return def;
    }


    /**
     * Gets a number member of a JSON object {@code float}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static float getFloat(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, Number.class).floatValue();
    }


    /**
     * Gets a number member of a JSON object {@code float}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static float getFloat(final JSONObject o, final String key, final float def)
            throws ParseException {

        if (o.get(key) != null) {
            return getFloat(o, key);
        }

        return def;
    }


    /**
     * Gets a number member of a JSON object as {@code double}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static double getDouble(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, Number.class).doubleValue();
    }


    /**
     * Gets a number member of a JSON object as {@code double}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static double getDouble(final JSONObject o, final String key, final double def)
            throws ParseException {

        if (o.get(key) != null) {
            return getDouble(o, key);
        }

        return def;
    }


    /**
     * Gets a number member of a JSON object as {@code java.lang.Number}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static Number getNumber(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, Number.class);
    }


    /**
     * Gets a number member of a JSON object as {@code java.lang.Number}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static Number getNumber(final JSONObject o, final String key, final Number def)
            throws ParseException {

        if (o.get(key) != null) {
            return getNumber(o, key);
        }

        return def;
    }


    /**
     * Gets a string member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static String getString(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, String.class);
    }


    /**
     * Gets a string member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static String getString(final JSONObject o, final String key, final String def)
            throws ParseException {

        if (o.get(key) != null) {
            return getString(o, key);
        }

        return def;
    }


    /**
     * Gets a string member of a JSON object as an enumerated object.
     *
     * @param o         The JSON object. Must not be {@code null}.
     * @param key       The JSON object member key. Must not be
     *                  {@code null}.
     * @param enumClass The enumeration class. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static <T extends Enum<T>> T getEnum(final JSONObject o,
                                                final String key,
                                                final Class<T> enumClass)
            throws ParseException {

        String value = getString(o, key);

        for (T en: enumClass.getEnumConstants()) {

            if (en.toString().equalsIgnoreCase(value))
                return en;
        }

        throw new ParseException("Unexpected value of JSON object member with key \"" + key + "\"");
    }


    /**
     * Gets a string member of a JSON object as an enumerated object.
     *
     * @param o         The JSON object. Must not be {@code null}.
     * @param key       The JSON object member key. Must not be
     *                  {@code null}.
     * @param enumClass The enumeration class. Must not be {@code null}.
     * @param def       The default value to return if the key is not
     *                  present or the value is {@code null}. May be
     *                  {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static <T extends Enum<T>> T getEnum(final JSONObject o,
                                                final String key,
                                                final Class<T> enumClass,
                                                final T def)
            throws ParseException {

        if (o.get(key) != null) {
            return getEnum(o, key, enumClass);
        }

        return def;
    }


    /**
     * Gets a string member of a JSON object as {@code java.net.URI}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static URI getURI(final JSONObject o, final String key)
            throws ParseException {

        try {
            return new URI(getGeneric(o, key, String.class));

        } catch (URISyntaxException e) {

            throw new ParseException(e.getMessage(), e);
        }
    }


    /**
     * Gets a string member of a JSON object as {@code java.net.URI}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static URI getURI(final JSONObject o, final String key, final URI def)
            throws ParseException {

        if (o.get(key) != null) {
            return getURI(o, key);
        }

        return def;
    }


    /**
     * Gets a string member of a JSON object as {@code java.net.URL}.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static URL getURL(final JSONObject o, final String key)
            throws ParseException {

        try {
            return new URL(getGeneric(o, key, String.class));

        } catch (MalformedURLException e) {

            throw new ParseException(e.getMessage(), e);
        }
    }

    /**
     * Gets a JSON array member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static JSONArray getJSONArray(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, JSONArray.class);
    }


    /**
     * Gets a JSON array member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static JSONArray getJSONArray(final JSONObject o, final String key, final JSONArray def)
            throws ParseException {

        if (o.get(key) != null) {
            return getJSONArray(o, key);
        }

        return def;
    }


    /**
     * Gets a list member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getList(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, List.class);
    }


    /**
     * Gets a list member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getList(final JSONObject o, final String key, final List<Object> def)
            throws ParseException {

        if (o.get(key) != null) {
            return getList(o, key);
        }

        return def;
    }


    /**
     * Gets a string array member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static String[] getStringArray(final JSONObject o, final String key)
            throws ParseException {

        List<Object> list = getList(o, key);

        try {
            return list.toArray(new String[0]);

        } catch (ArrayStoreException e) {

            throw new ParseException("JSON object member with key \"" + key + "\" is not an array of strings");
        }
    }


    /**
     * Gets a string array member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static String[] getStringArray(final JSONObject o, final String key, final String[] def)
            throws ParseException {

        if (o.get(key) != null) {
            return getStringArray(o, key);
        }

        return def;
    }


    /**
     * Gets a string list member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static List<String> getStringList(final JSONObject o, final String key)
            throws ParseException {

        return Arrays.asList(getStringArray(o, key));
    }


    /**
     * Gets a string list member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static List<String> getStringList(final JSONObject o, final String key, final List<String> def)
            throws ParseException {

        if (o.get(key) != null) {
            return getStringList(o, key);
        }

        return def;
    }


    /**
     * Gets a string array member of a JSON object as a string set.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static Set<String> getStringSet(final JSONObject o, final String key)
            throws ParseException {

        List<Object> list = getList(o, key);

        Set<String> set = new HashSet<>();

        for (Object item: list) {

            try {
                set.add((String)item);

            } catch (Exception e) {

                throw new ParseException("JSON object member with key \"" + key + "\" is not an array of strings");
            }

        }

        return set;
    }


    /**
     * Gets a string array member of a JSON object as a string set.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static Set<String> getStringSet(final JSONObject o, final String key, final Set<String> def)
            throws ParseException {

        if (o.get(key) != null) {
            return getStringSet(o, key);
        }

        return def;
    }


    /**
     * Gets a JSON object member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is missing, {@code null} or not
     *                        of the expected type.
     */
    public static JSONObject getJSONObject(final JSONObject o, final String key)
            throws ParseException {

        return getGeneric(o, key, JSONObject.class);
    }


    /**
     * Gets a JSON object member of a JSON object.
     *
     * @param o   The JSON object. Must not be {@code null}.
     * @param key The JSON object member key. Must not be {@code null}.
     * @param def The default value to return if the key is not present or
     *            the value is {@code null}. May be {@code null}.
     *
     * @return The member value.
     *
     * @throws ParseException If the value is not of the expected type.
     */
    public static JSONObject getJSONObject(final JSONObject o, final String key, final JSONObject def)
            throws ParseException {

        if (o.get(key) != null) {
            return getJSONObject(o, key);
        }

        return def;
    }


    /**
     * Prevents public instantiation.
     */
    private JSONObjectUtils() {}
}

