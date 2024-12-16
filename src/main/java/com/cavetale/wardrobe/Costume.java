package com.cavetale.wardrobe;

import com.cavetale.core.util.Json;
import com.cavetale.mytems.Mytems;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import static com.cavetale.mytems.util.Items.tooltip;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

@Getter @SuppressWarnings("LineLength")
public enum Costume implements WardrobeItem {
    WHITE_BUNNY(text("White Bunny Costume", WHITE),
                UUID.fromString("78749587-49cd-470b-b12a-666cde5d"),
                "ewogICJ0aW1lc3RhbXAiIDogMTYxNzQ3NTc1ODI1NSwKICAicHJvZmlsZUlkIiA6ICI3ODc0OTU4NzQ5Y2Q0NzBiYjEyYTY2NmNkZTVkODVmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYW50cmlYeEQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQyNzllOGIyYmExZTFhNDI4OTkwMzBjMjBhOThhYjQzNWQyOWRmNzVhNDZmOWM1ZTVlMGQ3MzkxOGQzOGIzNyIKICAgIH0KICB9Cn0=",
                "BpJRS19BaXiCVjv3A8jGagwL5yXnHiaKtdxvG5IDlvEEmZuyUD65c4Qrk2vcEubc22Tyzz2GEIrf7xZgDNBZaTtWtENaBXd7/gLTUyLyfCtYva0wd2bBbCY3K2MQfx6CjIjXZBTvz0z9nON7hACiADodX9Y8tZyoFsGTxzfAKtItPdkEQamzIqYjp2o/VPF1394NweAqUOfIjEcSJggBcgsjHwxqi9XVbQyO9dc4Dw8M62hOeb4DhJANZJlLzN8PSwmR8FWmgfHxY4SbkkZrw/wWTQ5+G7QI7vgBf4DRaYtqfyFUHrxlZ8enzyjkiLWhCfPhDJu1M+8uaGwq+w75HHmR6vUAi5xO+vipznr9rbwGXP0Mckq4CUWGFY1Dc3M4T/+mCXsavZyetu1QTpEUvSDoS92GFw+FhqC09fc70ctXrWVzA48KB6uYwYwnX1Nu9CBd807Rc843W4cSyZrlDrySPvlfT815r1kSXEj/E4wX2l5pkz91/kRey0BfPtJSajqyfNxBApFqnhzPn9G+zkxsNz8VsUYLRr6ZUUW7yK/Az5U/oyBFoCfLKuBTem5nV51C6Eq8LBGUOlNbAVQzl2zmhLsv2XdTkGx9p5Pgka+efG4RUufMShpy286YipAD7WlXpiXJvbJCYHK3Y99jiYs0diAnmBZdKt5+HMGOjd0="),
    PLAGUE_DOCTOR(text("Plague Doctor", DARK_GRAY),
                  UUID.fromString("3b80985a-e886-4efe-b078-862fd94a15d9"),
                  "ewogICJ0aW1lc3RhbXAiIDogMTYzNTg1ODMwNDQzNiwKICAicHJvZmlsZUlkIiA6ICIzYjgwOTg1YWU4ODY0ZWZlYjA3ODg2MmZkOTRhMTVkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLaWVyYW5fVmF4aWxpYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjNzNjMDQwYWIzNWE1N2E1OTkzZGVmODUwNTczNTFjMzhlNmU2OGZjZTgwMTZlMGQ4ZjYzMmUwNDY4Mjk1MiIKICAgIH0KICB9Cn0=",
                  "ByS6VQEvitB56JUPw3umnBJ/wXGcpAmrehjPx/XmEgbWUeIYqQsmAmVgf7/jS3GeG7StQv1bKPVh1fsmkNOhQMTvmWd4a9Wq70DzKE7FcSv3AawMq/JprcFzQYnHKWVvoyZ5je/6dIEshBykxFdEjABxRyBzaSUAWAU9vD4Tg7VvNehzsVMEvDmMljjl1SBozak8YeHi9Tm80CkSZIybubhjKDp1mOum5mTf0djK5sTkdmi/c/5gPBea2oBHt8DvYry+vTLBXLai8h22NAUBzK2VdhRzEu+mPh2u2eb0fD51+E0XoAnYiYeRWLiGXu3oioYDKMtYeN5rRtJ4YBL2UEMtS/OQ7vAsRGrIB1XK/x6orb7y5OPCo/EiMMBytN1400H8bYqD/txFZ/I/FgdjYzsRiVM+r/V8PVED8Uh1yfK0qkbZHDpFeY74mAUFjXrP3i6VZGxkW+jEAxI0lCfZim3B6+B8x6Kfs9qUFTzgrhHvXm02gYwZw0ErSEr8OC2kpkgObalWI0OAAr4ZZSCvSeVKDe2A65ScqvZ8U75/8CZBfKGW6z7y/yb6YKbY/dVDXRerUj195K5sy+YQdRsNxvKj612d4GEDM1jYjia/g3BAHcrMUh6Jkz7Of6EDDuxtlgVICmN1620bN+MNyVVkB+iZHk27P/VD3GxLfi7uFqg="),
    GREEN_COWBOY_FROG(text("Green Cowboy Frog", DARK_GREEN),
                      UUID.fromString("56675b22-32f0-4ee0-8917-9e9c9206cfe8"),
                      "ewogICJ0aW1lc3RhbXAiIDogMTYzNTk4Nzc2ODI0OSwKICAicHJvZmlsZUlkIiA6ICI1NjY3NWIyMjMyZjA0ZWUwODkxNzllOWM5MjA2Y2ZlOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVJbmRyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82Nzk5NDNhNjNkNWNhY2I5MmRhNDdmZjVkNDQxNzUzMThmNTg3YjA4ZWEwNmY5MjNmZTllMTM2M2RiN2U4NTIiCiAgICB9CiAgfQp9",
                      "V8fv+nz072m3/xcz94FzTlfBpWKybA8il74+NlxjRDbn/K8pdWtLjFClf/BH5IXn6fvECVuBE8iOmmOonDgYO93ty9uO/CEHMRb88gMBgaosRkaWSPSnMUyFnDh4JaZZvReTBlPlPQzFsp4EWJyRQgIxqvRQbf/v7b4nzZuNmz5Xqg+/9E8+cRFjMJJ5hxR8vtUtWjtaXvi03X+Dr4YXp+RSGSd3W/LGopl4PDxVsQJI9IgWh4nHGoQ7p3O9myJZOhiFCxkswOWJUL9TshDmLx4dpsQ2LYIXP7vaw0yKlUPmRmpAwz9T1LYqnw7vLjENlKn8Cee196fEHLbThK9G+r75R5ZyuJDdFz1WvJ+qfVgjLqgWRPfL+lBO42CcHqiID36+8PZltwOwG6LHFIGrE+AnkVYskFHtgnQNYilxqAlDzMEqNzLJbZwQqXBVLlCuJmthgOqFGCBcr3qjlncZM1OqtplOnMb71gzqyZXNDo5nsGEDw3fy0Bry/vbNwMSTpIVoDO9M6qkz5mFlJ+kKNtVuv/pb3wqoffruXDx5UXs2mksoxPJNKDJNLXlBIvSPAqyqoQZMhQ3HLNnMme1zWiq7isCYHxYOaPj3puu/04BjNdRmat20HkMvhEodJUq2Ph+FzoFpowSE2uyZA9OySmKkVuO6DihNOss58pzvveU="),
    ORANGE_COWBOY_FROG(text("Orange Cowboy Frog", GOLD),
                       UUID.fromString("276e046b-2403-43ed-9966-e494e7e6cb34"),
                       "ewogICJ0aW1lc3RhbXAiIDogMTYzNTk4NzY2MDY3MiwKICAicHJvZmlsZUlkIiA6ICIyNzZlMDQ2YjI0MDM0M2VkOTk2NmU0OTRlN2U2Y2IzNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBRFJBTlM3MTAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzEzOTBmYTYzMjQ2ZDVjM2UxY2Q3Zjk1MzY4NDg4YzNiNjIzNGNmNzgxNjM3NmZmMDU3NzY2MzlhYWFiMDQyOCIKICAgIH0KICB9Cn0=",
                       "s2JM6W7a2mBlao9BjJeG8lV+oe1OSB1BMdFu1gqMieCZM7vzrvouo7NhPrYPzgDeeIvDbCDVlF0/fOM2wpd7Ugg7Rllvc0TnNIpdPh0r1q/ifUZTQv+6g+GXyI1IDWpYAdNtEzunNwB0qjtPkcV+q+ulYeaDQRdO2Y0AtjJ0i+qt8rsvyfHun1o+jXrCIDTgNseYRx8C4VNyFgIUEOfSLSJFED7hrCGdB+BCkGUOo77qvMXHPc1srHqCb0j4hmJJ4NzVzu9IXbstAxuIVsuigAWU6zjFGEk+qWpkWeMYcS8p4j9+5Crrsqrj4pCQl7lfsfLt9ylxvpix26+vnPUUswd0+y3i1bI6IUq6baZT3MSuz0PSMrspEEA+741APN+UjfxYgvuEcyJqpGzAOzZqqy4CrGA/L2dGJJd5YV5nrf7XRIBtwGy63dDx1L/x/JUqkKYPZ5l1iysTbR9rqvgCPY3LB4/dR+TvlEMP9Ln1aS27O8yaLy2iMa9Jz0RFJ+MQA6pN9Z0p6PT3LZdMqqY73k8oFqmA1UAKpHQOozXrb6tBZyqxmzKGlt8hqIL4l3zuJNtlhbWqUBD4qCbO7sFZ70Tp6k/iag69V9cR3Es4iAi6be+TnEu0i1FOGK67I6B5ZlAsfTkdr2N3K3xcD0ZRbzISHorHhmyVwrzRq63giSY="),
    PIRATE_PARROT(text("Pirate Parrot", GOLD),
                  UUID.fromString("3b80985a-e886-4efe-b078-862fd94a15d9"),
                  "ewogICJ0aW1lc3RhbXAiIDogMTYzNTk2ODQxNTI3MywKICAicHJvZmlsZUlkIiA6ICIzYjgwOTg1YWU4ODY0ZWZlYjA3ODg2MmZkOTRhMTVkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLaWVyYW5fVmF4aWxpYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ4NjI4ZGYxY2RlYTk2MzAwYjQ2NmFiYThiM2I5NjI0NTVmZmRhYzE5YTZhNTE4ZWFiZTg1ZWUzYzY5Y2U0MCIKICAgIH0KICB9Cn0=",
                  "PlBDZ2LSu7uhA46SA+RUfx7XiRbBBJZeil7OrUDXO7yfnEKuY4OwEfuZfs3Ehe58QQbnD33jdP+nPsknyoGd+rcGKUs8JYmPVIHsauPG2/ePJtQ119HeMmuIy8SJMFhMDa/wsCCdeMnWPG1n8S/kUj24+mMS2Mxkb+qRWjUoxE11q4RjUJPcxqFfQrbd1IEkQQA73YYFoCbOS96p76xh/dv9740erGqV8f3r0OCwJrnmoye6vdXYOnHCFsQj98GpTOdN0plMPf7vyMQWOtTdpRtsQsSIOdnXUogqpMZkEUGxGEF00I1bgZtcyIR4ZgqUhItwuvNjJvXswgDX1ecei5aa80125JrXDEmb8YYTGqNmTvh1AKEkwnryHoG1bdQj8vkYnBIG8CJ10gHS1AKvhh1bSOPtZ+PMGpniVs3zL8Nq84DxVFhVlWg0uRV4hiDoQpVVIpfLJKOr/GmsNJF0dKkvp7Fk0U5f9WQ+sAG2UE27bHnT9ovwWOFNtjobpuUdHBqkF3jiLAYiBb/T0BsVveZJ5iUHVzvVwWXjuVvrQvVNVJUkVIL9hkBLiWx+8K1X5nCPRxujph4f0ZR0qc7XttIJuHg0etRlLM6cB3ioAPalUbE/SQTXh1AaOFtDcdbom47tBOheJwVXgOUsPuA6zbOr51T2wd2+7F8jvNLfQtA="),
    TUXEDO_CAT(text("Tuxedo Cat", GRAY),
               UUID.fromString("f104731f-9ca5-46b4-9936-3859def97cc6"),
               "ewogICJ0aW1lc3RhbXAiIDogMTYzNjMyNjM0MTkwMCwKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZiYjIxZTdjOTcyNGViYTgyZjBhOGMzYjA4YjFjNjQxOTdhZTk0Zjc3NzE4N2QyNjM4Zjc4OWZlOTA3MjQ4MyIKICAgIH0KICB9Cn0=",
               "nZ6FjwFZbSUhKFxM/8Z0DMs6OWYMhpRm8N2lnUECJYfLkn/oZ1Q1AdoditvrFT3tTQwJeqy5hp2DDwvaIu/s2TP/gqGCtcT3YmG9ak6fj0M72Ezhzv5vA3szK6KwBDmWoJ+YRr2kRmjMHA+UAyNOQ4kizUMi0VLNEZ8eUkKlbh234l0JpAwUmVONhxaNLvFRHy2QjyKCvP1grOZmnSKPxiY8b+xgkpaOwG9ljcZ2e6EuTn7pB7OGDYsp/p6/YWFf5GfNc54EdzwJFIbz4aB3ny0uYPA3IEvSee4rdhBEZko9De9IeGuycjv2GCInXVthNAhCrjwmEIo0NPWx+j+Gene1hpmO9TFBHMn25BBapjEwzSOOJmw4VaZsx6BOnHhDKfDuHBJv07xIH4DckUC6hLJDFOafMdM5MR2p1f7zB4bRh6qljCstK8OEaoBmUMdwOuSHGj3HCfJ4vSPNfbBUear63YE8AtDWxRqPX4Wn1tjfDfg+mqpOKeq+fFqADHBYFpt5hPStxMDB91rwQ9k66ntnrif/Z/NP1SY6XVCsnSjAesfE8x06IsRAT0HKC9O30R39B41rsLO5Cs3DbUe6bmrk/hyAYqdKLjqcnIlI7DCAtz7I2J6gszH5f093xjNxpHPle3gNg0Oo2OJdWM3WKBguG6ADbblC7eEESVE5vyg="),
    CAVETALE(text("Cavetale", YELLOW),
             UUID.fromString("38b7fcf5-8cd8-4654-98f8-64a98c286f1e"),
             "ewogICJ0aW1lc3RhbXAiIDogMTYxMjk5OTExNTY0NCwKICAicHJvZmlsZUlkIiA6ICIzOGI3ZmNmNThjZDg0NjU0OThmODY0YTk4YzI4NmYxZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXZldGFsZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMmU1ZWZiMDM2ZGFkYmJkODY5NzI4YzE0NzZhODMzZTRmMjkzYmU4MGJhYjA5NTYyYzZmZWE0ZjgwMDk4ZDU5IgogICAgfQogIH0KfQ==",
             "DR8fX+ufWmEE71zMX4kHQtYK5+f0x4cjt0uugEB2Af8UgfdPNQjZUpW9j0InI4WlLmrs6+sydUBw/t6zKZveUjHx4mmc/rhIGJX8ZiUFWlBHlF+HAxnTcHWMmgdl92++FY++mWcmJe5Iv5L1w0wSKqA+wklLcEh8nS2lBQj073WU74wG9s2g7t37xJklPCE0xjRPCx0ENz0v6O0qWWgUDS4f3ZF3Xcwg6KD1JZ0CAKw3wHoBiAK8Si2kLwBHT4OuZb0qMElH1XqjeZAeSfSX3GlGOxs35le7z2ClrDH0P+H12Lb45syLVcdRDx5TReVOcXnxUD8+yP+fQ+HnCiprK+e7RavGCNfAGPGJW+nBpJYQF9xZGXZhfXX+TzeZ6d0WWBa+5m5l41iB34VRSWhRO6wtpK7iS+VI6AW/UdbdV1vQO1BvzExnG6xuktwllr91j+qpuTzqehXtQbGzlyOxY/cFSuXfgeyc/WK1esoRoWXCpGkCUALCtrmF9bgIW4Xx/xPTgfXH1PbqFxUPz0jLEZ1DGt5fM6iaZw34uRpoZuHRO6o8eXYTODJmJB45MPbZo2+9n+ZzlC7WIm5NrBdqyUgUlOWz+4+r4CPZoouE81hlGxU0KPIoa7s8SZcz1jxSSZ+fNajY12fTfqob6xOR/usaewJIv293jEx1zk0NKWU="),
    ELF(text("Elf", RED),
        UUID.fromString("e793b2ca-7a2f-4126-a098-092d7c99417b"),
        "ewogICJ0aW1lc3RhbXAiIDogMTYzOTc1MzYyNTU3MCwKICAicHJvZmlsZUlkIiA6ICJlNzkzYjJjYTdhMmY0MTI2YTA5ODA5MmQ3Yzk5NDE3YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVfSG9zdGVyX01hbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80Mzc3Nzk5NjRiNmNhMzQ5ZjE1YTNjNGVjNTQ2MDZjMTZiNmUzMzIwMTY5ZDQwNTEyY2M5N2JjY2Q5MjQ2ZjBlIgogICAgfQogIH0KfQ==",
        "FXSugQ6cEf9a/2S9qh4f0v6TsZTl6SkjWEF2utUJPcKVDPGQdhxkrz+5owxkCaamJFSuNZGtoQf8olrqAI/JOB1freFnc/l0foPwF+TBbmk//ZoIFlyhDavQKWc+g3T9F70TstqH5P5LUwsg+LbNT5EahEWSwl7ZXnnOrBjZexsoY1HfK4d3RSs0WjX2xumznCruuGEYGHBLS4vqVTFKYUA9Fg+VR20Ndcw9isxVokw8rU9ZCE5Z3ZErqn++Wpmogz7c5q1cIC3n+bUzoCow1xYOFgM9Z7FtjjqV+/AgN5gY0wSfGZeJjLiKPqdtsvhsylvbSVCw/fkn1DTfVsLHLA+bKoR32UOmS5d8z6ZnWaPPKrRXSRi+0x9PdwdmyMfrDBSuO5Kzu76nbMVsAGIw0PWXIbD8Sq8GppYkykhlVCYIAYc/2SF2PEVLqS0lo5oWAfsg/upG860pLpAf9grY4SCgjC3fKS0/YgXu5rzMjYqJfKkx6e6GvSLSOeeBJVYSgK6cJmc5Rt3qlj/DmrPWSGMj5qcJnlfbYMiaKwF2Yf877s53wgQBIhfGs7IzKCQtLlXLqVXCqGXQIr4yWLeS2yxuVCDSA0YG1i2z9w1UkZM7JsvEDxm5sjJUVQfehyUXqgIAsWcB35l7OypOMDp7GZKvuX9ol9fEJjUo7Q+vUuk="),
    SANTA_POLAR_BEAR(text("Santa Polar Bear", RED),
                     UUID.fromString("f5d0b1ae416e-4a19-8121-4fdd-31e7305b"),
                     "ewogICJ0aW1lc3RhbXAiIDogMTYzOTg2OTM1MTcwNSwKICAicHJvZmlsZUlkIiA6ICJmNWQwYjFhZTQxNmU0YTE5ODEyMTRmZGQzMWU3MzA1YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXRjaFRoZVdhdmUxMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYWQ2ZGY0YjhlOThmN2VjZTQyNjk0MTA2MGUyMzQxMDJiYTI0NDMwN2JjNmViMjBlYjViOTFlNWY4OTRlY2YiCiAgICB9CiAgfQp9",
                     "qwbzny3KAu2tj1/zuyNtTF2b/RDod8hOhthnvgxviN+CmQ8yC0gvMsyu75JEBSgioTjOcCwhVyucYxQHk9VHA1XCj+8zpnv5k79otYaai7Ra/YGyOfCGC2fpTa5W2PGGhi7Lb1SjzrmsXthqdcuFPvZd08w42DXuEKGPTCf+mfbl4dAhWtv+lSHfGy0XAH2xjrPxhJt3Cv9A35vApjZTg63y+NtHnervTxlgRF6naEe4cBxYt3nyYoqIgVyRUqd/9Bh8DcFBFE74HJqfacw7MqlD+V3gvfyc+yY0D1lMRcOhplITkwmbeBZzzfT3BzjmEWF3cH6eEJsYv5/cZkNQqzlGnesK592+3todgEmbcFVpvAt74dCv/OxBTNd9wNMcLkJjSIvVQH6vzbEGi5zxzuEYcwq6MKVILKR6hL8VQ2HSsHrdnVoX8shuu3wYDqSya2O37n8qWGHl3uH0x6PzLan3urspZrhJHHM8lyIGkQcr1Mnm2hnhQXHdtkgtYOuVdsywthAspXUhzZ3BrZwy8YClD9CQsO7DuHxWEuPuwCdlAp3VAH3aqybX5ap2+tCPpRtNuqnUqwTJLvxTlGXkOHhcqmpSCgySysEbVaZ9Ez7U+2/NCP8lBs8Nux1/qdyeeAZQkLWhdO90MDP+/PePENGRLN6/1guPN8q1AYqrOWA="),
    WITCH(text("Witch", DARK_GREEN),
          UUID.fromString("3491f2b97c01-41a6-936b-1c22-a2a00fb7"),
          "ewogICJ0aW1lc3RhbXAiIDogMTY0NDA5MDYwODI4NiwKICAicHJvZmlsZUlkIiA6ICIzNDkxZjJiOTdjMDE0MWE2OTM2YjFjMjJhMmEwMGZiNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJKZXNzc3N1aGgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRiZTQ3MDc0MGU4ZmZkYzE4MDJhOGY5OTc5NzVkNDdmMDExOTc0MTJmNTU1YTQwODc3ODgxM2FmYzViYzQzZSIKICAgIH0KICB9Cn0=",
          "A/VFDGJjwphHw3x4SyXcAd3rmMwm+/DWfVcM/vXljtR9xlCcH1cJqfbmS+PyLrNDbtpuVNMTM9h/e/bwfSp4EavoGGz+tUKP9A8O67MC+ZO4CLXSyH/q6H9f6FZ/ymgnga7nStGbs2uh+8K1YmQHMWQVFM7xA2WhY83DojQuZlopTAQNG46w4L3gxbloSKezmW4t6ehWv6DvryY2kGFdrv/AzwVl0DD8kZkkKI1+7ssRyzdLQd/4C6LftpiG1t7lw6fXIEWJ3TCiIZOjS+SaBpKkYOK+YeIscPtawzqKFz9BjFaomf25ivtQbV6zyJeW9YSNU7kmPrDf2YZm0brqCC5tOHn02++7W/KfJA8pX2+o640H0i0cgBXMJK89Ggu0tQN24mrauBHs4qoHMJ766IUT+/gJMdMLnLFbJEoG9Q7IvRMdvVKDS1+8R5t1qvMrmke5C66H8Vrc3ADYEjrTtKXPPPgPo44JdsQxPT8lBg413M87skGTHNNBIlNahSVz7YY3vcvLeupXvDVjGuu7rN2eQ68Y/dHxhN17XcgIf/g4DR0/9iSE4YZZ5pfe9Vd2KfjTfhzu6Uj4hXHUb2VVdQ2JErrOGpoZZqBJT0D7AnJBMl7AhuHoKReJpu8kzbmlXd229Z/r5cK8XXbtjP14niDXKycb9lCV49VykdxRtZk=");

