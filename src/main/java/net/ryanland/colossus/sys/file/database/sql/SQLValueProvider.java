package net.ryanland.colossus.sys.file.database.sql;

import net.ryanland.colossus.sys.file.database.ValueProvider;

import java.sql.ResultSet;

public interface SQLValueProvider<T> extends ValueProvider<Object, ResultSet, T> {

    /**
     * The SQL data type (e.g. {@code datetime}, {@code varchar(25)}).
     * <p>This is injected into an SQL query if the column does not exist,
     * meaning you can also return something like {@code varchar(25) default "Hello World" not null}
     */
    String getSQLDataType();
}
