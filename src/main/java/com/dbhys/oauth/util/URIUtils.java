/*
 * copy from com.nimbusds oauth2-oidc-sdk
 *
 * Copyright 2012-2016, Connect2id Ltd and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.dbhys.oauth.util;


import java.net.URI;
import java.net.URISyntaxException;


/**
 * URI operations.
 */
public final class URIUtils {


	/**
	 * Gets the base part (schema, host, port and path) of the specified
	 * URI.
	 *
	 * @param uri The URI. May be {@code null}.
	 *
	 * @return The base part of the URI, {@code null} if the original URI
	 *         is {@code null} or doesn't specify a protocol.
	 */
	public static URI getBaseURI(final URI uri) {

		if (uri == null)
			return null;

		try {
			return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), null, null);

		} catch (URISyntaxException e) {

			return null;
		}
	}
	
	
	/**
	 * Strips the query string from the specified URI.
	 *
	 * @param uri The URI. May be {@code null}.'
	 *
	 * @return The URI with stripped query string, {@code null} if the
	 *         original URI is {@code null} or doesn't specify a protocol.
	 */
	public static URI stripQueryString(final URI uri) {
		
		if (uri == null)
			return null;
		
		try {
			return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), null, uri.getFragment());
			
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	
	/**
	 * Removes the trailing slash ("/") from the specified URI, if present.
	 *
	 * @param uri The URI. May be {@code null}.
	 *
	 * @return The URI with no trailing slash, {@code null} if the original
	 *         URI is {@code null}.
	 */
	public static URI removeTrailingSlash(final URI uri) {
		
		if (uri == null)
			return null;
		
		String uriString = uri.toString();
		
		if (uriString.charAt(uriString.length() - 1 ) == '/') {
			return URI.create(uriString.substring(0, uriString.length() - 1));
		}
		
		return uri;
	}


	/**
	 * Prevents public instantiation.
	 */
	private URIUtils() {}
}
