package com.caretracker.di;

import com.caretracker.data.database.AppointmentDao;
import com.caretracker.data.database.CareTrackerDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideAppointmentDaoFactory implements Factory<AppointmentDao> {
  private final Provider<CareTrackerDatabase> databaseProvider;

  public DatabaseModule_ProvideAppointmentDaoFactory(
      Provider<CareTrackerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AppointmentDao get() {
    return provideAppointmentDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideAppointmentDaoFactory create(
      Provider<CareTrackerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideAppointmentDaoFactory(databaseProvider);
  }

  public static AppointmentDao provideAppointmentDao(CareTrackerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppointmentDao(database));
  }
}
