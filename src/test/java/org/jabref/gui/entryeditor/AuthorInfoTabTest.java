package org.jabref.gui.entryeditor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jabref.logic.importer.GoogleScholarProfilesParser;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.BibEntryType;
import org.jabref.model.entry.field.BibField;
import org.jabref.model.entry.field.Field;
import org.jabref.model.entry.field.OrFields;
import org.jabref.model.entry.field.StandardField;
import org.jabref.testutils.category.GUITest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@GUITest
@ExtendWith(ApplicationExtension.class)
class AuthorInfoTabTest {

    String jsonString = "{\n" +
            "  \"search_metadata\": {\n" +
            "    \"id\": \"61ccd4ba116a5572fc590046\",\n" +
            "    \"status\": \"Success\",\n" +
            "    \"json_endpoint\": \"https://serpapi.com/searches/98e234f0556d61db/61ccd4ba116a5572fc590046.json\",\n" +
            "    \"created_at\": \"2021-12-29 21:35:54 UTC\",\n" +
            "    \"processed_at\": \"2021-12-29 21:35:54 UTC\",\n" +
            "    \"google_scholar_profiles_url\": \"https://scholar.google.com/citations?mauthors=Ravara%2C+Antonio&view_op=search_authors&hl=en\",\n" +
            "    \"raw_html_file\": \"https://serpapi.com/searches/98e234f0556d61db/61ccd4ba116a5572fc590046.html\",\n" +
            "    \"total_time_taken\": 2.57\n" +
            "  },\n" +
            "  \"search_parameters\": {\n" +
            "    \"engine\": \"google_scholar_profiles\",\n" +
            "    \"mauthors\": \"Ravara, Antonio\",\n" +
            "    \"hl\": \"en\"\n" +
            "  },\n" +
            "  \"profiles\": [\n" +
            "    {\n" +
            "      \"name\": \"Antonio Ravara\",\n" +
            "      \"link\": \"https://scholar.google.com/citations?hl=en&user=HuxP2pUAAAAJ\",\n" +
            "      \"serpapi_link\": \"https://serpapi.com/search.json?author_id=HuxP2pUAAAAJ&engine=google_scholar_author&hl=en\",\n" +
            "      \"author_id\": \"HuxP2pUAAAAJ\",\n" +
            "      \"affiliations\": \"Associate Professor, Department of Informatics, New University of Lisbon\",\n" +
            "      \"email\": \"Verified email at fct.unl.pt\",\n" +
            "      \"cited_by\": 1485,\n" +
            "      \"interests\": [\n" +
            "        {\n" +
            "          \"title\": \"Programming languages\",\n" +
            "          \"serpapi_link\": \"https://serpapi.com/search.json?engine=google_scholar_profiles&hl=en&mauthors=label%3Aprogramming_languages\",\n" +
            "          \"link\": \"https://scholar.google.com/citations?hl=en&view_op=search_authors&mauthors=label:programming_languages\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"type systems\",\n" +
            "          \"serpapi_link\": \"https://serpapi.com/search.json?engine=google_scholar_profiles&hl=en&mauthors=label%3Atype_systems\",\n" +
            "          \"link\": \"https://scholar.google.com/citations?hl=en&view_op=search_authors&mauthors=label:type_systems\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"concurrency\",\n" +
            "          \"serpapi_link\": \"https://serpapi.com/search.json?engine=google_scholar_profiles&hl=en&mauthors=label%3Aconcurrency\",\n" +
            "          \"link\": \"https://scholar.google.com/citations?hl=en&view_op=search_authors&mauthors=label:concurrency\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"logic in computer science\",\n" +
            "          \"serpapi_link\": \"https://serpapi.com/search.json?engine=google_scholar_profiles&hl=en&mauthors=label%3Alogic_in_computer_science\",\n" +
            "          \"link\": \"https://scholar.google.com/citations?hl=en&view_op=search_authors&mauthors=label:logic_in_computer_science\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"title\": \"specification and verification of systems\",\n" +
            "          \"serpapi_link\": \"https://serpapi.com/search.json?engine=google_scholar_profiles&hl=en&mauthors=label%3Aspecification_and_verification_of_systems\",\n" +
            "          \"link\": \"https://scholar.google.com/citations?hl=en&view_op=search_authors&mauthors=label:specification_and_verification_of_systems\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"thumbnail\": \"https://scholar.googleusercontent.com/citations?view_op=small_photo&user=HuxP2pUAAAAJ&citpid=2\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    private BibEntryType bibEntryType=new BibEntryType(BibEntry.DEFAULT_TYPE, new LinkedHashSet<BibField>(), new LinkedHashSet<OrFields>());

    @BeforeAll
    public static void setupAll() {
        System.out.println("Starting the tests!");
    }

    //@DisplayName()
    @ParameterizedTest
    @MethodSource ("expectedFields")
    public void shouldDetermineFields(Field expectedField) {
        Set<Field> receivedFields = bibEntryType.getAuthorInfoFields();
        assertFalse(receivedFields.isEmpty());
        assertEquals(4, receivedFields.size());
        assertTrue(receivedFields.contains(expectedField));
    }

    private static Set<Field> expectedFields() {
        Set<Field> expectedFields = new HashSet<Field>();
        expectedFields.add(StandardField.AUTHOR_NAME);
        expectedFields.add(StandardField.AFFILIATION);
        expectedFields.add(StandardField.EMAIL);
        expectedFields.add(StandardField.INTERESTS);


        return expectedFields;
    }

    @Test
    public void shouldParseCorrectly() {

        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
        assertTrue(jsonObject.isJsonObject());

        GoogleScholarProfilesParser parser = new GoogleScholarProfilesParser(jsonObject);

        BibEntry bibEntry = parser.parseEntries();

        BibEntry bibEntry2 = new BibEntry();

        bibEntry2.setField(StandardField.AUTHOR_NAME, "Antonio Ravara");
        bibEntry2.setField(StandardField.AFFILIATION, "Associate Professor, Department of Informatics, New University of Lisbon");
        bibEntry2.setField(StandardField.EMAIL, "Verified email at fct.unl.pt");
        bibEntry2.setField(StandardField.INTERESTS, "Programming languages, type systems, concurrency, logic in computer science, specification and verification of systems");

        assertEquals(bibEntry,bibEntry2);
    }

}
