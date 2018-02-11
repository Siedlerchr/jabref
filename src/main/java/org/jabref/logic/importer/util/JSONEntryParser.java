package org.jabref.logic.importer.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;
import org.jabref.model.entry.Month;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONEntryParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONEntryParser.class);

    /**
     * Convert a JSONObject containing a bibJSON entry to a BibEntry
     *
     * @param bibJsonEntry The JSONObject to convert
     * @return the converted BibEntry
     */
    public BibEntry parseBibJSONtoBibtex(JsonObject bibJsonEntry, Character keywordSeparator) {
        // Fields that are directly accessible at the top level BibJson object
        String[] singleFieldStrings = {FieldName.YEAR, FieldName.TITLE, FieldName.ABSTRACT, FieldName.MONTH};

        // Fields that are accessible in the journal part of the BibJson object
        String[] journalSingleFieldStrings = {FieldName.PUBLISHER, FieldName.NUMBER, FieldName.VOLUME};

        BibEntry entry = new BibEntry();
        entry.setType("article");

        // Authors
        if (bibJsonEntry.has("author")) {
            JsonArray authors = bibJsonEntry.getAsJsonArray("author");
            List<String> authorList = new ArrayList<>();
            for (int i = 0; i < authors.size(); i++) {
                if (authors.get(i).getAsJsonObject().has("name")) {
                    authorList.add(authors.get(i).getAsJsonObject().get("name").getAsString());
                } else {
                    LOGGER.info("Empty author name.");
                }
            }
            entry.setField(FieldName.AUTHOR, String.join(" and ", authorList));
        } else {
            LOGGER.info("No author found.");
        }

        // Direct accessible fields
        for (String field : singleFieldStrings) {
            if (bibJsonEntry.has(field)) {
                entry.setField(field, bibJsonEntry.get(field).getAsString());
            }
        }

        // Page numbers
        if (bibJsonEntry.has("start_page")) {
            if (bibJsonEntry.has("end_page")) {
                entry.setField(FieldName.PAGES,
                        bibJsonEntry.get("start_page").getAsString() + "--" + bibJsonEntry.get("end_page").getAsString());
            } else {
                entry.setField(FieldName.PAGES, bibJsonEntry.get("start_page").getAsString());
            }
        }

        // Journal
        if (bibJsonEntry.has("journal")) {
            JsonObject journal = bibJsonEntry.getAsJsonObject("journal");
            // Journal title
            if (journal.has("title")) {
                entry.setField(FieldName.JOURNAL, journal.get("title").getAsString());
            } else {
                LOGGER.info("No journal title found.");
            }
            // Other journal related fields
            for (String field : journalSingleFieldStrings) {
                if (journal.has(field)) {
                    entry.setField(field, journal.get(field).getAsString());
                }
            }
        } else {
            LOGGER.info("No journal information found.");
        }

        // Keywords
        if (bibJsonEntry.has("keywords")) {
            JsonArray keywords = bibJsonEntry.getAsJsonArray("keywords");
            for (int i = 0; i < keywords.size(); i++) {
                if (!keywords.get(i).isJsonNull()) {
                    entry.addKeyword(keywords.get(i).getAsString(), keywordSeparator);
                }
            }
        }

        // Identifiers
        if (bibJsonEntry.has("identifier")) {
            JsonArray identifiers = bibJsonEntry.getAsJsonArray("identifier");
            for (int i = 0; i < identifiers.size(); i++) {
                String type = identifiers.get(i).getAsJsonObject().get("type").getAsString();
                if ("doi".equals(type)) {
                    entry.setField(FieldName.DOI, identifiers.get(i).getAsJsonObject().get("id").getAsString());
                } else if ("pissn".equals(type)) {
                    entry.setField(FieldName.ISSN, identifiers.get(i).getAsJsonObject().get("id").getAsString());
                } else if ("eissn".equals(type)) {
                    entry.setField(FieldName.ISSN, identifiers.get(i).getAsJsonObject().get("id").getAsString());
                }
            }
        }

        // Links
        if (bibJsonEntry.has("link")) {
            JsonArray links = bibJsonEntry.getAsJsonArray("link");
            for (int i = 0; i < links.size(); i++) {
                if (links.get(i).getAsJsonObject().has("type")) {
                    String type = links.get(i).getAsJsonObject().get("type").getAsString();
                    if ("fulltext".equals(type) && links.get(i).getAsJsonObject().has("url")) {
                        entry.setField(FieldName.URL, links.get(i).getAsJsonObject().get("url").getAsString());
                    }
                }
            }
        }

        return entry;
    }

    /**
     * Convert a JSONObject obtained from http://api.springer.com/metadata/json to a BibEntry
     *
     * @param springerJsonEntry the JSONObject from search results
     * @return the converted BibEntry
     */
    public static BibEntry parseSpringerJSONtoBibtex(JsonObject springerJsonEntry) {
        // Fields that are directly accessible at the top level Json object
        String[] singleFieldStrings = {FieldName.ISSN, FieldName.VOLUME, FieldName.ABSTRACT, FieldName.DOI, FieldName.TITLE, FieldName.NUMBER,
                FieldName.PUBLISHER};

        BibEntry entry = new BibEntry();
        String nametype;

        // Guess publication type
        String isbn = springerJsonEntry.get("isbn").getAsString();
        if (com.google.common.base.Strings.isNullOrEmpty(isbn)) {
            // Probably article
            entry.setType("article");
            nametype = FieldName.JOURNAL;
        } else {
            // Probably book chapter or from proceeding, go for book chapter
            entry.setType("incollection");
            nametype = FieldName.BOOKTITLE;
            entry.setField(FieldName.ISBN, isbn);
        }

        // Authors
        if (springerJsonEntry.has("creators")) {
            JsonArray authors = springerJsonEntry.get("creators").getAsJsonArray();
            List<String> authorList = new ArrayList<>();
            for (int i = 0; i < authors.size(); i++) {
                if (authors.get(i).getAsJsonObject().has("creator")) {
                    authorList.add(authors.get(i).getAsJsonObject().get("creator").getAsString());
                } else {
                    LOGGER.info("Empty author name.");
                }
            }
            entry.setField(FieldName.AUTHOR, String.join(" and ", authorList));
        } else {
            LOGGER.info("No author found.");
        }

        // Direct accessible fields
        for (String field : singleFieldStrings) {
            if (springerJsonEntry.has(field)) {
                String text = springerJsonEntry.get(field).getAsString();
                if (!text.isEmpty()) {
                    entry.setField(field, text);
                }
            }
        }

        // Page numbers
        if (springerJsonEntry.has("startingPage") && !(springerJsonEntry.get("startingPage").getAsString().isEmpty())) {
            if (springerJsonEntry.has("endPage") && !(springerJsonEntry.get("endPage").getAsString().isEmpty())) {
                entry.setField(FieldName.PAGES,
                        springerJsonEntry.get("startingPage").getAsString() + "--" + springerJsonEntry.get("endPage").getAsString());
            } else {
                entry.setField(FieldName.PAGES, springerJsonEntry.get("startingPage").getAsString());
            }
        }

        // Journal
        if (springerJsonEntry.has("publicationName")) {
            entry.setField(nametype, springerJsonEntry.get("publicationName").getAsString());
        }

        // URL
        if (springerJsonEntry.has("url")) {
            JsonElement urlarray = springerJsonEntry.get("url");
            if (urlarray.isJsonArray()) {
                entry.setField(FieldName.URL, urlarray.getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString());
            } else {
                entry.setField(FieldName.URL, springerJsonEntry.get("url").getAsString());
            }
        }

        // Date
        if (springerJsonEntry.has("publicationDate")) {
            String date = springerJsonEntry.get("publicationDate").getAsString();
            entry.setField(FieldName.DATE, date); // For biblatex
            String[] dateparts = date.split("-");
            entry.setField(FieldName.YEAR, dateparts[0]);
            Optional<Month> month = Month.getMonthByNumber(Integer.parseInt(dateparts[1]));
            month.ifPresent(entry::setMonth);
        }

        // Clean up abstract (often starting with Abstract)
        entry.getField(FieldName.ABSTRACT).ifPresent(abstractContents -> {
            if (abstractContents.startsWith("Abstract")) {
                entry.setField(FieldName.ABSTRACT, abstractContents.substring(8));
            }
        });

        return entry;
    }
}
