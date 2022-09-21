package net.ryanland.colossus.sys.oldfile.serializer;

/**
 * Interface for serializer classes.
 *
 * @param <S> Serialized value
 * @param <D> Deserialized value
 */
public interface Serializer<S, D> extends aSerializer<S, S, D> {
}
