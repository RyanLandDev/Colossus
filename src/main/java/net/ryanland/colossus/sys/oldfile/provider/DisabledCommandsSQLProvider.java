package net.ryanland.colossus.sys.oldfile.provider;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisabledCommandsSQLProvider extends ListProvider<BasicCommand, Object> {

    @Override
    public String getKey() {
        return "disabled_commands";
    }

    @Override
    public List<BasicCommand> retrieve(Object param) {
        ResultSet result = Colossus.getSQLDatabaseDriver().query("SELECT * FROM disabled_commands");
        List<BasicCommand> commands = new ArrayList<>();
        try {
            while (result.next()) {
                CommandType commandType = CommandType.of(result.getInt("command_type"));
                BasicCommand command = commandType.getCommand(result.getString("command_name"));
                commands.add(command);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return commands;
    }

    @Override
    public void update(List<BasicCommand> newValue) {
        Colossus.getSQLDatabaseDriver().query("DELETE FROM disabled_commands");
        newValue.forEach(this::add);
    }

    @Override
    public boolean contains(BasicCommand value) {
        try {
            PreparedStatement ps = Colossus.getSQLDatabaseDriver().getConnection()
                .prepareStatement("SELECT * FROM disabled_commands WHERE command_name = ? AND command_type = ?");
            ps.setString(1, value.getName());
            ps.setInt(2, value.getCommandType().getId());
            ResultSet result = ps.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void add(BasicCommand value) {
        try {
            PreparedStatement ps = Colossus.getSQLDatabaseDriver().getConnection()
                .prepareStatement("INSERT INTO disabled_commands (command_name, command_type) VALUES (?, ?)");
            ps.setString(1, value.getName());
            ps.setInt(2, value.getCommandType().getId());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void remove(BasicCommand value) {
        try {
            PreparedStatement ps = Colossus.getSQLDatabaseDriver().getConnection()
                .prepareStatement("DELETE FROM disabled_commands WHERE command_name = ? AND command_type = ?");
            ps.setString(1, value.getName());
            ps.setInt(2, value.getCommandType().getId());
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
