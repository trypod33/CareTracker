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
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.caretracker.data.entities.MedLogEntity;
import com.caretracker.data.entities.MedicationEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class MedicationDao_Impl implements MedicationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MedicationEntity> __insertionAdapterOfMedicationEntity;

  private final EntityInsertionAdapter<MedLogEntity> __insertionAdapterOfMedLogEntity;

  private final EntityDeletionOrUpdateAdapter<MedicationEntity> __deletionAdapterOfMedicationEntity;

  private final EntityDeletionOrUpdateAdapter<MedicationEntity> __updateAdapterOfMedicationEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMedicationsByUserId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLogsByUserId;

  public MedicationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMedicationEntity = new EntityInsertionAdapter<MedicationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `medications` (`id`,`userId`,`name`,`genericName`,`dosage`,`dosageUnit`,`form`,`frequency`,`timesPerDay`,`scheduledTimes`,`withFood`,`instructions`,`prescriber`,`pharmacy`,`rxNumber`,`color`,`currentCount`,`pillsPerRefill`,`refillReminderAt`,`lastRefillDate`,`nextRefillDate`,`startDate`,`endDate`,`sortOrder`,`isActive`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MedicationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getName());
        if (entity.getGenericName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getGenericName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDosage());
        }
        statement.bindString(6, entity.getDosageUnit());
        statement.bindString(7, entity.getForm());
        if (entity.getFrequency() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFrequency());
        }
        statement.bindLong(9, entity.getTimesPerDay());
        if (entity.getScheduledTimes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getScheduledTimes());
        }
        final int _tmp = entity.getWithFood() ? 1 : 0;
        statement.bindLong(11, _tmp);
        if (entity.getInstructions() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getInstructions());
        }
        if (entity.getPrescriber() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPrescriber());
        }
        if (entity.getPharmacy() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPharmacy());
        }
        if (entity.getRxNumber() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getRxNumber());
        }
        statement.bindString(16, entity.getColor());
        statement.bindLong(17, entity.getCurrentCount());
        if (entity.getPillsPerRefill() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getPillsPerRefill());
        }
        statement.bindLong(19, entity.getRefillReminderAt());
        if (entity.getLastRefillDate() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getLastRefillDate());
        }
        if (entity.getNextRefillDate() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getNextRefillDate());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getEndDate());
        }
        statement.bindLong(24, entity.getSortOrder());
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(25, _tmp_1);
        statement.bindLong(26, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfMedLogEntity = new EntityInsertionAdapter<MedLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `med_logs` (`id`,`medicationId`,`takenAt`,`takenDate`,`scheduledTime`,`status`,`note`,`doseTaken`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MedLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMedicationId());
        statement.bindLong(3, entity.getTakenAt());
        statement.bindString(4, entity.getTakenDate());
        if (entity.getScheduledTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getScheduledTime());
        }
        statement.bindString(6, entity.getStatus());
        if (entity.getNote() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNote());
        }
        if (entity.getDoseTaken() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDoseTaken());
        }
      }
    };
    this.__deletionAdapterOfMedicationEntity = new EntityDeletionOrUpdateAdapter<MedicationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `medications` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MedicationEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMedicationEntity = new EntityDeletionOrUpdateAdapter<MedicationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `medications` SET `id` = ?,`userId` = ?,`name` = ?,`genericName` = ?,`dosage` = ?,`dosageUnit` = ?,`form` = ?,`frequency` = ?,`timesPerDay` = ?,`scheduledTimes` = ?,`withFood` = ?,`instructions` = ?,`prescriber` = ?,`pharmacy` = ?,`rxNumber` = ?,`color` = ?,`currentCount` = ?,`pillsPerRefill` = ?,`refillReminderAt` = ?,`lastRefillDate` = ?,`nextRefillDate` = ?,`startDate` = ?,`endDate` = ?,`sortOrder` = ?,`isActive` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MedicationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getName());
        if (entity.getGenericName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getGenericName());
        }
        if (entity.getDosage() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDosage());
        }
        statement.bindString(6, entity.getDosageUnit());
        statement.bindString(7, entity.getForm());
        if (entity.getFrequency() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFrequency());
        }
        statement.bindLong(9, entity.getTimesPerDay());
        if (entity.getScheduledTimes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getScheduledTimes());
        }
        final int _tmp = entity.getWithFood() ? 1 : 0;
        statement.bindLong(11, _tmp);
        if (entity.getInstructions() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getInstructions());
        }
        if (entity.getPrescriber() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPrescriber());
        }
        if (entity.getPharmacy() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getPharmacy());
        }
        if (entity.getRxNumber() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getRxNumber());
        }
        statement.bindString(16, entity.getColor());
        statement.bindLong(17, entity.getCurrentCount());
        if (entity.getPillsPerRefill() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getPillsPerRefill());
        }
        statement.bindLong(19, entity.getRefillReminderAt());
        if (entity.getLastRefillDate() == null) {
          statement.bindNull(20);
        } else {
          statement.bindString(20, entity.getLastRefillDate());
        }
        if (entity.getNextRefillDate() == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.getNextRefillDate());
        }
        if (entity.getStartDate() == null) {
          statement.bindNull(22);
        } else {
          statement.bindString(22, entity.getStartDate());
        }
        if (entity.getEndDate() == null) {
          statement.bindNull(23);
        } else {
          statement.bindString(23, entity.getEndDate());
        }
        statement.bindLong(24, entity.getSortOrder());
        final int _tmp_1 = entity.isActive() ? 1 : 0;
        statement.bindLong(25, _tmp_1);
        statement.bindLong(26, entity.getCreatedAt());
        statement.bindLong(27, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteMedicationsByUserId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM medications WHERE userId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteLogsByUserId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM med_logs WHERE medicationId IN (SELECT id FROM medications WHERE userId = ?)";
        return _query;
      }
    };
  }

  @Override
  public Object insertMedication(final MedicationEntity medication,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMedicationEntity.insertAndReturnId(medication);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLog(final MedLogEntity log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMedLogEntity.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMedication(final MedicationEntity medication,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMedicationEntity.handle(medication);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMedication(final MedicationEntity medication,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMedicationEntity.handle(medication);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMedicationsByUserId(final long userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMedicationsByUserId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteMedicationsByUserId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLogsByUserId(final long userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLogsByUserId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteLogsByUserId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MedicationEntity>> getAllMedications() {
    final String _sql = "SELECT * FROM medications ORDER BY sortOrder ASC, createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medications"}, new Callable<List<MedicationEntity>>() {
      @Override
      @NonNull
      public List<MedicationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGenericName = CursorUtil.getColumnIndexOrThrow(_cursor, "genericName");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfDosageUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageUnit");
          final int _cursorIndexOfForm = CursorUtil.getColumnIndexOrThrow(_cursor, "form");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesPerDay");
          final int _cursorIndexOfScheduledTimes = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTimes");
          final int _cursorIndexOfWithFood = CursorUtil.getColumnIndexOrThrow(_cursor, "withFood");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescriber = CursorUtil.getColumnIndexOrThrow(_cursor, "prescriber");
          final int _cursorIndexOfPharmacy = CursorUtil.getColumnIndexOrThrow(_cursor, "pharmacy");
          final int _cursorIndexOfRxNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "rxNumber");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCurrentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCount");
          final int _cursorIndexOfPillsPerRefill = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerRefill");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfLastRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRefillDate");
          final int _cursorIndexOfNextRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRefillDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MedicationEntity> _result = new ArrayList<MedicationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGenericName;
            if (_cursor.isNull(_cursorIndexOfGenericName)) {
              _tmpGenericName = null;
            } else {
              _tmpGenericName = _cursor.getString(_cursorIndexOfGenericName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpDosageUnit;
            _tmpDosageUnit = _cursor.getString(_cursorIndexOfDosageUnit);
            final String _tmpForm;
            _tmpForm = _cursor.getString(_cursorIndexOfForm);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final int _tmpTimesPerDay;
            _tmpTimesPerDay = _cursor.getInt(_cursorIndexOfTimesPerDay);
            final String _tmpScheduledTimes;
            if (_cursor.isNull(_cursorIndexOfScheduledTimes)) {
              _tmpScheduledTimes = null;
            } else {
              _tmpScheduledTimes = _cursor.getString(_cursorIndexOfScheduledTimes);
            }
            final boolean _tmpWithFood;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWithFood);
            _tmpWithFood = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpPrescriber;
            if (_cursor.isNull(_cursorIndexOfPrescriber)) {
              _tmpPrescriber = null;
            } else {
              _tmpPrescriber = _cursor.getString(_cursorIndexOfPrescriber);
            }
            final String _tmpPharmacy;
            if (_cursor.isNull(_cursorIndexOfPharmacy)) {
              _tmpPharmacy = null;
            } else {
              _tmpPharmacy = _cursor.getString(_cursorIndexOfPharmacy);
            }
            final String _tmpRxNumber;
            if (_cursor.isNull(_cursorIndexOfRxNumber)) {
              _tmpRxNumber = null;
            } else {
              _tmpRxNumber = _cursor.getString(_cursorIndexOfRxNumber);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final int _tmpCurrentCount;
            _tmpCurrentCount = _cursor.getInt(_cursorIndexOfCurrentCount);
            final Integer _tmpPillsPerRefill;
            if (_cursor.isNull(_cursorIndexOfPillsPerRefill)) {
              _tmpPillsPerRefill = null;
            } else {
              _tmpPillsPerRefill = _cursor.getInt(_cursorIndexOfPillsPerRefill);
            }
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpLastRefillDate;
            if (_cursor.isNull(_cursorIndexOfLastRefillDate)) {
              _tmpLastRefillDate = null;
            } else {
              _tmpLastRefillDate = _cursor.getString(_cursorIndexOfLastRefillDate);
            }
            final String _tmpNextRefillDate;
            if (_cursor.isNull(_cursorIndexOfNextRefillDate)) {
              _tmpNextRefillDate = null;
            } else {
              _tmpNextRefillDate = _cursor.getString(_cursorIndexOfNextRefillDate);
            }
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
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MedicationEntity(_tmpId,_tmpUserId,_tmpName,_tmpGenericName,_tmpDosage,_tmpDosageUnit,_tmpForm,_tmpFrequency,_tmpTimesPerDay,_tmpScheduledTimes,_tmpWithFood,_tmpInstructions,_tmpPrescriber,_tmpPharmacy,_tmpRxNumber,_tmpColor,_tmpCurrentCount,_tmpPillsPerRefill,_tmpRefillReminderAt,_tmpLastRefillDate,_tmpNextRefillDate,_tmpStartDate,_tmpEndDate,_tmpSortOrder,_tmpIsActive,_tmpCreatedAt);
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
  public Flow<List<MedicationEntity>> getMedicationsForUser(final long userId) {
    final String _sql = "SELECT * FROM medications WHERE userId = ? ORDER BY sortOrder ASC, createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"medications"}, new Callable<List<MedicationEntity>>() {
      @Override
      @NonNull
      public List<MedicationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGenericName = CursorUtil.getColumnIndexOrThrow(_cursor, "genericName");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfDosageUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageUnit");
          final int _cursorIndexOfForm = CursorUtil.getColumnIndexOrThrow(_cursor, "form");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesPerDay");
          final int _cursorIndexOfScheduledTimes = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTimes");
          final int _cursorIndexOfWithFood = CursorUtil.getColumnIndexOrThrow(_cursor, "withFood");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescriber = CursorUtil.getColumnIndexOrThrow(_cursor, "prescriber");
          final int _cursorIndexOfPharmacy = CursorUtil.getColumnIndexOrThrow(_cursor, "pharmacy");
          final int _cursorIndexOfRxNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "rxNumber");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCurrentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCount");
          final int _cursorIndexOfPillsPerRefill = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerRefill");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfLastRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRefillDate");
          final int _cursorIndexOfNextRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRefillDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MedicationEntity> _result = new ArrayList<MedicationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGenericName;
            if (_cursor.isNull(_cursorIndexOfGenericName)) {
              _tmpGenericName = null;
            } else {
              _tmpGenericName = _cursor.getString(_cursorIndexOfGenericName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpDosageUnit;
            _tmpDosageUnit = _cursor.getString(_cursorIndexOfDosageUnit);
            final String _tmpForm;
            _tmpForm = _cursor.getString(_cursorIndexOfForm);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final int _tmpTimesPerDay;
            _tmpTimesPerDay = _cursor.getInt(_cursorIndexOfTimesPerDay);
            final String _tmpScheduledTimes;
            if (_cursor.isNull(_cursorIndexOfScheduledTimes)) {
              _tmpScheduledTimes = null;
            } else {
              _tmpScheduledTimes = _cursor.getString(_cursorIndexOfScheduledTimes);
            }
            final boolean _tmpWithFood;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWithFood);
            _tmpWithFood = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpPrescriber;
            if (_cursor.isNull(_cursorIndexOfPrescriber)) {
              _tmpPrescriber = null;
            } else {
              _tmpPrescriber = _cursor.getString(_cursorIndexOfPrescriber);
            }
            final String _tmpPharmacy;
            if (_cursor.isNull(_cursorIndexOfPharmacy)) {
              _tmpPharmacy = null;
            } else {
              _tmpPharmacy = _cursor.getString(_cursorIndexOfPharmacy);
            }
            final String _tmpRxNumber;
            if (_cursor.isNull(_cursorIndexOfRxNumber)) {
              _tmpRxNumber = null;
            } else {
              _tmpRxNumber = _cursor.getString(_cursorIndexOfRxNumber);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final int _tmpCurrentCount;
            _tmpCurrentCount = _cursor.getInt(_cursorIndexOfCurrentCount);
            final Integer _tmpPillsPerRefill;
            if (_cursor.isNull(_cursorIndexOfPillsPerRefill)) {
              _tmpPillsPerRefill = null;
            } else {
              _tmpPillsPerRefill = _cursor.getInt(_cursorIndexOfPillsPerRefill);
            }
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpLastRefillDate;
            if (_cursor.isNull(_cursorIndexOfLastRefillDate)) {
              _tmpLastRefillDate = null;
            } else {
              _tmpLastRefillDate = _cursor.getString(_cursorIndexOfLastRefillDate);
            }
            final String _tmpNextRefillDate;
            if (_cursor.isNull(_cursorIndexOfNextRefillDate)) {
              _tmpNextRefillDate = null;
            } else {
              _tmpNextRefillDate = _cursor.getString(_cursorIndexOfNextRefillDate);
            }
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
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MedicationEntity(_tmpId,_tmpUserId,_tmpName,_tmpGenericName,_tmpDosage,_tmpDosageUnit,_tmpForm,_tmpFrequency,_tmpTimesPerDay,_tmpScheduledTimes,_tmpWithFood,_tmpInstructions,_tmpPrescriber,_tmpPharmacy,_tmpRxNumber,_tmpColor,_tmpCurrentCount,_tmpPillsPerRefill,_tmpRefillReminderAt,_tmpLastRefillDate,_tmpNextRefillDate,_tmpStartDate,_tmpEndDate,_tmpSortOrder,_tmpIsActive,_tmpCreatedAt);
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
  public Object getMedicationsForUserOnce(final long userId,
      final Continuation<? super List<MedicationEntity>> $completion) {
    final String _sql = "SELECT * FROM medications WHERE userId = ? ORDER BY sortOrder ASC, createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MedicationEntity>>() {
      @Override
      @NonNull
      public List<MedicationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGenericName = CursorUtil.getColumnIndexOrThrow(_cursor, "genericName");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfDosageUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageUnit");
          final int _cursorIndexOfForm = CursorUtil.getColumnIndexOrThrow(_cursor, "form");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesPerDay");
          final int _cursorIndexOfScheduledTimes = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTimes");
          final int _cursorIndexOfWithFood = CursorUtil.getColumnIndexOrThrow(_cursor, "withFood");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescriber = CursorUtil.getColumnIndexOrThrow(_cursor, "prescriber");
          final int _cursorIndexOfPharmacy = CursorUtil.getColumnIndexOrThrow(_cursor, "pharmacy");
          final int _cursorIndexOfRxNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "rxNumber");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCurrentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCount");
          final int _cursorIndexOfPillsPerRefill = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerRefill");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfLastRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRefillDate");
          final int _cursorIndexOfNextRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRefillDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MedicationEntity> _result = new ArrayList<MedicationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedicationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGenericName;
            if (_cursor.isNull(_cursorIndexOfGenericName)) {
              _tmpGenericName = null;
            } else {
              _tmpGenericName = _cursor.getString(_cursorIndexOfGenericName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpDosageUnit;
            _tmpDosageUnit = _cursor.getString(_cursorIndexOfDosageUnit);
            final String _tmpForm;
            _tmpForm = _cursor.getString(_cursorIndexOfForm);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final int _tmpTimesPerDay;
            _tmpTimesPerDay = _cursor.getInt(_cursorIndexOfTimesPerDay);
            final String _tmpScheduledTimes;
            if (_cursor.isNull(_cursorIndexOfScheduledTimes)) {
              _tmpScheduledTimes = null;
            } else {
              _tmpScheduledTimes = _cursor.getString(_cursorIndexOfScheduledTimes);
            }
            final boolean _tmpWithFood;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWithFood);
            _tmpWithFood = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpPrescriber;
            if (_cursor.isNull(_cursorIndexOfPrescriber)) {
              _tmpPrescriber = null;
            } else {
              _tmpPrescriber = _cursor.getString(_cursorIndexOfPrescriber);
            }
            final String _tmpPharmacy;
            if (_cursor.isNull(_cursorIndexOfPharmacy)) {
              _tmpPharmacy = null;
            } else {
              _tmpPharmacy = _cursor.getString(_cursorIndexOfPharmacy);
            }
            final String _tmpRxNumber;
            if (_cursor.isNull(_cursorIndexOfRxNumber)) {
              _tmpRxNumber = null;
            } else {
              _tmpRxNumber = _cursor.getString(_cursorIndexOfRxNumber);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final int _tmpCurrentCount;
            _tmpCurrentCount = _cursor.getInt(_cursorIndexOfCurrentCount);
            final Integer _tmpPillsPerRefill;
            if (_cursor.isNull(_cursorIndexOfPillsPerRefill)) {
              _tmpPillsPerRefill = null;
            } else {
              _tmpPillsPerRefill = _cursor.getInt(_cursorIndexOfPillsPerRefill);
            }
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpLastRefillDate;
            if (_cursor.isNull(_cursorIndexOfLastRefillDate)) {
              _tmpLastRefillDate = null;
            } else {
              _tmpLastRefillDate = _cursor.getString(_cursorIndexOfLastRefillDate);
            }
            final String _tmpNextRefillDate;
            if (_cursor.isNull(_cursorIndexOfNextRefillDate)) {
              _tmpNextRefillDate = null;
            } else {
              _tmpNextRefillDate = _cursor.getString(_cursorIndexOfNextRefillDate);
            }
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
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MedicationEntity(_tmpId,_tmpUserId,_tmpName,_tmpGenericName,_tmpDosage,_tmpDosageUnit,_tmpForm,_tmpFrequency,_tmpTimesPerDay,_tmpScheduledTimes,_tmpWithFood,_tmpInstructions,_tmpPrescriber,_tmpPharmacy,_tmpRxNumber,_tmpColor,_tmpCurrentCount,_tmpPillsPerRefill,_tmpRefillReminderAt,_tmpLastRefillDate,_tmpNextRefillDate,_tmpStartDate,_tmpEndDate,_tmpSortOrder,_tmpIsActive,_tmpCreatedAt);
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
  public Object getMedicationById(final long id,
      final Continuation<? super MedicationEntity> $completion) {
    final String _sql = "SELECT * FROM medications WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MedicationEntity>() {
      @Override
      @Nullable
      public MedicationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfGenericName = CursorUtil.getColumnIndexOrThrow(_cursor, "genericName");
          final int _cursorIndexOfDosage = CursorUtil.getColumnIndexOrThrow(_cursor, "dosage");
          final int _cursorIndexOfDosageUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "dosageUnit");
          final int _cursorIndexOfForm = CursorUtil.getColumnIndexOrThrow(_cursor, "form");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfTimesPerDay = CursorUtil.getColumnIndexOrThrow(_cursor, "timesPerDay");
          final int _cursorIndexOfScheduledTimes = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTimes");
          final int _cursorIndexOfWithFood = CursorUtil.getColumnIndexOrThrow(_cursor, "withFood");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrescriber = CursorUtil.getColumnIndexOrThrow(_cursor, "prescriber");
          final int _cursorIndexOfPharmacy = CursorUtil.getColumnIndexOrThrow(_cursor, "pharmacy");
          final int _cursorIndexOfRxNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "rxNumber");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfCurrentCount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCount");
          final int _cursorIndexOfPillsPerRefill = CursorUtil.getColumnIndexOrThrow(_cursor, "pillsPerRefill");
          final int _cursorIndexOfRefillReminderAt = CursorUtil.getColumnIndexOrThrow(_cursor, "refillReminderAt");
          final int _cursorIndexOfLastRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRefillDate");
          final int _cursorIndexOfNextRefillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRefillDate");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MedicationEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpGenericName;
            if (_cursor.isNull(_cursorIndexOfGenericName)) {
              _tmpGenericName = null;
            } else {
              _tmpGenericName = _cursor.getString(_cursorIndexOfGenericName);
            }
            final String _tmpDosage;
            if (_cursor.isNull(_cursorIndexOfDosage)) {
              _tmpDosage = null;
            } else {
              _tmpDosage = _cursor.getString(_cursorIndexOfDosage);
            }
            final String _tmpDosageUnit;
            _tmpDosageUnit = _cursor.getString(_cursorIndexOfDosageUnit);
            final String _tmpForm;
            _tmpForm = _cursor.getString(_cursorIndexOfForm);
            final String _tmpFrequency;
            if (_cursor.isNull(_cursorIndexOfFrequency)) {
              _tmpFrequency = null;
            } else {
              _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            }
            final int _tmpTimesPerDay;
            _tmpTimesPerDay = _cursor.getInt(_cursorIndexOfTimesPerDay);
            final String _tmpScheduledTimes;
            if (_cursor.isNull(_cursorIndexOfScheduledTimes)) {
              _tmpScheduledTimes = null;
            } else {
              _tmpScheduledTimes = _cursor.getString(_cursorIndexOfScheduledTimes);
            }
            final boolean _tmpWithFood;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWithFood);
            _tmpWithFood = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpPrescriber;
            if (_cursor.isNull(_cursorIndexOfPrescriber)) {
              _tmpPrescriber = null;
            } else {
              _tmpPrescriber = _cursor.getString(_cursorIndexOfPrescriber);
            }
            final String _tmpPharmacy;
            if (_cursor.isNull(_cursorIndexOfPharmacy)) {
              _tmpPharmacy = null;
            } else {
              _tmpPharmacy = _cursor.getString(_cursorIndexOfPharmacy);
            }
            final String _tmpRxNumber;
            if (_cursor.isNull(_cursorIndexOfRxNumber)) {
              _tmpRxNumber = null;
            } else {
              _tmpRxNumber = _cursor.getString(_cursorIndexOfRxNumber);
            }
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final int _tmpCurrentCount;
            _tmpCurrentCount = _cursor.getInt(_cursorIndexOfCurrentCount);
            final Integer _tmpPillsPerRefill;
            if (_cursor.isNull(_cursorIndexOfPillsPerRefill)) {
              _tmpPillsPerRefill = null;
            } else {
              _tmpPillsPerRefill = _cursor.getInt(_cursorIndexOfPillsPerRefill);
            }
            final int _tmpRefillReminderAt;
            _tmpRefillReminderAt = _cursor.getInt(_cursorIndexOfRefillReminderAt);
            final String _tmpLastRefillDate;
            if (_cursor.isNull(_cursorIndexOfLastRefillDate)) {
              _tmpLastRefillDate = null;
            } else {
              _tmpLastRefillDate = _cursor.getString(_cursorIndexOfLastRefillDate);
            }
            final String _tmpNextRefillDate;
            if (_cursor.isNull(_cursorIndexOfNextRefillDate)) {
              _tmpNextRefillDate = null;
            } else {
              _tmpNextRefillDate = _cursor.getString(_cursorIndexOfNextRefillDate);
            }
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
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            final boolean _tmpIsActive;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new MedicationEntity(_tmpId,_tmpUserId,_tmpName,_tmpGenericName,_tmpDosage,_tmpDosageUnit,_tmpForm,_tmpFrequency,_tmpTimesPerDay,_tmpScheduledTimes,_tmpWithFood,_tmpInstructions,_tmpPrescriber,_tmpPharmacy,_tmpRxNumber,_tmpColor,_tmpCurrentCount,_tmpPillsPerRefill,_tmpRefillReminderAt,_tmpLastRefillDate,_tmpNextRefillDate,_tmpStartDate,_tmpEndDate,_tmpSortOrder,_tmpIsActive,_tmpCreatedAt);
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
  public Flow<List<MedLogEntity>> getLogsForDate(final long medId, final String date) {
    final String _sql = "SELECT * FROM med_logs WHERE medicationId = ? AND takenDate = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"med_logs"}, new Callable<List<MedLogEntity>>() {
      @Override
      @NonNull
      public List<MedLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfTakenDate = CursorUtil.getColumnIndexOrThrow(_cursor, "takenDate");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDoseTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "doseTaken");
          final List<MedLogEntity> _result = new ArrayList<MedLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpTakenDate;
            _tmpTakenDate = _cursor.getString(_cursorIndexOfTakenDate);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpDoseTaken;
            if (_cursor.isNull(_cursorIndexOfDoseTaken)) {
              _tmpDoseTaken = null;
            } else {
              _tmpDoseTaken = _cursor.getString(_cursorIndexOfDoseTaken);
            }
            _item = new MedLogEntity(_tmpId,_tmpMedicationId,_tmpTakenAt,_tmpTakenDate,_tmpScheduledTime,_tmpStatus,_tmpNote,_tmpDoseTaken);
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
  public Object getMedLogsForDateOnce(final long medId, final String date,
      final Continuation<? super List<MedLogEntity>> $completion) {
    final String _sql = "SELECT * FROM med_logs WHERE medicationId = ? AND takenDate = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MedLogEntity>>() {
      @Override
      @NonNull
      public List<MedLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfTakenDate = CursorUtil.getColumnIndexOrThrow(_cursor, "takenDate");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDoseTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "doseTaken");
          final List<MedLogEntity> _result = new ArrayList<MedLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpTakenDate;
            _tmpTakenDate = _cursor.getString(_cursorIndexOfTakenDate);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpDoseTaken;
            if (_cursor.isNull(_cursorIndexOfDoseTaken)) {
              _tmpDoseTaken = null;
            } else {
              _tmpDoseTaken = _cursor.getString(_cursorIndexOfDoseTaken);
            }
            _item = new MedLogEntity(_tmpId,_tmpMedicationId,_tmpTakenAt,_tmpTakenDate,_tmpScheduledTime,_tmpStatus,_tmpNote,_tmpDoseTaken);
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
  public Flow<List<MedLogEntity>> getRecentLogs(final long medId) {
    final String _sql = "SELECT * FROM med_logs WHERE medicationId = ? ORDER BY id DESC LIMIT 30";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, medId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"med_logs"}, new Callable<List<MedLogEntity>>() {
      @Override
      @NonNull
      public List<MedLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfTakenDate = CursorUtil.getColumnIndexOrThrow(_cursor, "takenDate");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDoseTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "doseTaken");
          final List<MedLogEntity> _result = new ArrayList<MedLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpTakenDate;
            _tmpTakenDate = _cursor.getString(_cursorIndexOfTakenDate);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpDoseTaken;
            if (_cursor.isNull(_cursorIndexOfDoseTaken)) {
              _tmpDoseTaken = null;
            } else {
              _tmpDoseTaken = _cursor.getString(_cursorIndexOfDoseTaken);
            }
            _item = new MedLogEntity(_tmpId,_tmpMedicationId,_tmpTakenAt,_tmpTakenDate,_tmpScheduledTime,_tmpStatus,_tmpNote,_tmpDoseTaken);
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
  public Object getAllMedLogsForUserOnce(final long userId,
      final Continuation<? super List<MedLogEntity>> $completion) {
    final String _sql = "SELECT ml.* FROM med_logs ml INNER JOIN medications m ON ml.medicationId = m.id WHERE m.userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MedLogEntity>>() {
      @Override
      @NonNull
      public List<MedLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMedicationId = CursorUtil.getColumnIndexOrThrow(_cursor, "medicationId");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfTakenDate = CursorUtil.getColumnIndexOrThrow(_cursor, "takenDate");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDoseTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "doseTaken");
          final List<MedLogEntity> _result = new ArrayList<MedLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MedLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMedicationId;
            _tmpMedicationId = _cursor.getLong(_cursorIndexOfMedicationId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpTakenDate;
            _tmpTakenDate = _cursor.getString(_cursorIndexOfTakenDate);
            final String _tmpScheduledTime;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmpScheduledTime = null;
            } else {
              _tmpScheduledTime = _cursor.getString(_cursorIndexOfScheduledTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpDoseTaken;
            if (_cursor.isNull(_cursorIndexOfDoseTaken)) {
              _tmpDoseTaken = null;
            } else {
              _tmpDoseTaken = _cursor.getString(_cursorIndexOfDoseTaken);
            }
            _item = new MedLogEntity(_tmpId,_tmpMedicationId,_tmpTakenAt,_tmpTakenDate,_tmpScheduledTime,_tmpStatus,_tmpNote,_tmpDoseTaken);
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
