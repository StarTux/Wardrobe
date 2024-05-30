package com.cavetale.wardrobe.sql;

import com.cavetale.wardrobe.Package;
import com.cavetale.wardrobe.WardrobePlugin;
import com.winthier.sql.SQLRow;
import com.winthier.sql.SQLRow.Name;
import com.winthier.sql.SQLRow.NotNull;
import com.winthier.sql.SQLRow.UniqueKey;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import lombok.Data;

@Data @NotNull @Name("packages")
@UniqueKey({"player", "packageName"})
public final class SQLPackage implements SQLRow {
    @Id private Integer id;
    private UUID player;
    @VarChar(40) private String packageName; // enum Package.name().toLowerCase()
    @Default("NOW()") private Date unlocked;

    public SQLPackage() { }

    public SQLPackage(final UUID uuid, final Package thePackage) {
        this.player = uuid;
        this.packageName = thePackage.name().toLowerCase();
        this.unlocked = new Date();
    }

    public Package getPackage() {
        try {
            return Package.valueOf(packageName.toUpperCase());
        } catch (Exception e) {
            WardrobePlugin.getInstance().getLogger().log(Level.SEVERE, "" + packageName, e);
            return null;
        }
    }
}
