package org.jabref.gui.entryeditor;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryType;
import org.jabref.model.entry.field.BibField;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.OrFields;
import org.jabref.model.entry.field.StandardField;
import org.jabref.testutils.category.GUITest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@GUITest
@ExtendWith(ApplicationExtension.class)
class AuthorInfoTabTest {

    private BibEntryType bibEntryType=new BibEntryType(BibEntry.DEFAULT_TYPE, new LinkedHashSet<BibField>(), new LinkedHashSet<OrFields>());

    @BeforeAll
    public static void setupAll() {
        System.out.println("Starting the tests!");
    }

    //@DisplayName()
    @ParameterizedTest
    @MethodSource ("expectedFields")
    public void shouldDetermineFields() {
        Set<Field> receivedFields = bibEntryType.getAuthorInfoFields();
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

}
