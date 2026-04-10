package com.caretracker.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u0010\u000f\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/caretracker/data/repository/PersonRepository;", "", "personDao", "Lcom/caretracker/data/database/PersonDao;", "(Lcom/caretracker/data/database/PersonDao;)V", "allPeople", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/caretracker/data/models/Person;", "getAllPeople", "()Lkotlinx/coroutines/flow/Flow;", "deletePerson", "", "person", "(Lcom/caretracker/data/models/Person;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPersonById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPerson", "updatePerson", "app_debug"})
public final class PersonRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.caretracker.data.database.PersonDao personDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Person>> allPeople = null;
    
    @javax.inject.Inject()
    public PersonRepository(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.database.PersonDao personDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.caretracker.data.models.Person>> getAllPeople() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getPersonById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.caretracker.data.models.Person> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertPerson(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Person person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updatePerson(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Person person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deletePerson(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Person person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}