package com.cavetale.wardrobe.sql;

import com.winthier.sql.SQLDatabase;
import org.junit.Test;

public final class SQLTest {
    @Test
    public void test() {
        System.out.println(SQLDatabase.testTableCreation(SQLPackage.class));
        System.out.println(SQLDatabase.testTableCreation(SQLEmote.class));
        System.out.println(SQLDatabase.testTableCreation(SQLEquipped.class));
    }
}
