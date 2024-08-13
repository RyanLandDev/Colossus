package net.ryanland.colossus.sys.database.entities;

import net.ryanland.colossus.sys.database.HibernateManager;

public class ColossusEntity {

    /**
     * Saves the entity to the database.
     */
    public void save() {
        HibernateManager.save(this);
    }
}