    public final Component displayName;
    public final UUID uuid;
    public final String texture;
    public final String signature;
    public final String json;
    public final URL url;
    static Map<UUID, PlayerProfile> backups = new HashMap<>();
    static Map<UUID, Costume> worn = new HashMap<>();

    @SuppressWarnings("unchecked")
    Costume(final Component displayName, final UUID uuid, final String texture, final String signature) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.texture = texture;
        this.signature = signature;
        this.json = new String(Base64.getDecoder().decode(texture));
        Map<String, Object> map = (Map<String, Object>) Json.deserialize(json, Object.class);
        Map<String, Object> map2 = map;
        map2 = (Map<String, Object>) map2.get("textures");
        map2 = (Map<String, Object>) map2.get("SKIN");
        try {
            this.url = new URI((String) map2.get("url")).toURL();
        } catch (MalformedURLException murle) {
            throw new IllegalStateException(murle);
        } catch (URISyntaxException urise) {
            throw new IllegalStateException(urise);
        }
    }

    @Override
    public Category getCategory() {
        return null;
    }

    public void wear(Player player) {
        if (!backups.containsKey(player.getUniqueId())) {
            backups.put(player.getUniqueId(), player.getPlayerProfile());
        }
        PlayerProfile profile = player.getPlayerProfile();
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(url, PlayerTextures.SkinModel.CLASSIC);
        profile.setTextures(textures);
        player.setPlayerProfile(profile);
        worn.put(player.getUniqueId(), this);
    }

    public static Costume remove(Player player) {
        PlayerProfile backup = backups.remove(player.getUniqueId());
        if (backup != null) {
            player.setPlayerProfile(backup);
        }
        return worn.remove(player.getUniqueId());
    }

    public PlayerProfile toPlayerProfile() {
        PlayerProfile profile = Bukkit.createProfile(uuid);
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(url);
        profile.setTextures(textures);
        return profile;
    }

    @Override
    public ItemStack toMenuItem() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.editMeta(m -> {
                SkullMeta meta = (SkullMeta) m;
                tooltip(meta, List.of(displayName,
                                      text("Player Skin", DARK_PURPLE),
                                      text("Click to equip", TextColor.color(0xFFFF00))));
                meta.setPlayerProfile(toPlayerProfile());
            });
        return itemStack;
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       text("Costume", DARK_GRAY),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Equip", GRAY)));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player)) {
            player.sendMessage(text("Costume removed!").color(WardrobeCommand.COLOR));
            return;
        }
        wear(player);
        player.sendMessage(text("Costume equipped: ").color(WardrobeCommand.COLOR)
                           .append(displayName));
    }

    @Override
    public boolean isWearing(Player player) {
        return false;
    }

    @Override
    public int getIndex() {
        return ordinal();
    }
}
