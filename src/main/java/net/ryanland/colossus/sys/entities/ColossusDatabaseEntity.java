package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Provider;
import net.ryanland.colossus.sys.file.database.Table;

import java.util.function.Function;

import static net.ryanland.colossus.Colossus.getDatabaseDriver;
import static net.ryanland.colossus.Colossus.getProvider;

public interface ColossusDatabaseEntity<T extends ISnowflake> extends ColossusEntity {

    T getClient();

    /**
     * Gets the {@link Table} for this entity using {@link DatabaseDriver#get(ISnowflake)}
     */
    default Table<T> getTable() {
        return getDatabaseDriver().get(getClient());
    }

    /**
     * Updates the {@link Table} of this entity in the database with modified values.
     * @param table The table to update (with)
     * @see DatabaseDriver#updateTable(ISnowflake, Table)
     * @see #modifyTable(Function)
     * @see #updateValue(String, Object)
     * @see #modifyValue(String, Function)
     */
    default void updateTable(Table<T> table) {
        getDatabaseDriver().updateTable(getClient(), table);
    }

    /**
     * Updates the {@link Table} of this entity in the database with modified values.
     * <br>Provides the old value to help with modification.
     * @param tableModifier The table modifier function; providing the old value and returning the new value
     * @see DatabaseDriver#modifyTable(ISnowflake, Function)
     * @see #updateTable(Table)
     * @see #updateValue(String, Object)
     * @see #modifyValue(String, Function)
     */
    default void modifyTable(Function<Table<T>, Table<T>> tableModifier) {
        getDatabaseDriver().modifyTable(getClient(), tableModifier);
    }

    /**
     * Returns the value associated with the provided key in the {@link Table} of this entity, and performs an unchecked cast to it.<br>
     * Will be {@code null} if the value does not exist.
     * @see #getValue(String, Object)
     */
    default <R> R getValue(String key) {
        return getTable().get(key);
    }

    /**
     * Returns the value associated with the provided key in the {@link Table} of this entity, and performs an unchecked cast to it.
     * @param defaultValue If the value is null, this value will be returned instead.
     * @see #getValue(String)
     */
    default <R> R getValue(String key, Object defaultValue) {
        return getTable().get(key, defaultValue);
    }

    /**
     * Returns the raw (serialized) value associated with the provided key in the {@link Table} of this entity, and performs an unchecked cast to it.<br>
     * Will be {@code null} if the value does not exist.
     * @see #getRawValue(String, Object)
     */
    @SuppressWarnings("all")
    default <R> R getRawValue(String key) {
        return (R) getProvider(key).serialize(getValue(key));
    }

    /**
     * Returns the raw (serialized) value associated with the provided key in the {@link Table} of this entity, and performs an unchecked cast to it.
     * @param defaultValue If the value is null, this value will be returned instead.
     * @see #getRawValue(String)
     */
    @SuppressWarnings("all")
    default <R> R getRawValue(String key, Object defaultValue) {
        return (R) getProvider(key).serialize(getTable().get(key, defaultValue));
    }

    /**
     * Updates a value of the {@link Table} of this entity in the database to the provided value
     * @return The new value
     * @see #modifyValue(String, Function)
     */
    default <V> V updateValue(String key, V newValue) {
        updateTable(getTable().put(key, newValue));
        return newValue;
    }

    /**
     * Updates a value of the {@link Table} of this entity in the database to the provided raw (serialized) value
     * @return The raw value
     * @see #modifyRawValue(String, Function)
     */
    default <V> V updateRawValue(String key, V newValue) {
        updateValue(key, getProvider(key).deserialize(newValue));
        return newValue;
    }

    /**
     * Updates a value of the {@link Table} of this entity in the database with modified values.
     * <br>Provides the old value to help with modification.
     * @param valueModifier The value modifier function; providing the old value and returning the new value
     * @return The new value
     * @see #updateValue(String, Object)
     */
    default <V> V modifyValue(String key, Function<V, V> valueModifier) {
        Table<T> table = getTable();
        V newValue = valueModifier.apply(table.get(key));
        updateTable(table.put(key, newValue));
        return newValue;
    }

    /**
     * Updates a value of the {@link Table} of this entity in the database to the provided raw (serialized) value
     * <br>Provides the old raw value to help with modification.
     * @param valueModifier The value modifier function; providing the old raw value and returning the new raw value
     * @return The raw value
     * @see #updateRawValue(String, Object)
     */
    default <V, D> V modifyRawValue(String key, Function<V, V> valueModifier) {
        Provider<V, D> p = getProvider(key);
        return p.serialize(modifyValue(key, oldValue -> p.deserialize(valueModifier.apply(p.serialize(oldValue)))));
    }

}
