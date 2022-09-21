package net.ryanland.colossus.sys.file.database.provider;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.sys.file.database.Stock;
import net.ryanland.colossus.sys.file.database.Supply;
import org.jetbrains.annotations.Nullable;

/**
 * Provider for serializing and deserializing a {@link Supply} from a {@link Stock}
 * @param <S> Serialized data type (e.g. JsonObject)
 */
public abstract class Provider<S> {

    public abstract String getStockName();

    public final Stock getStock() {
        return Colossus.getDatabaseDriver().get(getStockName());
    }

    public abstract Object serialize(Supply toSerialize);

    public abstract Supply deserialize(S data);
}
