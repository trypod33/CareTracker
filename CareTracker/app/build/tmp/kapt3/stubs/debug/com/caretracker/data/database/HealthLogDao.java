package com.caretracker.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0002\b\u000e\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000bH\'J$\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bH\'J,\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0011J,\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0011J4\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0019"}, d2 = {"Lcom/caretracker/data/database/HealthLogDao;", "", "delete", "", "log", "Lcom/caretracker/data/models/HealthLog;", "(Lcom/caretracker/data/models/HealthLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllLogsForPerson", "Lkotlinx/coroutines/flow/Flow;", "", "personId", "", "getLogsForPersonByType", "metricId", "getLogsForPersonOnDate", "startOfDayMs", "endOfDayMs", "(JJJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLogsForPersonOnDateRange", "getLogsInRange", "startMs", "endMs", "(JJJJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface HealthLogDao {
    
    @androidx.room.Query(value = "SELECT * FROM health_logs WHERE personId = :personId AND metricId = :metricId ORDER BY timestamp DESC LIMIT 30")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.HealthLog>> getLogsForPersonByType(long personId, long metricId);
    
    @androidx.room.Query(value = "SELECT * FROM health_logs WHERE personId = :personId ORDER BY timestamp DESC LIMIT 50")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.HealthLog>> getAllLogsForPerson(long personId);
    
    @androidx.room.Query(value = "SELECT * FROM health_logs WHERE personId = :personId AND timestamp BETWEEN :startOfDayMs AND :endOfDayMs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLogsForPersonOnDate(long personId, long startOfDayMs, long endOfDayMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.caretracker.data.models.HealthLog>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM health_logs WHERE personId = :personId AND timestamp BETWEEN :startOfDayMs AND :endOfDayMs ORDER BY timestamp ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLogsForPersonOnDateRange(long personId, long startOfDayMs, long endOfDayMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.caretracker.data.models.HealthLog>> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM health_logs WHERE personId = :personId AND metricId = :metricId AND timestamp BETWEEN :startMs AND :endMs ORDER BY timestamp ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLogsInRange(long personId, long metricId, long startMs, long endMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.caretracker.data.models.HealthLog>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.HealthLog log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.HealthLog log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.HealthLog log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}