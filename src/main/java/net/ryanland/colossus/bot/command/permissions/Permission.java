package net.ryanland.colossus.bot.command.permissions;

import net.dv8tion.jda.api.entities.Member;

public enum Permission {
    BANNED("Banned", "banned") {
        @Override
        public boolean checkPermission(Member member) {
            return false;
        }
    },
    USER("User", "user") {
        @Override
        public boolean checkPermission(Member member) {
            return true;
        }
    },
    // -------------------------------------------------------------------
    SERVER_ADMIN("Server Admin", "serverAdmin") {
        @Override
        public boolean checkPermission(Member member) {
            return member.hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR);
        }
    },
    MANAGE_SERVER("Manage Server", "manageServer") {
        @Override
        public boolean checkPermission(Member member) {
            return member.hasPermission(net.dv8tion.jda.api.Permission.MANAGE_SERVER);
        }
    },
    // -------------------------------------------------------------------
    DEVELOPER("Developer", "developer") {
        @Override
        public boolean checkPermission(Member member) {
            return RankHandler.hasRank(member.getUser(), this);
        }
    },
    OWNER("Owner", "owner") {
        @Override
        public boolean checkPermission(Member member) {
            return RankHandler.hasRank(member.getUser(), this);
        }
    };

    private final String id;
    private final String name;
    private final int level;

    Permission(String name, String id) {
        this.id = id;
        this.name = name;
        this.level = ordinal();
    }

    public abstract boolean checkPermission(Member member);

    public boolean hasPermission(Member member) {
        return PermissionHandler.getHighestPermission(member).getLevel() >= getLevel();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
