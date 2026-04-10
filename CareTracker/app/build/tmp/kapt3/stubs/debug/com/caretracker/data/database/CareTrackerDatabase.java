package com.caretracker.data.database;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u000f"}, d2 = {"Lcom/caretracker/data/database/CareTrackerDatabase;", "Landroidx/room/RoomDatabase;", "()V", "appointmentDao", "Lcom/caretracker/data/database/AppointmentDao;", "contactDao", "Lcom/caretracker/data/database/ContactDao;", "habitDao", "Lcom/caretracker/data/database/HabitDao;", "healthLogDao", "Lcom/caretracker/data/database/HealthLogDao;", "medicationDao", "Lcom/caretracker/data/database/MedicationDao;", "personDao", "Lcom/caretracker/data/database/PersonDao;", "app_debug"})
@androidx.room.Database(entities = {com.caretracker.data.models.Person.class, com.caretracker.data.models.Appointment.class, com.caretracker.data.models.Contact.class, com.caretracker.data.models.Medication.class, com.caretracker.data.models.MedicationLog.class, com.caretracker.data.models.Habit.class, com.caretracker.data.models.HabitLog.class, com.caretracker.data.models.HealthLog.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.caretracker.data.models.Converters.class})
public abstract class CareTrackerDatabase extends androidx.room.RoomDatabase {
    
    public CareTrackerDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.caretracker.data.database.PersonDao personDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.caretracker.data.database.AppointmentDao appointmentDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.caretracker.data.database.ContactDao contactDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.caretracker.data.database.MedicationDao medicationDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.caretracker.data.database.HabitDao habitDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.caretracker.data.database.HealthLogDao healthLogDao();
}