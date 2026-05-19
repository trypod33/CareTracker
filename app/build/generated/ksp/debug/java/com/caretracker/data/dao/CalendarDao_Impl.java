package com.caretracker.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.caretracker.data.entities.CalendarEventEntity;
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
public final class CalendarDao_Impl implements CalendarDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CalendarEventEntity> __insertionAdapterOfCalendarEventEntity;

  private final EntityDeletionOrUpdateAdapter<CalendarEventEntity> __deletionAdapterOfCalendarEventEntity;

  private final EntityDeletionOrUpdateAdapter<CalendarEventEntity> __updateAdapterOfCalendarEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEventsByUserId;

  public CalendarDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCalendarEventEntity = new EntityInsertionAdapter<CalendarEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `calendar_events` (`id`,`userId`,`title`,`description`,`category`,`color`,`startDatetime`,`endDatetime`,`allDay`,`location`,`reminderMinutes`,`recurrence`,`isCompleted`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindString(5, entity.getCategory());
        statement.bindString(6, entity.getColor());
        statement.bindLong(7, entity.getStartDatetime());
        if (entity.getEndDatetime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getEndDatetime());
        }
        final int _tmp = entity.getAllDay() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getLocation() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLocation());
        }
        statement.bindLong(11, entity.getReminderMinutes());
        statement.bindString(12, entity.getRecurrence());
        final int _tmp_1 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        statement.bindLong(14, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfCalendarEventEntity = new EntityDeletionOrUpdateAdapter<CalendarEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `calendar_events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEventEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCalendarEventEntity = new EntityDeletionOrUpdateAdapter<CalendarEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `calendar_events` SET `id` = ?,`userId` = ?,`title` = ?,`description` = ?,`category` = ?,`color` = ?,`startDatetime` = ?,`endDatetime` = ?,`allDay` = ?,`location` = ?,`reminderMinutes` = ?,`recurrence` = ?,`isCompleted` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindString(5, entity.getCategory());
        statement.bindString(6, entity.getColor());
        statement.bindLong(7, entity.getStartDatetime());
        if (entity.getEndDatetime() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getEndDatetime());
        }
        final int _tmp = entity.getAllDay() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getLocation() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLocation());
        }
        statement.bindLong(11, entity.getReminderMinutes());
        statement.bindString(12, entity.getRecurrence());
        final int _tmp_1 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteEventsByUserId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM calendar_events WHERE userId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final CalendarEventEntity event,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCalendarEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEvent(final CalendarEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCalendarEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEvent(final CalendarEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCalendarEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEventsByUserId(final long userId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEventsByUserId.acquire();
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
          __preparedStmtOfDeleteEventsByUserId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CalendarEventEntity>> getEventsForUser(final long userId) {
    final String _sql = "SELECT * FROM calendar_events WHERE userId = ? ORDER BY startDatetime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendar_events"}, new Callable<List<CalendarEventEntity>>() {
      @Override
      @NonNull
      public List<CalendarEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfStartDatetime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDatetime");
          final int _cursorIndexOfEndDatetime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDatetime");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinutes");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CalendarEventEntity> _result = new ArrayList<CalendarEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final long _tmpStartDatetime;
            _tmpStartDatetime = _cursor.getLong(_cursorIndexOfStartDatetime);
            final Long _tmpEndDatetime;
            if (_cursor.isNull(_cursorIndexOfEndDatetime)) {
              _tmpEndDatetime = null;
            } else {
              _tmpEndDatetime = _cursor.getLong(_cursorIndexOfEndDatetime);
            }
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpReminderMinutes;
            _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            final String _tmpRecurrence;
            _tmpRecurrence = _cursor.getString(_cursorIndexOfRecurrence);
            final boolean _tmpIsCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CalendarEventEntity(_tmpId,_tmpUserId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpColor,_tmpStartDatetime,_tmpEndDatetime,_tmpAllDay,_tmpLocation,_tmpReminderMinutes,_tmpRecurrence,_tmpIsCompleted,_tmpCreatedAt);
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
  public Flow<List<CalendarEventEntity>> getEventsInRange(final long userId, final long start,
      final long end) {
    final String _sql = "SELECT * FROM calendar_events WHERE userId = ? AND startDatetime >= ? AND startDatetime <= ? ORDER BY startDatetime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, start);
    _argIndex = 3;
    _statement.bindLong(_argIndex, end);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendar_events"}, new Callable<List<CalendarEventEntity>>() {
      @Override
      @NonNull
      public List<CalendarEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfStartDatetime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDatetime");
          final int _cursorIndexOfEndDatetime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDatetime");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinutes");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CalendarEventEntity> _result = new ArrayList<CalendarEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final long _tmpStartDatetime;
            _tmpStartDatetime = _cursor.getLong(_cursorIndexOfStartDatetime);
            final Long _tmpEndDatetime;
            if (_cursor.isNull(_cursorIndexOfEndDatetime)) {
              _tmpEndDatetime = null;
            } else {
              _tmpEndDatetime = _cursor.getLong(_cursorIndexOfEndDatetime);
            }
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpReminderMinutes;
            _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            final String _tmpRecurrence;
            _tmpRecurrence = _cursor.getString(_cursorIndexOfRecurrence);
            final boolean _tmpIsCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CalendarEventEntity(_tmpId,_tmpUserId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpColor,_tmpStartDatetime,_tmpEndDatetime,_tmpAllDay,_tmpLocation,_tmpReminderMinutes,_tmpRecurrence,_tmpIsCompleted,_tmpCreatedAt);
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
  public Object getAllEventsForUserOnce(final long userId,
      final Continuation<? super List<CalendarEventEntity>> $completion) {
    final String _sql = "SELECT * FROM calendar_events WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CalendarEventEntity>>() {
      @Override
      @NonNull
      public List<CalendarEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfStartDatetime = CursorUtil.getColumnIndexOrThrow(_cursor, "startDatetime");
          final int _cursorIndexOfEndDatetime = CursorUtil.getColumnIndexOrThrow(_cursor, "endDatetime");
          final int _cursorIndexOfAllDay = CursorUtil.getColumnIndexOrThrow(_cursor, "allDay");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinutes");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CalendarEventEntity> _result = new ArrayList<CalendarEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final long _tmpStartDatetime;
            _tmpStartDatetime = _cursor.getLong(_cursorIndexOfStartDatetime);
            final Long _tmpEndDatetime;
            if (_cursor.isNull(_cursorIndexOfEndDatetime)) {
              _tmpEndDatetime = null;
            } else {
              _tmpEndDatetime = _cursor.getLong(_cursorIndexOfEndDatetime);
            }
            final boolean _tmpAllDay;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAllDay);
            _tmpAllDay = _tmp != 0;
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final int _tmpReminderMinutes;
            _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            final String _tmpRecurrence;
            _tmpRecurrence = _cursor.getString(_cursorIndexOfRecurrence);
            final boolean _tmpIsCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CalendarEventEntity(_tmpId,_tmpUserId,_tmpTitle,_tmpDescription,_tmpCategory,_tmpColor,_tmpStartDatetime,_tmpEndDatetime,_tmpAllDay,_tmpLocation,_tmpReminderMinutes,_tmpRecurrence,_tmpIsCompleted,_tmpCreatedAt);
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
