package net.ryanland.colossus.sys.file;

/**
 * Indicates how something should be stored.
 */
public enum StorageType {

    /**
     * The {@code Memory} (RAM) type is stored in variables
     */
    MEMORY,

    /**
     * The {@code Local} type is stored in local files
     */
    LOCAL,

    /**
     * The {@code External} type is stored in an external database
     */
    EXTERNAL
}
