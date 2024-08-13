package dev.ryanland.colossus.sys.database.entities;

import dev.ryanland.colossus.sys.database.HibernateManager;

public class ColossusEntity {

    /**
     * Saves the entity to the database.
     */
    public void save() {
        HibernateManager.save(this);
    }
}
