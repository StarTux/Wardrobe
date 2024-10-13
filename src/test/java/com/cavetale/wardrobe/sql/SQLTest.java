package com.cavetale.wardrobe.sql;

import com.winthier.sql.SQLDatabase;

public final class SQLTest {
    /**
     * Print all database tables.
     */
    public void test() {
        System.out.println(SQLDatabase.testTableCreation(SQLPackage.class));
        System.out.println(SQLDatabase.testTableCreation(SQLEmote.class));
        System.out.println(SQLDatabase.testTableCreation(SQLEquipped.class));
    }
}
