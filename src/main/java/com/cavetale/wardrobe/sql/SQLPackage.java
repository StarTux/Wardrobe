package com.cavetale.wardrobe.sql;

import com.cavetale.wardrobe.Package;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Table(name = "packages",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"player", "package_name"})
       })
public final class SQLPackage {
    @Id
    private Integer id;
    @Column(nullable = false)
    private UUID player;
    @Column(nullable = false)
    private String packageName; // enum Package.name().toLowerCase()
    @Column(nullable = false)
    private Date unlocked;

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
            e.printStackTrace();
            return null;
        }
    }
}
