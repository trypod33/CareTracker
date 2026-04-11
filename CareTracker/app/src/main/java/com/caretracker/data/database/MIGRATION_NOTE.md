# Room Migration Note

Adding the `vital_readings` table requires a Room migration.

In your `AppDatabase`, increment the `version` to the next number and add:

```kotlin
val MIGRATION_X_Y = object : Migration(X, Y) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `vital_readings` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `personId` INTEGER NOT NULL,
                `readingDate` TEXT NOT NULL,
                `readingTimestamp` INTEGER NOT NULL,
                `readingType` TEXT NOT NULL,
                `bpSystolic` INTEGER,
                `bpDiastolic` INTEGER,
                `bpPosition` TEXT,
                `bloodSugar` REAL,
                `bsContext` TEXT,
                `heartRate` INTEGER,
                `hrContext` TEXT,
                `weightValue` REAL,
                `weightUnit` TEXT,
                `oxygenSat` INTEGER,
                `temperature` REAL,
                `tempUnit` TEXT,
                `notes` TEXT,
                `createdAt` INTEGER NOT NULL,
                FOREIGN KEY(`personId`) REFERENCES `persons`(`id`) ON DELETE CASCADE
            )
        """)
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_vital_readings_personId` ON `vital_readings` (`personId`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_vital_readings_readingType` ON `vital_readings` (`readingType`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_vital_readings_readingDate` ON `vital_readings` (`readingDate`)")
    }
}
```

Then add `MIGRATION_X_Y` to your `databaseBuilder` call and
add `VitalReadingDao` to the `@Database` entities and DAO list.

Also register `AddVitalReadingActivity` in `AndroidManifest.xml`:
```xml
<activity
    android:name=".ui.health.AddVitalReadingActivity"
    android:parentActivityName=".ui.health.HealthDashboardActivity" />
```
