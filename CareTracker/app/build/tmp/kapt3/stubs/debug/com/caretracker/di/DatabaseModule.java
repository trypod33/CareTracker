package com.caretracker.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0014"}, d2 = {"Lcom/caretracker/di/DatabaseModule;", "", "()V", "provideAppointmentDao", "Lcom/caretracker/data/database/AppointmentDao;", "database", "Lcom/caretracker/data/database/CareTrackerDatabase;", "provideContactDao", "Lcom/caretracker/data/database/ContactDao;", "provideDatabase", "context", "Landroid/content/Context;", "provideHabitDao", "Lcom/caretracker/data/database/HabitDao;", "provideHealthLogDao", "Lcom/caretracker/data/database/HealthLogDao;", "provideMedicationDao", "Lcom/caretracker/data/database/MedicationDao;", "providePersonDao", "Lcom/caretracker/data/database/PersonDao;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.caretracker.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.CareTrackerDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.PersonDao providePersonDao(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.CareTrackerDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.AppointmentDao provideAppointmentDao(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.CareTrackerDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.ContactDao provideContactDao(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.CareTrackerDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.MedicationDao provideMedicationDao(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.CareTrackerDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.HabitDao provideHabitDao(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.CareTrackerDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.caretracker.data.database.HealthLogDao provideHealthLogDao(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.CareTrackerDatabase database) {
        return null;
    }
}