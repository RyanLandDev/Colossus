package net.ryanland.colossus.sys.file.database;

import net.ryanland.colossus.Colossus;

/**
 * Provider for serializing and deserializing a {@link Supply} from a {@link Stock}
 * @param <S1> Serialized data type to serialize to (e.g. JsonObject/HashMap{@literal <}String, Object>)
 * @param <S2> Serialized data type to deserialize (e.g. JsonObject/ResultSet)
 */
public abstract class Provider<S1, S2> {

    public abstract String getStockName();

    public final Stock getStock() {
        return Colossus.getDatabaseDriver().get(getStockName());
    }

    public abstract S1 serialize(Supply toSerialize);

    public abstract Supply deserialize(S2 data);
}
