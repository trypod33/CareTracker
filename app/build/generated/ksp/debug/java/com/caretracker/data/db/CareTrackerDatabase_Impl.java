package com.caretracker.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.caretracker.data.dao.BloodPressureDao;
import com.caretracker.data.dao.BloodPressureDao_Impl;
import com.caretracker.data.dao.BloodSugarDao;
import com.caretracker.data.dao.BloodSugarDao_Impl;
import com.caretracker.data.dao.CalendarDao;
import com.caretracker.data.dao.CalendarDao_Impl;
import com.caretracker.data.dao.HabitDao;
import com.caretracker.data.dao.HabitDao_Impl;
import com.caretracker.data.dao.HealthDao;
import com.caretracker.data.dao.HealthDao_Impl;
import com.caretracker.data.dao.MedicationDao;
import com.caretracker.data.dao.MedicationDao_Impl;
import com.caretracker.data.dao.MoodDao;
import com.caretracker.data.dao.MoodDao_Impl;
import com.caretracker.data.dao.TaskDao;
import com.caretracker.data.dao.TaskDao_Impl;
import com.caretracker.data.dao.UserDao;
import com.caretracker.data.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CareTrackerDatabase_Impl extends CareTrackerDatabase {
  private volatile UserDao _userDao;

  private volatile HabitDao _habitDao;

  private volatile HealthDao _healthDao;

  private volatile MedicationDao _medicationDao;

  private volatile CalendarDao _calendarDao;

  private volatile TaskDao _taskDao;

  private volatile MoodDao _moodDao;

  private volatile BloodPressureDao _bloodPressureDao;

  private volatile BloodSugarDao _bloodSugarDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `username` TEXT NOT NULL, `email` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `displayName` TEXT, `avatarColor` TEXT NOT NULL, `role` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `habits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `category` TEXT NOT NULL, `color` TEXT NOT NULL, `icon` TEXT NOT NULL, `frequency` TEXT NOT NULL, `targetCount` INTEGER NOT NULL, `sortOrder` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `habit_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `habitId` INTEGER NOT NULL, `loggedDate` TEXT NOT NULL, `count` INTEGER NOT NULL, `note` TEXT, `loggedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `health_entries` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `entryDate` TEXT NOT NULL, `weight` REAL, `weightUnit` TEXT NOT NULL, `bloodPressureSystolic` INTEGER, `bloodPressureDiastolic` INTEGER, `heartRate` INTEGER, `bloodSugar` REAL, `bloodSugarUnit` TEXT NOT NULL, `sleepHours` REAL, `sleepQuality` INTEGER, `mood` INTEGER, `energy` INTEGER, `steps` INTEGER, `waterOz` REAL, `calories` INTEGER, `exerciseMinutes` INTEGER, `notes` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medications` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `genericName` TEXT, `dosage` TEXT, `dosageUnit` TEXT NOT NULL, `form` TEXT NOT NULL, `frequency` TEXT, `timesPerDay` INTEGER NOT NULL, `scheduledTimes` TEXT, `withFood` INTEGER NOT NULL, `instructions` TEXT, `prescriber` TEXT, `pharmacy` TEXT, `rxNumber` TEXT, `color` TEXT NOT NULL, `currentCount` INTEGER NOT NULL, `pillsPerRefill` INTEGER, `refillReminderAt` INTEGER NOT NULL, `lastRefillDate` TEXT, `nextRefillDate` TEXT, `startDate` TEXT, `endDate` TEXT, `sortOrder` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `med_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `medicationId` INTEGER NOT NULL, `takenAt` INTEGER NOT NULL, `takenDate` TEXT NOT NULL, `scheduledTime` TEXT, `status` TEXT NOT NULL, `note` TEXT, `doseTaken` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `calendar_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `category` TEXT NOT NULL, `color` TEXT NOT NULL, `startDatetime` INTEGER NOT NULL, `endDatetime` INTEGER, `allDay` INTEGER NOT NULL, `location` TEXT, `reminderMinutes` INTEGER NOT NULL, `recurrence` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `priority` TEXT NOT NULL, `status` TEXT NOT NULL, `category` TEXT NOT NULL, `dueDate` TEXT, `dueTime` TEXT, `estimatedMinutes` INTEGER, `tags` TEXT, `sortOrder` INTEGER NOT NULL, `completedAt` INTEGER, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mood_journal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `entryDate` TEXT NOT NULL, `moodScore` INTEGER, `content` TEXT, `tags` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vital_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `entryDate` TEXT NOT NULL, `recordedAt` INTEGER NOT NULL, `type` TEXT NOT NULL, `value` REAL, `value2` REAL, `unit` TEXT NOT NULL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blood_pressure_readings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entryId` INTEGER NOT NULL, `systolic` INTEGER NOT NULL, `diastolic` INTEGER NOT NULL, `readingTime` TEXT NOT NULL, `label` TEXT NOT NULL, FOREIGN KEY(`entryId`) REFERENCES `health_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_blood_pressure_readings_entryId` ON `blood_pressure_readings` (`entryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blood_sugar_readings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `entryId` INTEGER NOT NULL, `value` REAL NOT NULL, `readingTime` TEXT NOT NULL, `label` TEXT NOT NULL, FOREIGN KEY(`entryId`) REFERENCES `health_entries`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_blood_sugar_readings_entryId` ON `blood_sugar_readings` (`entryId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '725866bb92488f09ac960bfdebaa6bd4')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `habits`");
        db.execSQL("DROP TABLE IF EXISTS `habit_logs`");
        db.execSQL("DROP TABLE IF EXISTS `health_entries`");
        db.execSQL("DROP TABLE IF EXISTS `medications`");
        db.execSQL("DROP TABLE IF EXISTS `med_logs`");
        db.execSQL("DROP TABLE IF EXISTS `calendar_events`");
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        db.execSQL("DROP TABLE IF EXISTS `mood_journal`");
        db.execSQL("DROP TABLE IF EXISTS `vital_logs`");
        db.execSQL("DROP TABLE IF EXISTS `blood_pressure_readings`");
        db.execSQL("DROP TABLE IF EXISTS `blood_sugar_readings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(9);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("displayName", new TableInfo.Column("displayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("avatarColor", new TableInfo.Column("avatarColor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.caretracker.data.entities.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsHabits = new HashMap<String, TableInfo.Column>(12);
        _columnsHabits.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("icon", new TableInfo.Column("icon", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("targetCount", new TableInfo.Column("targetCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabits.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHabits = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHabits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHabits = new TableInfo("habits", _columnsHabits, _foreignKeysHabits, _indicesHabits);
        final TableInfo _existingHabits = TableInfo.read(db, "habits");
        if (!_infoHabits.equals(_existingHabits)) {
          return new RoomOpenHelper.ValidationResult(false, "habits(com.caretracker.data.entities.HabitEntity).\n"
                  + " Expected:\n" + _infoHabits + "\n"
                  + " Found:\n" + _existingHabits);
        }
        final HashMap<String, TableInfo.Column> _columnsHabitLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsHabitLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabitLogs.put("habitId", new TableInfo.Column("habitId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabitLogs.put("loggedDate", new TableInfo.Column("loggedDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabitLogs.put("count", new TableInfo.Column("count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabitLogs.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHabitLogs.put("loggedAt", new TableInfo.Column("loggedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHabitLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHabitLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHabitLogs = new TableInfo("habit_logs", _columnsHabitLogs, _foreignKeysHabitLogs, _indicesHabitLogs);
        final TableInfo _existingHabitLogs = TableInfo.read(db, "habit_logs");
        if (!_infoHabitLogs.equals(_existingHabitLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "habit_logs(com.caretracker.data.entities.HabitLogEntity).\n"
                  + " Expected:\n" + _infoHabitLogs + "\n"
                  + " Found:\n" + _existingHabitLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsHealthEntries = new HashMap<String, TableInfo.Column>(20);
        _columnsHealthEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("entryDate", new TableInfo.Column("entryDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("weight", new TableInfo.Column("weight", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("weightUnit", new TableInfo.Column("weightUnit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("bloodPressureSystolic", new TableInfo.Column("bloodPressureSystolic", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("bloodPressureDiastolic", new TableInfo.Column("bloodPressureDiastolic", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("heartRate", new TableInfo.Column("heartRate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("bloodSugar", new TableInfo.Column("bloodSugar", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("bloodSugarUnit", new TableInfo.Column("bloodSugarUnit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("sleepHours", new TableInfo.Column("sleepHours", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("sleepQuality", new TableInfo.Column("sleepQuality", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("mood", new TableInfo.Column("mood", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("energy", new TableInfo.Column("energy", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("steps", new TableInfo.Column("steps", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("waterOz", new TableInfo.Column("waterOz", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("calories", new TableInfo.Column("calories", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("exerciseMinutes", new TableInfo.Column("exerciseMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEntries.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHealthEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHealthEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHealthEntries = new TableInfo("health_entries", _columnsHealthEntries, _foreignKeysHealthEntries, _indicesHealthEntries);
        final TableInfo _existingHealthEntries = TableInfo.read(db, "health_entries");
        if (!_infoHealthEntries.equals(_existingHealthEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "health_entries(com.caretracker.data.entities.HealthEntryEntity).\n"
                  + " Expected:\n" + _infoHealthEntries + "\n"
                  + " Found:\n" + _existingHealthEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsMedications = new HashMap<String, TableInfo.Column>(26);
        _columnsMedications.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("genericName", new TableInfo.Column("genericName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("dosage", new TableInfo.Column("dosage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("dosageUnit", new TableInfo.Column("dosageUnit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("form", new TableInfo.Column("form", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("frequency", new TableInfo.Column("frequency", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("timesPerDay", new TableInfo.Column("timesPerDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("scheduledTimes", new TableInfo.Column("scheduledTimes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("withFood", new TableInfo.Column("withFood", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("instructions", new TableInfo.Column("instructions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("prescriber", new TableInfo.Column("prescriber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("pharmacy", new TableInfo.Column("pharmacy", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("rxNumber", new TableInfo.Column("rxNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("currentCount", new TableInfo.Column("currentCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("pillsPerRefill", new TableInfo.Column("pillsPerRefill", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("refillReminderAt", new TableInfo.Column("refillReminderAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("lastRefillDate", new TableInfo.Column("lastRefillDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("nextRefillDate", new TableInfo.Column("nextRefillDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("startDate", new TableInfo.Column("startDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("endDate", new TableInfo.Column("endDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedications = new TableInfo("medications", _columnsMedications, _foreignKeysMedications, _indicesMedications);
        final TableInfo _existingMedications = TableInfo.read(db, "medications");
        if (!_infoMedications.equals(_existingMedications)) {
          return new RoomOpenHelper.ValidationResult(false, "medications(com.caretracker.data.entities.MedicationEntity).\n"
                  + " Expected:\n" + _infoMedications + "\n"
                  + " Found:\n" + _existingMedications);
        }
        final HashMap<String, TableInfo.Column> _columnsMedLogs = new HashMap<String, TableInfo.Column>(8);
        _columnsMedLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("medicationId", new TableInfo.Column("medicationId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("takenAt", new TableInfo.Column("takenAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("takenDate", new TableInfo.Column("takenDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("scheduledTime", new TableInfo.Column("scheduledTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedLogs.put("doseTaken", new TableInfo.Column("doseTaken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedLogs = new TableInfo("med_logs", _columnsMedLogs, _foreignKeysMedLogs, _indicesMedLogs);
        final TableInfo _existingMedLogs = TableInfo.read(db, "med_logs");
        if (!_infoMedLogs.equals(_existingMedLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "med_logs(com.caretracker.data.entities.MedLogEntity).\n"
                  + " Expected:\n" + _infoMedLogs + "\n"
                  + " Found:\n" + _existingMedLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsCalendarEvents = new HashMap<String, TableInfo.Column>(14);
        _columnsCalendarEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("startDatetime", new TableInfo.Column("startDatetime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("endDatetime", new TableInfo.Column("endDatetime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("allDay", new TableInfo.Column("allDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("reminderMinutes", new TableInfo.Column("reminderMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("recurrence", new TableInfo.Column("recurrence", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCalendarEvents.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCalendarEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCalendarEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCalendarEvents = new TableInfo("calendar_events", _columnsCalendarEvents, _foreignKeysCalendarEvents, _indicesCalendarEvents);
        final TableInfo _existingCalendarEvents = TableInfo.read(db, "calendar_events");
        if (!_infoCalendarEvents.equals(_existingCalendarEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "calendar_events(com.caretracker.data.entities.CalendarEventEntity).\n"
                  + " Expected:\n" + _infoCalendarEvents + "\n"
                  + " Found:\n" + _existingCalendarEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(14);
        _columnsTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dueDate", new TableInfo.Column("dueDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dueTime", new TableInfo.Column("dueTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("estimatedMinutes", new TableInfo.Column("estimatedMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("tags", new TableInfo.Column("tags", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.caretracker.data.entities.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsMoodJournal = new HashMap<String, TableInfo.Column>(7);
        _columnsMoodJournal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("entryDate", new TableInfo.Column("entryDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("moodScore", new TableInfo.Column("moodScore", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("content", new TableInfo.Column("content", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("tags", new TableInfo.Column("tags", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodJournal.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMoodJournal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMoodJournal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMoodJournal = new TableInfo("mood_journal", _columnsMoodJournal, _foreignKeysMoodJournal, _indicesMoodJournal);
        final TableInfo _existingMoodJournal = TableInfo.read(db, "mood_journal");
        if (!_infoMoodJournal.equals(_existingMoodJournal)) {
          return new RoomOpenHelper.ValidationResult(false, "mood_journal(com.caretracker.data.entities.MoodJournalEntity).\n"
                  + " Expected:\n" + _infoMoodJournal + "\n"
                  + " Found:\n" + _existingMoodJournal);
        }
        final HashMap<String, TableInfo.Column> _columnsVitalLogs = new HashMap<String, TableInfo.Column>(9);
        _columnsVitalLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("entryDate", new TableInfo.Column("entryDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("recordedAt", new TableInfo.Column("recordedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("value", new TableInfo.Column("value", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("value2", new TableInfo.Column("value2", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitalLogs.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVitalLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVitalLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVitalLogs = new TableInfo("vital_logs", _columnsVitalLogs, _foreignKeysVitalLogs, _indicesVitalLogs);
        final TableInfo _existingVitalLogs = TableInfo.read(db, "vital_logs");
        if (!_infoVitalLogs.equals(_existingVitalLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "vital_logs(com.caretracker.data.entities.VitalLogEntity).\n"
                  + " Expected:\n" + _infoVitalLogs + "\n"
                  + " Found:\n" + _existingVitalLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsBloodPressureReadings = new HashMap<String, TableInfo.Column>(6);
        _columnsBloodPressureReadings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodPressureReadings.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodPressureReadings.put("systolic", new TableInfo.Column("systolic", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodPressureReadings.put("diastolic", new TableInfo.Column("diastolic", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodPressureReadings.put("readingTime", new TableInfo.Column("readingTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodPressureReadings.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBloodPressureReadings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBloodPressureReadings.add(new TableInfo.ForeignKey("health_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBloodPressureReadings = new HashSet<TableInfo.Index>(1);
        _indicesBloodPressureReadings.add(new TableInfo.Index("index_blood_pressure_readings_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        final TableInfo _infoBloodPressureReadings = new TableInfo("blood_pressure_readings", _columnsBloodPressureReadings, _foreignKeysBloodPressureReadings, _indicesBloodPressureReadings);
        final TableInfo _existingBloodPressureReadings = TableInfo.read(db, "blood_pressure_readings");
        if (!_infoBloodPressureReadings.equals(_existingBloodPressureReadings)) {
          return new RoomOpenHelper.ValidationResult(false, "blood_pressure_readings(com.caretracker.data.entities.BloodPressureReadingEntity).\n"
                  + " Expected:\n" + _infoBloodPressureReadings + "\n"
                  + " Found:\n" + _existingBloodPressureReadings);
        }
        final HashMap<String, TableInfo.Column> _columnsBloodSugarReadings = new HashMap<String, TableInfo.Column>(5);
        _columnsBloodSugarReadings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodSugarReadings.put("entryId", new TableInfo.Column("entryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodSugarReadings.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodSugarReadings.put("readingTime", new TableInfo.Column("readingTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBloodSugarReadings.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBloodSugarReadings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBloodSugarReadings.add(new TableInfo.ForeignKey("health_entries", "CASCADE", "NO ACTION", Arrays.asList("entryId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBloodSugarReadings = new HashSet<TableInfo.Index>(1);
        _indicesBloodSugarReadings.add(new TableInfo.Index("index_blood_sugar_readings_entryId", false, Arrays.asList("entryId"), Arrays.asList("ASC")));
        final TableInfo _infoBloodSugarReadings = new TableInfo("blood_sugar_readings", _columnsBloodSugarReadings, _foreignKeysBloodSugarReadings, _indicesBloodSugarReadings);
        final TableInfo _existingBloodSugarReadings = TableInfo.read(db, "blood_sugar_readings");
        if (!_infoBloodSugarReadings.equals(_existingBloodSugarReadings)) {
          return new RoomOpenHelper.ValidationResult(false, "blood_sugar_readings(com.caretracker.data.entities.BloodSugarReadingEntity).\n"
                  + " Expected:\n" + _infoBloodSugarReadings + "\n"
                  + " Found:\n" + _existingBloodSugarReadings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "725866bb92488f09ac960bfdebaa6bd4", "c31947f6877b98fa54c5672bf61b0a76");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","habits","habit_logs","health_entries","medications","med_logs","calendar_events","tasks","mood_journal","vital_logs","blood_pressure_readings","blood_sugar_readings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `habits`");
      _db.execSQL("DELETE FROM `habit_logs`");
      _db.execSQL("DELETE FROM `health_entries`");
      _db.execSQL("DELETE FROM `medications`");
      _db.execSQL("DELETE FROM `med_logs`");
      _db.execSQL("DELETE FROM `calendar_events`");
      _db.execSQL("DELETE FROM `tasks`");
      _db.execSQL("DELETE FROM `mood_journal`");
      _db.execSQL("DELETE FROM `vital_logs`");
      _db.execSQL("DELETE FROM `blood_pressure_readings`");
      _db.execSQL("DELETE FROM `blood_sugar_readings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HabitDao.class, HabitDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HealthDao.class, HealthDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MedicationDao.class, MedicationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CalendarDao.class, CalendarDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MoodDao.class, MoodDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BloodPressureDao.class, BloodPressureDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BloodSugarDao.class, BloodSugarDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public HabitDao habitDao() {
    if (_habitDao != null) {
      return _habitDao;
    } else {
      synchronized(this) {
        if(_habitDao == null) {
          _habitDao = new HabitDao_Impl(this);
        }
        return _habitDao;
      }
    }
  }

  @Override
  public HealthDao healthDao() {
    if (_healthDao != null) {
      return _healthDao;
    } else {
      synchronized(this) {
        if(_healthDao == null) {
          _healthDao = new HealthDao_Impl(this);
        }
        return _healthDao;
      }
    }
  }

  @Override
  public MedicationDao medicationDao() {
    if (_medicationDao != null) {
      return _medicationDao;
    } else {
      synchronized(this) {
        if(_medicationDao == null) {
          _medicationDao = new MedicationDao_Impl(this);
        }
        return _medicationDao;
      }
    }
  }

  @Override
  public CalendarDao calendarDao() {
    if (_calendarDao != null) {
      return _calendarDao;
    } else {
      synchronized(this) {
        if(_calendarDao == null) {
          _calendarDao = new CalendarDao_Impl(this);
        }
        return _calendarDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public MoodDao moodDao() {
    if (_moodDao != null) {
      return _moodDao;
    } else {
      synchronized(this) {
        if(_moodDao == null) {
          _moodDao = new MoodDao_Impl(this);
        }
        return _moodDao;
      }
    }
  }

  @Override
  public BloodPressureDao bloodPressureDao() {
    if (_bloodPressureDao != null) {
      return _bloodPressureDao;
    } else {
      synchronized(this) {
        if(_bloodPressureDao == null) {
          _bloodPressureDao = new BloodPressureDao_Impl(this);
        }
        return _bloodPressureDao;
      }
    }
  }

  @Override
  public BloodSugarDao bloodSugarDao() {
    if (_bloodSugarDao != null) {
      return _bloodSugarDao;
    } else {
      synchronized(this) {
        if(_bloodSugarDao == null) {
          _bloodSugarDao = new BloodSugarDao_Impl(this);
        }
        return _bloodSugarDao;
      }
    }
  }
}
