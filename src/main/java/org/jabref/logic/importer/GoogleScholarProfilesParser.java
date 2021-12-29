package org.jabref.logic.importer;

import com.google.gson.*;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;

public class GoogleScholarProfilesParser {

    JsonObject jsonObject;

    public GoogleScholarProfilesParser(JsonObject jsonObject) {
        this.jsonObject=jsonObject;
    }

    public BibEntry parseEntries() {

        BibEntry bibEntry = new BibEntry();

        if (jsonObject.has("profiles")) {
            JsonArray jsonArray = jsonObject.get("profiles").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray
            ) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.has("name")) {
                    bibEntry.setField(StandardField.AUTHOR_NAME, jsonObject.get("name").getAsString());
                }

                if (jsonObject.has("affiliations")) {
                    bibEntry.setField(StandardField.AFFILIATION, jsonObject.get("affiliations").getAsString());
                }

                if (jsonObject.has("email")) {
                    bibEntry.setField(StandardField.EMAIL, jsonObject.get("email").getAsString());
                }

                if (jsonObject.has("interests")) {
                    JsonArray jsonArray2 = jsonObject.get("interests").getAsJsonArray();

                    for (JsonElement jsonElement2 : jsonArray2
                    ) {
                        JsonObject jsonObject2 = jsonElement2.getAsJsonObject();

                        if (jsonObject2.has("title")) {
                            bibEntry.setField(StandardField.INTERESTS, jsonObject.get("INTERESTS").getAsString());
                        }
                    }
                }


            }

        }

        return bibEntry;

    }
}
