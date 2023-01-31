package com.cavetale.wardrobe.mount;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MountResult {
    SUCCESS(""),
    ALREADY_MOUNTED("You are already mounted"),
    SERVER("You can only mount in survival mode"),
    LOCATION("You cannot fly here"),
    BLOCK_COLLISION("Your mount would get stuck in blocks"),
    UNKNOWN("Something went wrong"),
    ;

    public final String message;
}
