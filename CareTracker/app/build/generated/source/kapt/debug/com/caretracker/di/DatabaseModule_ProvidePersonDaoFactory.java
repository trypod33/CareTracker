package com.caretracker.di;

import com.caretracker.data.database.CareTrackerDatabase;
import com.caretracker.data.database.PersonDao;
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
public final class DatabaseModule_ProvidePersonDaoFactory implements Factory<PersonDao> {
  private final Provider<CareTrackerDatabase> databaseProvider;

  public DatabaseModule_ProvidePersonDaoFactory(Provider<CareTrackerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PersonDao get() {
    return providePersonDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePersonDaoFactory create(
      Provider<CareTrackerDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePersonDaoFactory(databaseProvider);
  }

  public static PersonDao providePersonDao(CareTrackerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePersonDao(database));
  }
}
