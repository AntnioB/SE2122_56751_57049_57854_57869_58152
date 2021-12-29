package org.jabref.logic.importer.fetcher;

import com.google.gson.JsonObject;

import org.jabref.logic.importer.GoogleScholarProfilesParser;
import org.jabref.model.entry.BibEntry;
import serpapi.*;

import java.util.HashMap;
import java.util.Map;

public class GoogleScholarProfiles {

    Map<String, String> parameter = new HashMap<>();

    public GoogleScholarProfiles(String query) {
        parameter.put("engine", "google_scholar_profiles");
        parameter.put("mauthors", query);
        parameter.put("api_key", "83c392e3fe916c0ef08910e17d9ab0e07058b2be37e8121c3fa806514c2bb37d");
    }

    public BibEntry executeQuery() {
        GoogleSearch search = new GoogleSearch(parameter);
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
