package com.caretracker.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.caretracker.data.entities.HealthEntryEntity;
import com.caretracker.data.entities.VitalLogEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HealthDao_Impl implements HealthDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HealthEntryEntity> __insertionAdapterOfHealthEntryEntity;

  private final EntityInsertionAdapter<VitalLogEntity> __insertionAdapterOfVitalLogEntity;

  private final EntityDeletionOrUpdateAdapter<HealthEntryEntity> __deletionAdapterOfHealthEntryEntity;

  private final EntityDeletionOrUpdateAdapter<VitalLogEntity> __deletionAdapterOfVitalLogEntity;

  private final EntityDeletionOrUpdateAdapter<HealthEntryEntity> __updateAdapterOfHealthEntryEntity;

  public HealthDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHealthEntryEntity = new EntityInsertionAdapter<HealthEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `health_entries` (`id`,`userId`,`entryDate`,`weight`,`weightUnit`,`heartRate`,`bloodPressureSystolic`,`bloodPressureDiastolic`,`bloodSugar`,`sleepHours`,`sleepQuality`,`mood`,`moodScore`,`energy`,`steps`,`exerciseMinutes`,`waterOz`,`calories`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getEntryDate());
        if (entity.getWeight() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getWeight());
        }
        if (entity.getWeightUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getWeightUnit());
        }
        if (entity.getHeartRate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getHeartRate());
        }
        if (entity.getBloodPressureSystolic() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getBloodPressureSystolic());
        }
        if (entity.getBloodPressureDiastolic() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getBloodPressureDiastolic());
        }
        if (entity.getBloodSugar() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getBloodSugar());
        }
        if (entity.getSleepHours() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getSleepHours());
        }
        if (entity.getSleepQuality() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getSleepQuality());
        }
        if (entity.getMood() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getMood());
        }
        if (entity.getMoodScore() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getMoodScore());
        }
        if (entity.getEnergy() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getEnergy());
        }
        if (entity.getSteps() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getSteps());
        }
        if (entity.getExerciseMinutes() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getExerciseMinutes());
        }
        if (entity.getWaterOz() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getWaterOz());
        }
        if (entity.getCalories() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getCalories());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getNotes());
        }
      }
    };
    this.__insertionAdapterOfVitalLogEntity = new EntityInsertionAdapter<VitalLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vital_logs` (`id`,`userId`,`entryDate`,`recordedAt`,`type`,`value`,`value2`,`unit`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitalLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getEntryDate());
        statement.bindLong(4, entity.getRecordedAt());
        statement.bindString(5, entity.getType());
        if (entity.getValue() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getValue());
        }
        if (entity.getValue2() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getValue2());
        }
        statement.bindString(8, entity.getUnit());
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
      }
    };
    this.__deletionAdapterOfHealthEntryEntity = new EntityDeletionOrUpdateAdapter<HealthEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `health_entries` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthEntryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfVitalLogEntity = new EntityDeletionOrUpdateAdapter<VitalLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `vital_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VitalLogEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHealthEntryEntity = new EntityDeletionOrUpdateAdapter<HealthEntryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `health_entries` SET `id` = ?,`userId` = ?,`entryDate` = ?,`weight` = ?,`weightUnit` = ?,`heartRate` = ?,`bloodPressureSystolic` = ?,`bloodPressureDiastolic` = ?,`bloodSugar` = ?,`sleepHours` = ?,`sleepQuality` = ?,`mood` = ?,`moodScore` = ?,`energy` = ?,`steps` = ?,`exerciseMinutes` = ?,`waterOz` = ?,`calories` = ?,`notes` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthEntryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getEntryDate());
        if (entity.getWeight() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getWeight());
        }
        if (entity.getWeightUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getWeightUnit());
        }
        if (entity.getHeartRate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getHeartRate());
        }
        if (entity.getBloodPressureSystolic() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getBloodPressureSystolic());
        }
        if (entity.getBloodPressureDiastolic() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getBloodPressureDiastolic());
        }
        if (entity.getBloodSugar() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getBloodSugar());
        }
        if (entity.getSleepHours() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getSleepHours());
        }
        if (entity.getSleepQuality() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getSleepQuality());
        }
        if (entity.getMood() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getMood());
        }
        if (entity.getMoodScore() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getMoodScore());
        }
        if (entity.getEnergy() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getEnergy());
        }
        if (entity.getSteps() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getSteps());
        }
        if (entity.getExerciseMinutes() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getExerciseMinutes());
        }
        if (entity.getWaterOz() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getWaterOz());
        }
        if (entity.getCalories() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getCalories());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getNotes());
        }
        statement.bindLong(20, entity.getId());
      }
    };
  }

  @Override
  public Object insertEntry(final HealthEntryEntity entry,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHealthEntryEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertVitalLog(final VitalLogEntity log,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVitalLogEntity.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEntry(final HealthEntryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHealthEntryEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteVitalLog(final VitalLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfVitalLogEntity.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEntry(final HealthEntryEntity entry,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHealthEntryEntity.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HealthEntryEntity>> getEntriesForUser(final long userId) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? ORDER BY entryDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_entries"}, new Callable<List<HealthEntryEntity>>() {
      @Override
      @NonNull
      public List<HealthEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthEntryEntity> _result = new ArrayList<HealthEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEntryForDate(final long userId, final String date,
      final Continuation<? super HealthEntryEntity> $completion) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? AND entryDate = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HealthEntryEntity>() {
      @Override
      @Nullable
      public HealthEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final HealthEntryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HealthEntryEntity>> getRecentEntries(final long userId) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? ORDER BY entryDate DESC LIMIT 30";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_entries"}, new Callable<List<HealthEntryEntity>>() {
      @Override
      @NonNull
      public List<HealthEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthEntryEntity> _result = new ArrayList<HealthEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<VitalLogEntity>> getVitalLogs(final long userId) {
    final String _sql = "SELECT * FROM vital_logs WHERE userId = ? ORDER BY recordedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vital_logs"}, new Callable<List<VitalLogEntity>>() {
      @Override
      @NonNull
      public List<VitalLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfRecordedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAt");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfValue2 = CursorUtil.getColumnIndexOrThrow(_cursor, "value2");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<VitalLogEntity> _result = new ArrayList<VitalLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VitalLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final long _tmpRecordedAt;
            _tmpRecordedAt = _cursor.getLong(_cursorIndexOfRecordedAt);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final Float _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getFloat(_cursorIndexOfValue);
            }
            final Float _tmpValue2;
            if (_cursor.isNull(_cursorIndexOfValue2)) {
              _tmpValue2 = null;
            } else {
              _tmpValue2 = _cursor.getFloat(_cursorIndexOfValue2);
            }
            final String _tmpUnit;
            _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new VitalLogEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpRecordedAt,_tmpType,_tmpValue,_tmpValue2,_tmpUnit,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HealthEntryEntity>> getRecentBPEntries(final long userId) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? AND bloodPressureSystolic IS NOT NULL ORDER BY entryDate DESC LIMIT 14";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_entries"}, new Callable<List<HealthEntryEntity>>() {
      @Override
      @NonNull
      public List<HealthEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthEntryEntity> _result = new ArrayList<HealthEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HealthEntryEntity>> getRecentSugarEntries(final long userId) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? AND bloodSugar IS NOT NULL ORDER BY entryDate DESC LIMIT 14";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_entries"}, new Callable<List<HealthEntryEntity>>() {
      @Override
      @NonNull
      public List<HealthEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthEntryEntity> _result = new ArrayList<HealthEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HealthEntryEntity>> getRecentHeartRateEntries(final long userId) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? AND heartRate IS NOT NULL ORDER BY entryDate DESC LIMIT 14";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_entries"}, new Callable<List<HealthEntryEntity>>() {
      @Override
      @NonNull
      public List<HealthEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthEntryEntity> _result = new ArrayList<HealthEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HealthEntryEntity>> getRecentSleepEntries(final long userId) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? AND sleepHours IS NOT NULL ORDER BY entryDate DESC LIMIT 14";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_entries"}, new Callable<List<HealthEntryEntity>>() {
      @Override
      @NonNull
      public List<HealthEntryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<HealthEntryEntity> _result = new ArrayList<HealthEntryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthEntryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLatestVitalEntry(final long userId,
      final Continuation<? super HealthEntryEntity> $completion) {
    final String _sql = "SELECT * FROM health_entries WHERE userId = ? AND (bloodPressureSystolic IS NOT NULL OR bloodSugar IS NOT NULL) ORDER BY entryDate DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HealthEntryEntity>() {
      @Override
      @Nullable
      public HealthEntryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "weightUnit");
          final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
          final int _cursorIndexOfBloodPressureSystolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureSystolic");
          final int _cursorIndexOfBloodPressureDiastolic = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodPressureDiastolic");
          final int _cursorIndexOfBloodSugar = CursorUtil.getColumnIndexOrThrow(_cursor, "bloodSugar");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfSleepQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepQuality");
          final int _cursorIndexOfMood = CursorUtil.getColumnIndexOrThrow(_cursor, "mood");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfEnergy = CursorUtil.getColumnIndexOrThrow(_cursor, "energy");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final int _cursorIndexOfExerciseMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseMinutes");
          final int _cursorIndexOfWaterOz = CursorUtil.getColumnIndexOrThrow(_cursor, "waterOz");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final HealthEntryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpEntryDate;
            _tmpEntryDate = _cursor.getString(_cursorIndexOfEntryDate);
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final String _tmpWeightUnit;
            if (_cursor.isNull(_cursorIndexOfWeightUnit)) {
              _tmpWeightUnit = null;
            } else {
              _tmpWeightUnit = _cursor.getString(_cursorIndexOfWeightUnit);
            }
            final Integer _tmpHeartRate;
            if (_cursor.isNull(_cursorIndexOfHeartRate)) {
              _tmpHeartRate = null;
            } else {
              _tmpHeartRate = _cursor.getInt(_cursorIndexOfHeartRate);
            }
            final Integer _tmpBloodPressureSystolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureSystolic)) {
              _tmpBloodPressureSystolic = null;
            } else {
              _tmpBloodPressureSystolic = _cursor.getInt(_cursorIndexOfBloodPressureSystolic);
            }
            final Integer _tmpBloodPressureDiastolic;
            if (_cursor.isNull(_cursorIndexOfBloodPressureDiastolic)) {
              _tmpBloodPressureDiastolic = null;
            } else {
              _tmpBloodPressureDiastolic = _cursor.getInt(_cursorIndexOfBloodPressureDiastolic);
            }
            final Float _tmpBloodSugar;
            if (_cursor.isNull(_cursorIndexOfBloodSugar)) {
              _tmpBloodSugar = null;
            } else {
              _tmpBloodSugar = _cursor.getFloat(_cursorIndexOfBloodSugar);
            }
            final Float _tmpSleepHours;
            if (_cursor.isNull(_cursorIndexOfSleepHours)) {
              _tmpSleepHours = null;
            } else {
              _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            }
            final Integer _tmpSleepQuality;
            if (_cursor.isNull(_cursorIndexOfSleepQuality)) {
              _tmpSleepQuality = null;
            } else {
              _tmpSleepQuality = _cursor.getInt(_cursorIndexOfSleepQuality);
            }
            final Integer _tmpMood;
            if (_cursor.isNull(_cursorIndexOfMood)) {
              _tmpMood = null;
            } else {
              _tmpMood = _cursor.getInt(_cursorIndexOfMood);
            }
            final Integer _tmpMoodScore;
            if (_cursor.isNull(_cursorIndexOfMoodScore)) {
              _tmpMoodScore = null;
            } else {
              _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            }
            final Integer _tmpEnergy;
            if (_cursor.isNull(_cursorIndexOfEnergy)) {
              _tmpEnergy = null;
            } else {
              _tmpEnergy = _cursor.getInt(_cursorIndexOfEnergy);
            }
            final Integer _tmpSteps;
            if (_cursor.isNull(_cursorIndexOfSteps)) {
              _tmpSteps = null;
            } else {
              _tmpSteps = _cursor.getInt(_cursorIndexOfSteps);
            }
            final Integer _tmpExerciseMinutes;
            if (_cursor.isNull(_cursorIndexOfExerciseMinutes)) {
              _tmpExerciseMinutes = null;
            } else {
              _tmpExerciseMinutes = _cursor.getInt(_cursorIndexOfExerciseMinutes);
            }
            final Float _tmpWaterOz;
            if (_cursor.isNull(_cursorIndexOfWaterOz)) {
              _tmpWaterOz = null;
            } else {
              _tmpWaterOz = _cursor.getFloat(_cursorIndexOfWaterOz);
            }
            final Integer _tmpCalories;
            if (_cursor.isNull(_cursorIndexOfCalories)) {
              _tmpCalories = null;
            } else {
              _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result = new HealthEntryEntity(_tmpId,_tmpUserId,_tmpEntryDate,_tmpWeight,_tmpWeightUnit,_tmpHeartRate,_tmpBloodPressureSystolic,_tmpBloodPressureDiastolic,_tmpBloodSugar,_tmpSleepHours,_tmpSleepQuality,_tmpMood,_tmpMoodScore,_tmpEnergy,_tmpSteps,_tmpExerciseMinutes,_tmpWaterOz,_tmpCalories,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
