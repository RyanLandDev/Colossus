package net.ryanland.colossus.sys.file.database;

/**
 * Provider for serializing and deserializing a specific value inside a {@link Supply}
 * @param <S1> Serialized data type to serialize to (e.g. JsonElement/Object)
 * @param <S2> Serialized data type to deserialize (e.g. JsonObject/ResultSet)
 * @param <T> Deserialized data type of value (e.g. for a balance it would be an Integer)
 */
public interface ValueProvider<S1, S2, T> {

    /**
     * The name of the {@link Stock} this {@link ValueProvider} is made for
     */
    String getStockName();

    /**
     * The name of the key in the {@link Supply}
     */
    String getKeyName();

    S1 serialize(T toSerialize);

    T deserialize(S2 data);
}
