package com.caretracker.di;

import com.caretracker.data.database.CareTrackerDatabase;
import com.caretracker.data.database.ContactDao;
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
public final class DatabaseModule_ProvideContactDaoFactory implements Factory<ContactDao> {
  private final Provider<CareTrackerDatabase> databaseProvider;

  public DatabaseModule_ProvideContactDaoFactory(Provider<CareTrackerDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ContactDao get() {
    return provideContactDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideContactDaoFactory create(
      Provider<CareTrackerDatabase> databaseProvider) {
    return new DatabaseModule_ProvideContactDaoFactory(databaseProvider);
  }

  public static ContactDao provideContactDao(CareTrackerDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideContactDao(database));
  }
}
