package com.dbhys.oauth.util;

import com.dbhys.oauth.ParseException;
import net.minidev.json.parser.JSONParser;


/**
 * copy from com.nimbusds oauth2-oidc-sdk
 * JSON helper methods.
 */
final class JSONUtils {


    /**
     * Parses a JSON value.
     *
     * @param s The JSON string to parse. Must not be {@code null}.
     *
     * @return The JSON value.
     *
     * @throws ParseException If the string cannot be parsed to a JSON
     *                        value.
     */
    public static Object parseJSON(final String s)
            throws ParseException {

        try {
            return new JSONParser(JSONParser.USE_HI_PRECISION_FLOAT | JSONParser.ACCEPT_TAILLING_SPACE).parse(s);

        } catch (net.minidev.json.parser.ParseException e) {

            throw new ParseException("Invalid JSON: " + e.getMessage(), e);
        }
    }
}

