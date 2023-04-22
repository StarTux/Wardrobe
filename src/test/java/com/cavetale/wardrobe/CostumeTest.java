package com.cavetale.wardrobe;

// import org.junit.Test;

public final class CostumeTest {
    // @Test
    public void test() {
        for (Costume costume : Costume.values()) {
            System.out.println(costume.name() + " " + costume.url);
        }
    }
}
