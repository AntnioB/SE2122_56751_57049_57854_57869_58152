package org.jabref.gui.entryeditor;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.autocompleter.SuggestionProviders;
import org.jabref.gui.externalfiletype.ExternalFileTypes;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.logic.journals.JournalAbbreviationRepository;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryTypesManager;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.StandardField;
import org.jabref.preferences.PreferencesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.swing.undo.UndoManager;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class AuthorInfoTabTest {

    AuthorInfoTab info;
    private BibDatabaseContext databaseContext = mock(BibDatabaseContext.class);
    private SuggestionProviders suggestionProviders = mock(SuggestionProviders.class);
    private UndoManager undoManager = mock(UndoManager.class);
    private DialogService dialogService = spy(DialogService.class);
    private PreferencesService preferences =  mock(PreferencesService.class);
    private StateManager stateManager = mock(StateManager.class);
    private BibEntryTypesManager entryTypesManager = mock(BibEntryTypesManager.class);
    private ExternalFileTypes externalFileTypes = mock(ExternalFileTypes.class);
    private TaskExecutor taskExecutor = mock(TaskExecutor.class);
    private JournalAbbreviationRepository journalAbbreviationRepository = mock(JournalAbbreviationRepository.class);
    private BibEntry bibEntry;

    @BeforeAll
    public static void setupAll() {
        System.out.println("Starting the tests!");
    }

    @BeforeEach
    public void setup() {

        info = new AuthorInfoTab( databaseContext,
                 suggestionProviders,
                 undoManager,
                 dialogService,
                 preferences,
                 stateManager,
                 entryTypesManager,
                 externalFileTypes,
                 taskExecutor,
                 journalAbbreviationRepository);
    }

    //@DisplayName()
    @ParameterizedTest
    @MethodSource ("expectedFields")
    public void shouldDetermineFields() {
        Set<Field> receivedFields = info.determineFieldsToShow(bibEntry);
        assertFalse(receivedFields.isEmpty());
        assertEquals(4, receivedFields.size());
    }

    private static Set<Field> expectedFields() {
        Set<Field> expectedFields = new HashSet<Field>();
        expectedFields.add(StandardField.AUTHOR_NAME);
        expectedFields.add(StandardField.AFFILIATION);
        expectedFields.add(StandardField.EMAIL);
        expectedFields.add(StandardField.INTERESTS);


       return expectedFields;
    }

    //{StandardField.AUTHOR_NAME, StandardField.AFFILIATION, StandardField.EMAIL, StandardField.INTERESTS}
}
