package com.caretracker.ui.people;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/caretracker/ui/people/PersonViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/caretracker/data/repository/PersonRepository;", "(Lcom/caretracker/data/repository/PersonRepository;)V", "allPeople", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/caretracker/data/models/Person;", "getAllPeople", "()Lkotlinx/coroutines/flow/StateFlow;", "deletePerson", "", "person", "savePerson", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PersonViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.caretracker.data.repository.PersonRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.caretracker.data.models.Person>> allPeople = null;
    
    @javax.inject.Inject()
    public PersonViewModel(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.repository.PersonRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.caretracker.data.models.Person>> getAllPeople() {
        return null;
    }
    
    public final void savePerson(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Person person) {
    }
    
    public final void deletePerson(@org.jetbrains.annotations.NotNull()
    com.caretracker.data.models.Person person) {
    }
}