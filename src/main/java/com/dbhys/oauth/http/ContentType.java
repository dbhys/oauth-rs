/*
 * oauth2-oidc-sdk
 *
 * Copyright 2020, Connect2id Ltd and contributors.
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

package com.dbhys.oauth.http;


import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.*;


/**
 * Content (media) type.
 *
 * <p>To create a new content type {@code application/json} without character
 * set parameter:
 *
 * <pre>
 * ContentType ct = new ContentType("application", "json");
 *
 * // Prints out "application/json"
 * System.out.println(ct.toString());
 * </pre>
 *
 * <p>With a character set parameter {@code application/json; charset=UTF-8}:
 *
 * <pre>
 * ContentType ct = new ContentType("application", "json", new ContentType.Parameter("charset", "UTF-8"));
 *
 * // Prints out "application/json; charset=UTF-8"
 * System.out.println(ct.toString());
 * </pre>
 *
 * <p>To parse a content type:
 *
 * <pre>
 * try {
 *         ContentType.parse("application/json; charset=UTF-8");
 * } catch (java.text.ParseException e) {
 *         System.err.println(e.getMessage());
 * }
 * </pre>
 *
 * <p>See RFC 2045, section 5.1.
 *
 * @author vd
 */
public final class ContentType {
	
	
	/**
	 * Optional content type parameter, for example {@code charset=UTF-8}.
	 */
	public static final class Parameter {
		
		
		/**
		 * A {@code charset=UTF-8} parameter.
		 */
		public static final Parameter CHARSET_UTF_8 = new Parameter("charset", "UTF-8");
		
		
		/**
		 * The parameter name.
		 */
		private final String name;
		
		
		/**
		 * The parameter value.
		 */
		private final String value;
		
		
		/**
		 * Creates a new content type parameter.
		 *
		 * @param name  The name. Must not be {@code null} or empty.
		 * @param value The value. Must not be {@code null} or empty.
		 */
		public Parameter(final String name, final String value) {
			
			if (name == null || name.trim().isEmpty()) {
				throw new IllegalArgumentException("The parameter name must be specified");
			}
			this.name = name;
			
			if (value == null || value.trim().isEmpty()) {
				throw new IllegalArgumentException("The parameter value must be specified");
			}
			this.value = value;
		}
		
		
		/**
		 * Returns the parameter name.
		 *
		 * @return The name.
		 */
		public String getName() {
			return name;
		}
		
		
		/**
		 * Returns the parameter value.
		 *
		 * @return The value.
		 */
		public String getValue() {
			return value;
		}
		
		
		@Override
		public String toString() {
			return name + "=" + value;
		}
		
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Parameter)) return false;
			Parameter parameter = (Parameter) o;
			return getName().equalsIgnoreCase(parameter.getName()) &&
				getValue().equalsIgnoreCase(parameter.getValue());
		}
		
		
		@Override
		public int hashCode() {
			return Objects.hash(getName().toLowerCase(), getValue().toLowerCase());
		}
	}
	
	
	/**
	 * Content type {@code application/json; charset=UTF-8}.
	 */
	public static final ContentType APPLICATION_JSON = new ContentType("application", "json", Parameter.CHARSET_UTF_8);
	
	
	/**
	 * Content type {@code application/jose; charset=UTF-8}.
	 */
	public static final ContentType APPLICATION_JOSE = new ContentType("application", "jose", Parameter.CHARSET_UTF_8);
	
	
	/**
	 * Content type {@code application/jwt; charset=UTF-8}.
	 */
	public static final ContentType APPLICATION_JWT = new ContentType("application", "jwt", Parameter.CHARSET_UTF_8);
	
	
	/**
	 * Content type {@code application/x-www-form-urlencoded; charset=UTF-8}.
	 */
	public static final ContentType APPLICATION_URLENCODED = new ContentType("application", "x-www-form-urlencoded", Parameter.CHARSET_UTF_8);
	
	
	/**
	 * Content type {@code text/plain; charset=UTF-8}.
	 */
	public static final ContentType TEXT_PLAIN = new ContentType("text", "plain", Parameter.CHARSET_UTF_8);
	
	
	/**
	 * The base type.
	 */
	private final String baseType;
	
	
	/**
	 * The sub type.
	 */
	private final String subType;
	
	
	/**
	 * The optional parameters.
	 */
	private final List<Parameter> params;
	
	
	/**
	 * Creates a new content type.
	 *
	 * @param baseType The type. E.g. "application" from
	 *                 "application/json".Must not be {@code null} or
	 *                 empty.
	 * @param subType  The subtype. E.g. "json" from "application/json".
	 *                 Must not be {@code null} or empty.
	 * @param param    Optional parameters.
	 */
	public ContentType(final String baseType, final String subType, final Parameter ... param) {
		
		if (baseType == null || baseType.trim().isEmpty()) {
			throw new IllegalArgumentException("The base type must be specified");
		}
		this.baseType = baseType;
		
		if (subType == null || subType.trim().isEmpty()) {
			throw new IllegalArgumentException("The subtype must be specified");
		}
		this.subType = subType;
		
		
		if (param != null && param.length > 0) {
			params = Collections.unmodifiableList(Arrays.asList(param));
		} else {
			params = Collections.emptyList();
		}
	}
	
	
	/**
	 * Creates a new content type with the specified character set.
	 *
	 * @param baseType The base type. E.g. "application" from
	 *                 "application/json".Must not be {@code null} or
	 *                 empty.
	 * @param subType  The subtype. E.g. "json" from "application/json".
	 *                 Must not be {@code null} or empty.
	 * @param charset  The character set to use for the {@code charset}
	 *                 parameter. Must not be {@code null}.
	 */
	public ContentType(final String baseType, final String subType, final Charset charset) {
		
		this(baseType, subType, new Parameter("charset", charset.toString()));
	}
	
	
	/**
	 * Returns the base type. E.g. "application" from "application/json".
	 *
	 * @return The base type.
	 */
	public String getBaseType() {
		return baseType;
	}
	
	
	/**
	 * Returns the subtype. E.g. "json" from "application/json".
	 *
	 * @return The subtype.
	 */
	public String getSubType() {
		return subType;
	}
	
	
	/**
	 * Returns the type. E.g. "application/json".
	 *
	 * @return The type, any optional parameters are omitted.
	 */
	public String getType() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseType());
		sb.append("/");
		sb.append(getSubType());
		return sb.toString();
	}
	
	
	/**
	 * Returns the optional parameters.
	 *
	 * @return The parameters, as unmodifiable list, empty list if none.
	 */
	public List<Parameter> getParameters() {
		return params;
	}
	
	
	/**
	 * Returns {@code true} if the types and subtypes match. The
	 * parameters, if any, are ignored.
	 *
	 * @param other The other content type, {@code null} if not specified.
	 *
	 * @return {@code true} if the types and subtypes match, else
	 *         {@code false}.
	 */
	public boolean matches(final ContentType other) {
		
		return other != null
			&& getBaseType().equalsIgnoreCase(other.getBaseType())
			&& getSubType().equalsIgnoreCase(other.getSubType());
	}
	
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder(getType());
		
		if (! getParameters().isEmpty()) {
			for (Parameter p: getParameters()) {
				sb.append("; ");
				sb.append(p.getName());
				sb.append("=");
				sb.append(p.getValue());
			}
		}
		
		return sb.toString();
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContentType)) return false;
		ContentType that = (ContentType) o;
		return getBaseType().equalsIgnoreCase(that.getBaseType()) &&
			getSubType().equalsIgnoreCase(that.getSubType()) &&
			params.equals(that.params);
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getBaseType().toLowerCase(), getSubType().toLowerCase(), params);
	}
	
	
	/**
	 * Parses a content type from the specified string.
	 *
	 * @param s The string to parse.
	 *
	 * @return The content type.
	 *
	 * @throws ParseException If parsing failed or the string is
	 *                        {@code null} or empty.
	 */
	public static ContentType parse(final String s)
		throws ParseException {
		
		if (s == null || s.trim().isEmpty()) {
			throw new ParseException("Null or empty content type string", 0);
		}
		
		StringTokenizer st = new StringTokenizer(s, "/");
		
		if (! st.hasMoreTokens()) {
			throw new ParseException("Invalid content type string", 0);
		}
		
		String type = st.nextToken().trim();
		
		if (type.trim().isEmpty()) {
			throw new ParseException("Invalid content type string", 0);
		}
		
		if (! st.hasMoreTokens()) {
			throw new ParseException("Invalid content type string", 0);
		}
		
		String subtypeWithOptParams = st.nextToken().trim();
		
		st = new StringTokenizer(subtypeWithOptParams, ";");
		
		if (! st.hasMoreTokens()) {
			// No params
			return new ContentType(type, subtypeWithOptParams.trim());
		}
		
		String subtype = st.nextToken().trim();
		
		if (! st.hasMoreTokens()) {
			// No params
			return new ContentType(type, subtype);
		}
		
		List<Parameter> params = new LinkedList<>();
		
		while (st.hasMoreTokens()) {
			
			String paramToken = st.nextToken().trim();
			
			StringTokenizer paramTokenizer = new StringTokenizer(paramToken, "=");
			
			if (! paramTokenizer.hasMoreTokens()) {
				throw new ParseException("Invalid parameter", 0);
			}
			
			String paramName = paramTokenizer.nextToken().trim();
			
			if (! paramTokenizer.hasMoreTokens()) {
				throw new ParseException("Invalid parameter", 0);
			}
			
			String paramValue = paramTokenizer.nextToken().trim();
			
			try {
				params.add(new Parameter(paramName, paramValue));
			} catch (IllegalArgumentException e) {
				throw new ParseException("Invalid parameter: " + e.getMessage(), 0);
			}
		}
		
		return new ContentType(type, subtype, params.toArray(new Parameter[0]));
	}
}
