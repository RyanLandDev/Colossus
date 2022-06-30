package net.ryanland.colossus.sys.entities;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.Table;

import java.util.function.Function;

import static net.ryanland.colossus.Colossus.getDatabaseDriver;

public interface ColossusDatabaseEntity<T extends ISnowflake> extends ColossusEntity {

    T getClient();

    /**
     * Gets the table for this entity using {@link DatabaseDriver#get(ISnowflake)}
     */
    default Table<T> getTable() {
        return getDatabaseDriver().get(getClient());
    }

    /**
     * Updates the table of this entity in the database with modified values.
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
     * Updates the table of this entity in the database with modified values.
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
     * Returns the value associated with the provided key in the table of this entity, and performs an unchecked cast to it.<br>
     * Will be null if the value does not exist.
     * <br><br>
     * Equivalent of {@code getTable().get(key)}.
     * @see #getTable()
     * @see Table#get(String)
     * @see #getValue(String, Object)
     */
    default <R> R getValue(String key) {
        return getTable().get(key);
    }

    /**
     * Returns the value associated with the provided key in the table of this entity, and performs an unchecked cast to it.
     * <br><br>
     * Equivalent of {@code getTable().get(key, defaultValue)}.
     * @param defaultValue If the value is null, this value will be returned instead.
     * @see #getTable()
     * @see Table#get(String)
     * @see #getValue(String)
     */
    default <R> R getValue(String key, Object defaultValue) {
        return getTable().get(key, defaultValue);
    }

    /**
     * Updates a value of the table of this entity in the database with modified values.
     * @return The new value
     * @see #modifyValue(String, Function)
     * @see #updateTable(Table)
     * @see #modifyTable(Function)
     */
    default <V> V updateValue(String key, V newValue) {
        updateTable(getTable().put(key, newValue));
        return newValue;
    }

    /**
     * Updates a value of the table of this entity in the database with modified values.
     * <br>Provides the old value to help with modification.
     * @param valueModifier The value modifier function; providing the old value and returning the new value
     * @return The new value
     * @see #updateValue(String, Object)
     * @see #updateTable(Table)
     * @see #modifyTable(Function)
     */
    default <V> V modifyValue(String key, Function<V, V> valueModifier) {
        Table<T> table = getTable();
        V newValue = valueModifier.apply(table.get(key));
        updateTable(table.put(key, newValue));
        return newValue;
    }

}
