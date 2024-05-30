package com.cavetale.wardrobe.sql;

import com.winthier.sql.SQLRow;
import com.winthier.sql.SQLRow.Name;
import com.winthier.sql.SQLRow.NotNull;
import java.util.UUID;
import lombok.Data;

@Data @NotNull @Name("equipped")
public final class SQLEquipped implements SQLRow {
    @Id private Integer id;
    @Unique private UUID player;
    @Nullable @VarChar(255) private String companion;
    @Nullable @VarChar(255) private String helmet;
    @Nullable @VarChar(255) private String hand;
    @Nullable @VarChar(255) private String offhand;

    public SQLEquipped() { }

    public SQLEquipped(final UUID uuid) {
        this.player = uuid;
    }
}
