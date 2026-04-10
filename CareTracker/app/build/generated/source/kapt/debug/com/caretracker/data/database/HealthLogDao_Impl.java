package com.caretracker.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.caretracker.data.models.HealthLog;
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
public final class HealthLogDao_Impl implements HealthLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HealthLog> __insertionAdapterOfHealthLog;

  private final EntityDeletionOrUpdateAdapter<HealthLog> __deletionAdapterOfHealthLog;

  private final EntityDeletionOrUpdateAdapter<HealthLog> __updateAdapterOfHealthLog;

  public HealthLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHealthLog = new EntityInsertionAdapter<HealthLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `health_logs` (`id`,`personId`,`metricId`,`value`,`notes`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        statement.bindLong(3, entity.getMetricId());
        if (entity.getValue() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getValue());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotes());
        }
        statement.bindLong(6, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfHealthLog = new EntityDeletionOrUpdateAdapter<HealthLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `health_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthLog entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHealthLog = new EntityDeletionOrUpdateAdapter<HealthLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `health_logs` SET `id` = ?,`personId` = ?,`metricId` = ?,`value` = ?,`notes` = ?,`timestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HealthLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPersonId());
        statement.bindLong(3, entity.getMetricId());
        if (entity.getValue() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getValue());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getNotes());
        }
        statement.bindLong(6, entity.getTimestamp());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final HealthLog log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHealthLog.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final HealthLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHealthLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final HealthLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHealthLog.handle(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HealthLog>> getLogsForPersonByType(final long personId, final long metricId) {
    final String _sql = "SELECT * FROM health_logs WHERE personId = ? AND metricId = ? ORDER BY timestamp DESC LIMIT 30";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, metricId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_logs"}, new Callable<List<HealthLog>>() {
      @Override
      @NonNull
      public List<HealthLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<HealthLog> _result = new ArrayList<HealthLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpMetricId;
            _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new HealthLog(_tmpId,_tmpPersonId,_tmpMetricId,_tmpValue,_tmpNotes,_tmpTimestamp);
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
  public Flow<List<HealthLog>> getAllLogsForPerson(final long personId) {
    final String _sql = "SELECT * FROM health_logs WHERE personId = ? ORDER BY timestamp DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"health_logs"}, new Callable<List<HealthLog>>() {
      @Override
      @NonNull
      public List<HealthLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<HealthLog> _result = new ArrayList<HealthLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpMetricId;
            _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new HealthLog(_tmpId,_tmpPersonId,_tmpMetricId,_tmpValue,_tmpNotes,_tmpTimestamp);
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
  public Object getLogsForPersonOnDate(final long personId, final long startOfDayMs,
      final long endOfDayMs, final Continuation<? super List<HealthLog>> $completion) {
    final String _sql = "SELECT * FROM health_logs WHERE personId = ? AND timestamp BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startOfDayMs);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endOfDayMs);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HealthLog>>() {
      @Override
      @NonNull
      public List<HealthLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<HealthLog> _result = new ArrayList<HealthLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpMetricId;
            _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new HealthLog(_tmpId,_tmpPersonId,_tmpMetricId,_tmpValue,_tmpNotes,_tmpTimestamp);
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
  public Object getLogsForPersonOnDateRange(final long personId, final long startOfDayMs,
      final long endOfDayMs, final Continuation<? super List<HealthLog>> $completion) {
    final String _sql = "SELECT * FROM health_logs WHERE personId = ? AND timestamp BETWEEN ? AND ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startOfDayMs);
    _argIndex = 3;
    _statement.bindLong(_argIndex, endOfDayMs);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HealthLog>>() {
      @Override
      @NonNull
      public List<HealthLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
          final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<HealthLog> _result = new ArrayList<HealthLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HealthLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpPersonId;
            _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
            final long _tmpMetricId;
            _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new HealthLog(_tmpId,_tmpPersonId,_tmpMetricId,_tmpValue,_tmpNotes,_tmpTimestamp);
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
  public Object getLogsInRange(final long personId, final long metricId, final long startMs,
      final long endMs, final Continuation<? super List<HealthLog>> $completion) {
    final String _sql = "SELECT * FROM health_logs WHERE personId = ? AND metricId = ? AND timestamp BETWEEN ? AND ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, personId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, metricId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, startMs);
    _argIndex = 4;
    _statement.bindLong(_argIndex, endMs);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<HealthLog>>() {
      @Override
      @NonNull
      public List<HealthLog> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfPersonId = CursorUtil.getColumnIndexOrThrow(_cursor, "personId");
            final int _cursorIndexOfMetricId = CursorUtil.getColumnIndexOrThrow(_cursor, "metricId");
            final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
            final List<HealthLog> _result = new ArrayList<HealthLog>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final HealthLog _item;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpPersonId;
              _tmpPersonId = _cursor.getLong(_cursorIndexOfPersonId);
              final long _tmpMetricId;
              _tmpMetricId = _cursor.getLong(_cursorIndexOfMetricId);
              final String _tmpValue;
              if (_cursor.isNull(_cursorIndexOfValue)) {
                _tmpValue = null;
              } else {
                _tmpValue = _cursor.getString(_cursorIndexOfValue);
              }
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final long _tmpTimestamp;
              _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
              _item = new HealthLog(_tmpId,_tmpPersonId,_tmpMetricId,_tmpValue,_tmpNotes,_tmpTimestamp);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
