package net.ryanland.colossus.sys.oldfile.provider;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.sys.entities.ColossusDatabaseEntity;
import net.ryanland.colossus.sys.oldfile.database.DatabaseDriver;
import net.ryanland.colossus.sys.oldfile.database.Table;
import net.ryanland.colossus.sys.oldfile.serializer.Serializer;

import java.util.function.Function;

/**
 * A provider is used in a {@link DatabaseDriver} to help retrieve values by setting a {@link Serializer} for it.<br>
 * Register {@link dProvider Providers} using {@link net.ryanland.colossus.ColossusBuilder#registerProviders(dProvider...)}
 * @param <S> The serialized type (e.g. String)
 * @param <D> The deserialized type (e.g. Command)
 */
public record dProvider<S, D>(String key, Serializer<S, D> serializer) {

    private <T extends ISnowflake> ColossusDatabaseEntity<T> databaseEntityOf(T client) {
        return () -> client;
    }

    public D deserialize(S toDeserialize) {
        return serializer.deserialize(toDeserialize);
    }

    public S serialize(D toSerialize) {
        return serializer.serialize(toSerialize);
    }

    /**
     * Gets this provider's deserialized value of the provided client
     */
    public <T extends ISnowflake> D get(T client) {
        return deserialize(databaseEntityOf(client).getRawValue(key));
    }

    /**
     * Updates a value of the {@link Table} of the provided client in the database to the serialized version of the provided value
     * @return The new value
     * @see #modify(ISnowflake, Function)
     */
    public <T extends ISnowflake> D update(T client, D newValue) {
        databaseEntityOf(client).updateRawValue(key, serialize(newValue));
        return newValue;
    }

    /**
     * Updates a value of the {@link Table} of the provided client in the database to the serialized version of the provided value
     * <br>Provides the old deserialized value to help with modification.
     * @param valueModifier The value modifier function; providing the old deserialized value and returning the new deserialized value
     * @return The new value
     * @see #update(ISnowflake, Object)
     */
    @SuppressWarnings("all")
    public <T extends ISnowflake> D modify(T client, Function<D, D> valueModifier) {
        return deserialize(databaseEntityOf(client).modifyRawValue(key,
            oldValue -> serialize(valueModifier.apply(deserialize((S) oldValue)))));
    }
}
