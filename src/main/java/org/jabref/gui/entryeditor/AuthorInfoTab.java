package org.jabref.gui.entryeditor;

import java.util.*;
import java.util.stream.Collectors;

import javax.swing.undo.UndoManager;

import javafx.scene.control.Tooltip;

import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.autocompleter.SuggestionProviders;
import org.jabref.gui.entryeditor.FieldsEditorTab;
import org.jabref.gui.externalfiletype.ExternalFileTypes;
import org.jabref.gui.icon.IconTheme;
import org.jabref.gui.util.TaskExecutor;
import org.jabref.logic.importer.fetcher.GoogleScholarProfiles;
import org.jabref.logic.journals.JournalAbbreviationRepository;
import org.jabref.logic.l10n.Localization;
import org.jabref.logic.util.UpdateField;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryType;
import org.jabref.model.entry.BibEntryTypesManager;
import org.jabref.model.entry.field.BibField;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.InternalField;
import org.jabref.model.entry.field.StandardField;
import org.jabref.preferences.PreferencesService;

public class AuthorInfoTab extends FieldsEditorTab {
    private final List<Field> customTabFieldNames;
    private final BibEntryTypesManager entryTypesManager;

    public AuthorInfoTab(BibDatabaseContext databaseContext,
                          SuggestionProviders suggestionProviders,
                          UndoManager undoManager,
                          DialogService dialogService,
                          PreferencesService preferences,
                          StateManager stateManager,
                          BibEntryTypesManager entryTypesManager,
                          ExternalFileTypes externalFileTypes,
                          TaskExecutor taskExecutor,
                          JournalAbbreviationRepository journalAbbreviationRepository) {
        super(false,
                databaseContext,
                suggestionProviders,
                undoManager,
                dialogService,
                preferences,
                stateManager,
                externalFileTypes,
                taskExecutor,
                journalAbbreviationRepository);

        this.entryTypesManager = entryTypesManager;
        this.customTabFieldNames = preferences.getAllDefaultTabFieldNames();

        setText(Localization.lang("Author info"));
        setTooltip(new Tooltip(Localization.lang("Show remaining fields")));
        setGraphic(IconTheme.JabRefIcons.OPTIONAL.getGraphicNode());
    }

    @Override
    protected Set<Field> determineFieldsToShow(BibEntry entry) {
        Optional<BibEntryType> entryType = entryTypesManager.enrich(entry.getType(), databaseContext.getMode());
        if (entryType.isPresent()) {


            Set<Field> authorInfoFields=entryType.get().getAuthorInfoFields();


            Iterator<Field> it = entryType.get().getAllFields().iterator();
            Field field = null;

            while (it.hasNext()) {
                Field node = it.next();

                if (node.getName().equalsIgnoreCase("author"))
                    field = node;

            }

            assert field != null;

            Optional<String> opt = entry.getField(field);


            if(opt.isPresent()) {
                String value = opt.get();

                GoogleScholarProfiles gsp = new GoogleScholarProfiles(value);

                BibEntry bibEntry2 = gsp.executeQuery();

                UpdateField.updateField(entry, StandardField.AUTHOR_NAME, bibEntry2.getField(StandardField.AUTHOR).get());
            }


            return authorInfoFields;
        } else {
            // Entry type unknown -> treat all fields as required
            return Collections.emptySet();
        }
    }
}
