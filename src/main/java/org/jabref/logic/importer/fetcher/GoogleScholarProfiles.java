package org.jabref.logic.importer.fetcher;

import com.google.gson.JsonObject;

import org.jabref.logic.importer.GoogleScholarProfilesParser;
import org.jabref.model.entry.BibEntry;
import serpapi.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GoogleScholarProfiles {

    private Map<String, String> parameter = new HashMap<>();

    public GoogleScholarProfiles(String query) {
        parameter.put("mauthors", query);
    }

    public BibEntry executeQuery() {
        SerpApiSearch search = new SerpApiSearch(parameter , "83c392e3fe916c0ef08910e17d9ab0e07058b2be37e8121c3fa806514c2bb37d", "google_scholar_profiles");
        GoogleScholarProfilesParser parser = null;

        try {
            JsonObject results = search.getJson();
            parser = new GoogleScholarProfilesParser(results);
        } catch (
                SerpApiSearchException ex) {
            System.out.println(ex);
        }
        return parser.parseEntries();
    }

}
