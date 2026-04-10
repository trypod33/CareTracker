package com.caretracker.ui.people;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0012\u0010\u0012\u001a\u00020\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\b\u0010\u0015\u001a\u00020\u0011H\u0002J\b\u0010\u0016\u001a\u00020\u0011H\u0002J\b\u0010\u0017\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0018"}, d2 = {"Lcom/caretracker/ui/people/AddEditPersonActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/caretracker/databinding/ActivityAddEditPersonBinding;", "personId", "", "viewModel", "Lcom/caretracker/ui/people/PersonViewModel;", "getViewModel", "()Lcom/caretracker/ui/people/PersonViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "getAvatarForRole", "", "role", "loadPersonData", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "savePerson", "setupDropdowns", "showDatePicker", "app_debug"})
public final class AddEditPersonActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.caretracker.databinding.ActivityAddEditPersonBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private long personId = 0L;
    
    public AddEditPersonActivity() {
        super();
    }
    
    private final com.caretracker.ui.people.PersonViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupDropdowns() {
    }
    
    private final void showDatePicker() {
    }
    
    private final void loadPersonData() {
    }
    
    private final void savePerson() {
    }
    
    private final java.lang.String getAvatarForRole(java.lang.String role) {
        return null;
    }
}