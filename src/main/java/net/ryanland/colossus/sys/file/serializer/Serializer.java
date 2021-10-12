package net.ryanland.colossus.sys.file.serializer;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for serializer classes.
 *
 * @param <S> Serialized value
 * @param <D> Deserialized value
 */
public interface Serializer<S, D> {

    S serialize(@NotNull D toSerialize);

    D deserialize(@NotNull S toDeserialize);
}
