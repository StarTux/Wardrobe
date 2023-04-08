package com.cavetale.wardrobe.sql;

import com.cavetale.core.util.Json;
import com.cavetale.inventory.storage.InventoryStorage;
import com.winthier.sql.SQLRow.Name;
import com.winthier.sql.SQLRow.NotNull;
import com.winthier.sql.SQLRow;
import java.util.Date;
import lombok.Data;

@Data @NotNull @Name("emotes")
public final class SQLEmote implements SQLRow {
    @Id private Integer id;
    @Unique @VarChar(40) private String name;
    @VarChar(255) private String displayName;
    @Default("0") private int frameTime;
    @Default("0") private int repeat;
    @LongText private String tag;
    private Date created;

    public SQLEmote() { }

    @Data
    public static final class Tag {
        InventoryStorage inventory;
    }

    public void storeTag(Tag theTag) {
        this.tag = Json.serialize(theTag);
    }

    public Tag parseTag() {
        return Json.deserialize(tag, Tag.class);
    }
}
