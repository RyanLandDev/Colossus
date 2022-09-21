package net.ryanland.colossus.sys.oldfile.serializer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface SQLSerializer<D> extends aSerializer<PreparedStatement, ResultSet, D> {
}
