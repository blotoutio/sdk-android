package com.blotout.storage;

import androidx.annotation.Nullable;

import java.util.List;

public interface IBOPreference {


    /**
     * Saves String property.
     *
     * @param key:   key to save string value
     * @param value: string value to be saved in preference
     */
    void saveString(String key, String value);

    /**
     * Saves integer property.
     *
     * @param key:   key to save integer value
     * @param value: integer value to be saved in preference
     */
    void saveInteger(String key, int value);

    /**
     * Saves boolean property.
     *
     * @param key:   key to save boolean value
     * @param value: boolean value to be saved in preference
     */
    void saveBoolean(String key, boolean value);

    /**
     * Saves float property.
     *
     * @param key:   key to save float value
     * @param value: float value to be saved in preference
     */
    void saveFloat(String key, float value);

    /**
     * Saves double property.
     *
     * @param key:   key to save double value
     * @param value: double value to be saved in preference
     */
    void saveDouble(String key, double value);

    /**
     * Saves long property.
     *
     * @param key:   key to save long value
     * @param value: long value to be saved in preference
     */
    void saveLong(String key, long value);

    @Nullable
    String getString(String key);

    @Nullable
    void removeKey(String key);

    /**
     * Gets the value associated with the key
     *
     * @param key:   key to get saved value
     * @param value: Specified value
     * @return: return null if specified key does not exist with the value
     */
    @Nullable
    String getString(String key, String value);


    /**
     * Generic method to retrieve Integer property
     *
     * @return null if the specified property key does not exists
     */
    int getInt(String key);

    /**
     * Generic method to retrieve Integer property
     *
     * @return null if the specified property key does not exists
     */
    int getInt(String key, int value);


    /**
     * Generic method to retrieve Boolean property
     *
     * @return null if the specified property key does not exists
     */
    boolean getBoolean(String key);

    /**
     * Generic method to retrieve Boolean property
     *
     * @return null if the specified property key does not exists
     */
    boolean getBoolean(String key, boolean value);


    /**
     * Generic method to retrieve Float property
     *
     * @return null if the specified property key does not exists
     */
    float getFloat(String key);

    /**
     * Generic method to retrieve Float property
     *
     * @return null if the specified property key does not exists
     */
    float getFloat(String key, float value);


    /**
     * Generic method to retrieve Double property
     *
     * @return null if the specified property key does not exists
     */
    double getDouble(String key);

    /**
     * Generic method to retrieve Double property
     *
     * @return null if the specified property key does not exists
     */
    double getDouble(String key, double value);


    /**
     * Generic method to retrieve Long property
     *
     * @return null if the specified property key does not exists
     */
    long getLong(String key);

    /**
     * Generic method to retrieve Long property
     *
     * @return null if the specified property key does not exists
     */
    long getLong(String key, long value);


    /**
     * Removes all the data from shared preferences.
     */
    void clear();

    void edit();

    void commit();

    void doEdit();

    void doCommit();

    /**
     * Generic method for saving list of custom types
     *
     * @param key:  Pref key
     * @param list: custom object list
     * @param <T>:  generic class type
     */
    <T> void saveListInPref(String key, List<T> list);

    /**
     * Genectic method to get list of custom types
     *
     * @param key: Pref key
     * @param <T>: generic class type
     * @return: returns list of custom type.
     */
    @Nullable
    <T> List<T> getSavedListFromPref(String key);
}
