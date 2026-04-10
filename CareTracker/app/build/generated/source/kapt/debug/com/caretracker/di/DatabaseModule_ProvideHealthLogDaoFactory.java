package com.caretracker.di;

import com.caretracker.data.database.CareTrackerDatabase;
import com.caretracker.data.database.HealthLogDao;
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
public final class DatabaseModule_ProvideHealthLogDaoFactory implements Factory<HealthLogDao> {
  private final Provider<CareTrackerDatabase> databaseProvider;

  public DatabaseModule_ProvideHealthLogDaoFactory(Provider<CareTrackerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public HealthLogDao get() {
    return provideHealthLogDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideHealthLogDaoFactory create(
      Provider<CareTrackerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideHealthLogDaoFactory(databaseProvider);
  }

  public static HealthLogDao provideHealthLogDao(CareTrackerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideHealthLogDao(database));
  }
}
