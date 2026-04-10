package com.caretracker.di;

import com.caretracker.data.database.CareTrackerDatabase;
import com.caretracker.data.database.HabitDao;
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
public final class DatabaseModule_ProvideHabitDaoFactory implements Factory<HabitDao> {
  private final Provider<CareTrackerDatabase> databaseProvider;

  public DatabaseModule_ProvideHabitDaoFactory(Provider<CareTrackerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public HabitDao get() {
    return provideHabitDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideHabitDaoFactory create(
      Provider<CareTrackerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideHabitDaoFactory(databaseProvider);
  }

  public static HabitDao provideHabitDao(CareTrackerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideHabitDao(database));
  }
}
