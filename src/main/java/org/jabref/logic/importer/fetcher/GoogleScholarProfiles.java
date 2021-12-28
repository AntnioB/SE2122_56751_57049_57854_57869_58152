package org.jabref.logic.importer.fetcher;

import com.google.gson.JsonObject;

import serpapi.*;

import java.util.HashMap;
import java.util.Map;

public class GoogleScholarProfiles {
    public GoogleScholarProfiles() {
        Map<String, String> parameter = new HashMap<>();

        parameter.put("engine", "google_scholar_profiles");
        parameter.put("mauthors", "Mike");
        parameter.put("api_key", "83c392e3fe916c0ef08910e17d9ab0e07058b2be37e8121c3fa806514c2bb37d");

        GoogleSearch search = new GoogleSearch(parameter);

        try {
            JsonObject results = search.getJson();
            var profiles = results.get("profiles");
        } catch (
                SerpApiSearchException ex) {
        }
    }
}
