package net.ryanland.colossus.sys.entities;

import net.ryanland.colossus.sys.file.database.DatabaseDriver;
import net.ryanland.colossus.sys.file.database.PrimaryKey;
import net.ryanland.colossus.sys.file.database.Stock;
import net.ryanland.colossus.sys.file.database.Supply;

import java.util.function.Function;

import static net.ryanland.colossus.Colossus.getDatabaseDriver;

public interface ColossusDatabaseEntity extends ColossusEntity {

    String getStockName();

    PrimaryKey getPrimaryKey();

    /**
     * Gets the {@link Supply} for this entity using {@link Stock#get(PrimaryKey)}
     */
    default Supply getSupply() {
        return getDatabaseDriver().get(getStockName()).get(getPrimaryKey());
    }

    /**
     * Updates the {@link Supply} of this entity in the database with modified values.
     * @param supply The supply to update (with)
     * @see DatabaseDriver#updateSupply(Supply)
     * @see #modifyTable(Function)
     * @see #updateValue(String, Object)
     * @see #modifyValue(String, Function)
     */
    default void updateSupply(Supply supply) {
        getDatabaseDriver().updateSupply(supply);
    }

    /**
     * Updates the {@link Supply} of this entity in the database with modified values.
     * <br>Provides the old value to help with modification.
     * @param supplyModifier The supply modifier function; providing the old value and returning the new value
     * @see Stock#modify(PrimaryKey, Function)
     * @see #updateSupply(Supply)
     * @see #updateValue(String, Object)
     * @see #modifyValue(String, Function)
     */
    default void modifyTable(Function<Supply, Supply> supplyModifier) {
        getDatabaseDriver().get(getStockName()).modify(getPrimaryKey(), supplyModifier);
    }

    /**
     * Returns the value associated with the provided key in the {@link Supply} of this entity, and performs an unchecked cast to it.<br>
     * Will be {@code null} if the value does not exist.
     * @see #getValue(String, Object)
     */
    default <R> R getValue(String key) {
        return getSupply().get(key);
    }

    /**
     * Returns the value associated with the provided key in the {@link Supply} of this entity, and performs an unchecked cast to it.
     * @param defaultValue If the value is null, this value will be returned instead.
     * @see #getValue(String)
     */
    @SuppressWarnings("unchecked")
    default <R> R getValue(String key, Object defaultValue) {
        return (R) getSupply().get(key, defaultValue);
    }

    /**
     * Updates a value of the {@link Supply} of this entity in the database to the provided value
     * @return The new value
     * @see #modifyValue(String, Function)
     */
    default <V> V updateValue(String key, V newValue) {
        getSupply().push(key, newValue);
        return newValue;
    }

    /**
     * Updates a value of the {@link Supply} of this entity in the database with modified values.
     * <br>Provides the old value to help with modification.
     * @param valueModifier The value modifier function; providing the old value and returning the new value
     * @return The new value
     * @see #updateValue(String, Object)
     */
    default <V> V modifyValue(String key, Function<V, V> valueModifier) {
        Supply supply = getSupply();
        V newValue = valueModifier.apply(supply.get(key));
        supply.push(key, newValue);
        return newValue;
    }

    /**
     * Increases a value of type integer of the {@link Supply} of this entity in the database by a provided amount.
     * @param incrementer The amount to increment by
     * @return The new value
     * @see #modifyValue(String, Function)
     */
    default int increaseValue(String key, int incrementer) {
        return modifyValue(key, value -> value + incrementer);
    }

}
