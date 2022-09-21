package net.ryanland.colossus.sys.oldfile.serializer;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for serializer classes.
 *
 * @param <S1> Serialized value, type which the deserialized value is serialized to
 * @param <S2> Serialized value to deserialize
 * @param <D> Deserialized value
 */
public interface aSerializer<S1, S2, D> {

    S1 serialize(@NotNull D toSerialize);

    D deserialize(@NotNull S2 toDeserialize);
}
