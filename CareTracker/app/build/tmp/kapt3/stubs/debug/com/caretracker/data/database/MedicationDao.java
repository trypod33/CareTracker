package com.caretracker.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0011\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u001c\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\t0\b2\u0006\u0010\f\u001a\u00020\rH\'J,\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\t2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0011J,\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\t2\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0015\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u001c\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\u0013\u001a\u00020\rH\'J\u0014\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\bH\'J\u0016\u0010\u0019\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u001e"}, d2 = {"Lcom/caretracker/data/database/MedicationDao;", "", "delete", "", "medication", "Lcom/caretracker/data/models/Medication;", "(Lcom/caretracker/data/models/Medication;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllActiveMedications", "Lkotlinx/coroutines/flow/Flow;", "", "getLogsForMedication", "Lcom/caretracker/data/models/MedicationLog;", "medicationId", "", "getLogsForMedicationInTimeRange", "startMs", "endMs", "(JJJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLogsForPersonInTimeRange", "personId", "getMedicationById", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMedicationsForPerson", "getMedicationsNeedingRefill", "insert", "insertLog", "log", "(Lcom/caretracker/data/models/MedicationLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface MedicationDao {
    
    @androidx.room.Query(value = "SELECT * FROM medications WHERE personId = :personId AND isActive = 1 ORDER BY name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Medication>> getMedicationsForPerson(long personId);
    
    @androidx.room.Query(value = "SELECT * FROM medications WHERE isActive = 1 ORDER BY personId, name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Medication>> getAllActiveMedications();
    
    @androidx.room.Query(value = "SELECT * FROM medications WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMedicationById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.caretracker.data.models.Medication> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM medications WHERE pillsRemaining <= refillReminderAt AND isActive = 1 ORDER BY pillsRemaining ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Medication>> getMedicationsNeedingRefill();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Medication medication, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Medication medication, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Medication medication, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertLog(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.MedicationLog log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM medication_logs WHERE medicationId = :medicationId ORDER BY takenAt DESC LIMIT 30")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.MedicationLog>> getLogsForMedication(long medicationId);
    
    @androidx.room.Query(value = "SELECT * FROM medication_logs WHERE personId = :personId AND takenAt BETWEEN :startMs AND :endMs ORDER BY takenAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLogsForPersonInTimeRange(long personId, long startMs, long endMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.caretracker.data.models.MedicationLog>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM medication_logs WHERE medicationId = :medicationId AND takenAt BETWEEN :startMs AND :endMs ORDER BY takenAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLogsForMedicationInTimeRange(long medicationId, long startMs, long endMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.caretracker.data.models.MedicationLog>> $completion);
}