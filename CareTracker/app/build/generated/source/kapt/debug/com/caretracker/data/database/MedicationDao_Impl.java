package com.caretracker.data.database;

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
import com.caretracker.data.models.Converters;
import com.caretracker.data.models.Medication;
import com.caretracker.data.models.MedicationLog;
import java.lang.Class;
import java.lang.Exception;
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
public final class MedicationDao_Impl implements MedicationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Medication> __insertionAdapterOfMedication;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<MedicationLog> __insertionAdapterOfMedicationLog;

  private final EntityDeletionOrUpdateAdapter<Medication> __deletionAdapterOfMedication;

  private final EntityDeletionOrUpdateAdapter<Medication> __updateAdapterOfMedication;

  public MedicationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedication = new EntityInsertionAdapter<Medication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medications` (`id`,`personId`,`name`,`dosage`,`unit`,`frequency`,`timesOfDay`,`instructions`,`prescribingDoctorId`,`pillsRemaining`,`pillsPerDose`,`refillReminderAt`,`startDate`,`endDate`,`isActive`,`color`,`notes`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Medication entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDosage());
        }
        if (entity.getUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUnit());
        }
        if (entity.getFrequency() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFrequency());
        }
        final String _tmp = __converters.toStringList(entity.getTimesOfDay());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp);
        }
        if (entity.getInstructions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInstructions());
        }
        statement.bindLong(9, entity.getPrescribingDoctorId());
        statement.bindLong(10, entity.getPillsRemaining());
        statement.bindLong(11, entity.getPillsPerDose());
        statement.bindLong(12, entity.getRefillReminderAt());
        if (entity.getStartDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getEndDate());
        }
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(15, _tmp_1);
        if (entity.getColor() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getColor());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getNotes());
        }
        statement.bindLong(18, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfMedicationLog = new EntityInsertionAdapter<MedicationLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medication_logs` (`id`,`medicationId`,`personId`,`takenAt`,`scheduledTime`,`wasTaken`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MedicationLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMedicationId());
        statement.bindLong(3, entity.getPersonId());
        statement.bindLong(4, entity.getTakenAt());
        if (entity.getScheduledTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getScheduledTime());
        }
        final int _tmp = entity.getWasTaken() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
      }
    };
    this.__deletionAdapterOfMedication = new EntityDeletionOrUpdateAdapter<Medication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `medications` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Medication entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMedication = new EntityDeletionOrUpdateAdapter<Medication>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `medications` SET `id` = ?,`personId` = ?,`name` = ?,`dosage` = ?,`unit` = ?,`frequency` = ?,`timesOfDay` = ?,`instructions` = ?,`prescribingDoctorId` = ?,`pillsRemaining` = ?,`pillsPerDose` = ?,`refillReminderAt` = ?,`startDate` = ?,`endDate` = ?,`isActive` = ?,`color` = ?,`notes` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Medication entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDosage());
        }
        if (entity.getUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUnit());
        }
        if (entity.getFrequency() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFrequency());
        }
        final String _tmp = __converters.toStringList(entity.getTimesOfDay());
        if (_tmp == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp);
        }
        if (entity.getInstructions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInstructions());
        }
        statement.bindLong(9, entity.getPrescribingDoctorId());
        statement.bindLong(10, entity.getPillsRemaining());
        statement.bindLong(11, entity.getPillsPerDose());
        statement.bindLong(12, entity.getRefillReminderAt());
        if (entity.getStartDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getEndDate());
        }
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(15, _tmp_1);
        if (entity.getColor() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getColor());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getNotes());
        }
        statement.bindLong(18, entity.getCreatedAt());
        statement.bindLong(19, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Medication medication, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMedication.insertAndReturnId(medication);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLog(final MedicationLog log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMedicationLog.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Medication medication, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMedication.handle(medication);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Medication medication, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMedication.handle(medication);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Medication>> getMedicationsForPerson(final long personId) {
    final String _sql = "SELECT * FROM medications WHERE personId = ? AND isActive = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medications"}, new Callable<List<Medication>>() {
      @Override
      @NonNull
      public List<Medication> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesOfDay");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescribingDoctorId = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribingDoctorId");
          final int _cursorIndexOfPillsRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsRemaining");
          final int _cursorIndexOfPillsPerDose = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerDose");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Medication> _result = new ArrayList<Medication>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Medication _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final List<String> _tmpTimesOfDay;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTimesOfDay)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTimesOfDay);
            }
            _tmpTimesOfDay = __converters.fromStringList(_tmp);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final long _tmpPrescribingDoctorId;
            _tmpPrescribingDoctorId = _cursor.getLong(_cursorIndexOfPrescribingDoctorId);
            final int _tmpPillsRemaining;
            _tmpPillsRemaining = _cursor.getInt(_cursorIndexOfPillsRemaining);
            final int _tmpPillsPerDose;
            _tmpPillsPerDose = _cursor.getInt(_cursorIndexOfPillsPerDose);
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Medication(_tmpId,_tmpPersonId,_tmpName,_tmpDosage,_tmpUnit,_tmpFrequency,_tmpTimesOfDay,_tmpInstructions,_tmpPrescribingDoctorId,_tmpPillsRemaining,_tmpPillsPerDose,_tmpRefillReminderAt,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpColor,_tmpNotes,_tmpCreatedAt);
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
  public Flow<List<Medication>> getAllActiveMedications() {
    final String _sql = "SELECT * FROM medications WHERE isActive = 1 ORDER BY personId, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medications"}, new Callable<List<Medication>>() {
      @Override
      @NonNull
      public List<Medication> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesOfDay");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescribingDoctorId = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribingDoctorId");
          final int _cursorIndexOfPillsRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsRemaining");
          final int _cursorIndexOfPillsPerDose = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerDose");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Medication> _result = new ArrayList<Medication>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Medication _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final List<String> _tmpTimesOfDay;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTimesOfDay)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTimesOfDay);
            }
            _tmpTimesOfDay = __converters.fromStringList(_tmp);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final long _tmpPrescribingDoctorId;
            _tmpPrescribingDoctorId = _cursor.getLong(_cursorIndexOfPrescribingDoctorId);
            final int _tmpPillsRemaining;
            _tmpPillsRemaining = _cursor.getInt(_cursorIndexOfPillsRemaining);
            final int _tmpPillsPerDose;
            _tmpPillsPerDose = _cursor.getInt(_cursorIndexOfPillsPerDose);
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Medication(_tmpId,_tmpPersonId,_tmpName,_tmpDosage,_tmpUnit,_tmpFrequency,_tmpTimesOfDay,_tmpInstructions,_tmpPrescribingDoctorId,_tmpPillsRemaining,_tmpPillsPerDose,_tmpRefillReminderAt,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpColor,_tmpNotes,_tmpCreatedAt);
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
  public Object getMedicationById(final long id,
      final Continuation<? super Medication> $completion) {
    final String _sql = "SELECT * FROM medications WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Medication>() {
      @Override
      @Nullable
      public Medication call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesOfDay");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescribingDoctorId = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribingDoctorId");
          final int _cursorIndexOfPillsRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsRemaining");
          final int _cursorIndexOfPillsPerDose = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerDose");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Medication _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final List<String> _tmpTimesOfDay;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTimesOfDay)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTimesOfDay);
            }
            _tmpTimesOfDay = __converters.fromStringList(_tmp);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final long _tmpPrescribingDoctorId;
            _tmpPrescribingDoctorId = _cursor.getLong(_cursorIndexOfPrescribingDoctorId);
            final int _tmpPillsRemaining;
            _tmpPillsRemaining = _cursor.getInt(_cursorIndexOfPillsRemaining);
            final int _tmpPillsPerDose;
            _tmpPillsPerDose = _cursor.getInt(_cursorIndexOfPillsPerDose);
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Medication(_tmpId,_tmpPersonId,_tmpName,_tmpDosage,_tmpUnit,_tmpFrequency,_tmpTimesOfDay,_tmpInstructions,_tmpPrescribingDoctorId,_tmpPillsRemaining,_tmpPillsPerDose,_tmpRefillReminderAt,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpColor,_tmpNotes,_tmpCreatedAt);
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
  public Flow<List<Medication>> getMedicationsNeedingRefill() {
    final String _sql = "SELECT * FROM medications WHERE pillsRemaining <= refillReminderAt AND isActive = 1 ORDER BY pillsRemaining ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medications"}, new Callable<List<Medication>>() {
      @Override
      @NonNull
      public List<Medication> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesOfDay");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescribingDoctorId = CursorUtil.getColumnIndexOrThrow(_cursor, "prescribingDoctorId");
          final int _cursorIndexOfPillsRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsRemaining");
          final int _cursorIndexOfPillsPerDose = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerDose");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Medication> _result = new ArrayList<Medication>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Medication _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final List<String> _tmpTimesOfDay;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTimesOfDay)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTimesOfDay);
            }
            _tmpTimesOfDay = __converters.fromStringList(_tmp);
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final long _tmpPrescribingDoctorId;
            _tmpPrescribingDoctorId = _cursor.getLong(_cursorIndexOfPrescribingDoctorId);
            final int _tmpPillsRemaining;
            _tmpPillsRemaining = _cursor.getInt(_cursorIndexOfPillsRemaining);
            final int _tmpPillsPerDose;
            _tmpPillsPerDose = _cursor.getInt(_cursorIndexOfPillsPerDose);
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final String _tmpColor;
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _tmpColor = null;
            } else {
              _tmpColor = _cursor.getString(_cursorIndexOfColor);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Medication(_tmpId,_tmpPersonId,_tmpName,_tmpDosage,_tmpUnit,_tmpFrequency,_tmpTimesOfDay,_tmpInstructions,_tmpPrescribingDoctorId,_tmpPillsRemaining,_tmpPillsPerDose,_tmpRefillReminderAt,_tmpStartDate,_tmpEndDate,_tmpIsActive,_tmpColor,_tmpNotes,_tmpCreatedAt);
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
  public Flow<List<MedicationLog>> getLogsForMedication(final long medicationId) {
    final String _sql = "SELECT * FROM medication_logs WHERE medicationId = ? ORDER BY takenAt DESC LIMIT 30";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medicationId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medication_logs"}, new Callable<List<MedicationLog>>() {
      @Override
      @NonNull
      public List<MedicationLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfWasTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "wasTaken");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MedicationLog> _result = new ArrayList<MedicationLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final boolean _tmpWasTaken;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWasTaken);
            _tmpWasTaken = _tmp != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new MedicationLog(_tmpId,_tmpMedicationId,_tmpPersonId,_tmpTakenAt,_tmpScheduledTime,_tmpWasTaken,_tmpNotes);
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
  public Object getLogsForPersonInTimeRange(final long personId, final long startMs,
      final long endMs, final Continuation<? super List<MedicationLog>> $completion) {
    final String _sql = "SELECT * FROM medication_logs WHERE personId = ? AND takenAt BETWEEN ? AND ? ORDER BY takenAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startMs);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endMs);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MedicationLog>>() {
      @Override
      @NonNull
      public List<MedicationLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfWasTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "wasTaken");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MedicationLog> _result = new ArrayList<MedicationLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final boolean _tmpWasTaken;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWasTaken);
            _tmpWasTaken = _tmp != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new MedicationLog(_tmpId,_tmpMedicationId,_tmpPersonId,_tmpTakenAt,_tmpScheduledTime,_tmpWasTaken,_tmpNotes);
            _result.add(_item);
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
  public Object getLogsForMedicationInTimeRange(final long medicationId, final long startMs,
      final long endMs, final Continuation<? super List<MedicationLog>> $completion) {
    final String _sql = "SELECT * FROM medication_logs WHERE medicationId = ? AND takenAt BETWEEN ? AND ? ORDER BY takenAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medicationId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startMs);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endMs);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MedicationLog>>() {
      @Override
      @NonNull
      public List<MedicationLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfWasTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "wasTaken");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<MedicationLog> _result = new ArrayList<MedicationLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final boolean _tmpWasTaken;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWasTaken);
            _tmpWasTaken = _tmp != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new MedicationLog(_tmpId,_tmpMedicationId,_tmpPersonId,_tmpTakenAt,_tmpScheduledTime,_tmpWasTaken,_tmpNotes);
            _result.add(_item);
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
