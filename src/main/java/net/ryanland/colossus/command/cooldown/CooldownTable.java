package net.ryanland.colossus.command.cooldown;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.ryanland.colossus.sys.database.entities.UserEntity;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cooldowns")
@Getter @NoArgsConstructor @AllArgsConstructor
@IdClass(CooldownTable.CooldownTableId.class)
public class CooldownTable extends UserEntity {

    @Id
    @Column(name = "command_name")
    private String commandName;

    @Id
    @Column(name = "command_type")
    // see CommandType
    private int commandType;

    private Date expires;

    public record CooldownTableId(String userId, String commandName, int commandType) {}

}
