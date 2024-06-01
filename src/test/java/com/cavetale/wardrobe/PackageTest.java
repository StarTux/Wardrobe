package com.cavetale.wardrobe;

import org.junit.Test;

public final class PackageTest {
    /**
     * Count all packages.
     */
    public void test() {
        for (Package pkg : Package.values()) {
            System.out.println(pkg.wardrobeItems.size() + " " + pkg.name());
            for (WardrobeItem item : pkg.wardrobeItems) {
                System.out.println("  " + item.name() + " (" + item.getClass().getSimpleName() + ")");
            }
        }
    }
}
