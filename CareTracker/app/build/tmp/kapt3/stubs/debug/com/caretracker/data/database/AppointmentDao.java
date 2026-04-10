package com.caretracker.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\n\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\tH\'J$\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\tH\'J\u001c\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\u0015\u001a\u00020\tH\'J$\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\tH\'J\u0016\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0019"}, d2 = {"Lcom/caretracker/data/database/AppointmentDao;", "", "delete", "", "appointment", "Lcom/caretracker/data/models/Appointment;", "(Lcom/caretracker/data/models/Appointment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePastAppointments", "thresholdMs", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAppointmentById", "id", "getAppointmentsForPerson", "Lkotlinx/coroutines/flow/Flow;", "", "personId", "getAppointmentsInRange", "startMs", "endMs", "getUpcomingAppointments", "nowMs", "getUpcomingAppointmentsForPerson", "insert", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface AppointmentDao {
    
    @androidx.room.Query(value = "SELECT * FROM appointments WHERE personId = :personId ORDER BY dateTime ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Appointment>> getAppointmentsForPerson(long personId);
    
    @androidx.room.Query(value = "SELECT * FROM appointments WHERE dateTime >= :startMs AND dateTime <= :endMs ORDER BY dateTime ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Appointment>> getAppointmentsInRange(long startMs, long endMs);
    
    @androidx.room.Query(value = "SELECT * FROM appointments WHERE dateTime >= :nowMs ORDER BY dateTime ASC LIMIT 10")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Appointment>> getUpcomingAppointments(long nowMs);
    
    @androidx.room.Query(value = "SELECT * FROM appointments WHERE personId = :personId AND dateTime >= :nowMs ORDER BY dateTime ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Appointment>> getUpcomingAppointmentsForPerson(long personId, long nowMs);
    
    @androidx.room.Query(value = "SELECT * FROM appointments WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAppointmentById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.caretracker.data.models.Appointment> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Appointment appointment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Appointment appointment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Appointment appointment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM appointments WHERE dateTime < :thresholdMs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletePastAppointments(long thresholdMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}