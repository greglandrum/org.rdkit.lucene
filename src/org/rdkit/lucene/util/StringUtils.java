/*
 * Copyright (C)2014, Novartis Institutes for BioMedical Research Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 * 
 * - Neither the name of Novartis Institutes for BioMedical Research Inc.
 *   nor the names of its contributors may be used to endorse or promote
 *   products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rdkit.lucene.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This utility class contains convenience methods to work with strings.
 * 
 * @author Manuel Schwarze
 */
public final class StringUtils {

	//
	// Static Methods
	//

	/**
	 * Generates a friendly and easily readable description based on the passed in description.
	 * 
	 * @param description A description of something. Can be null.
	 * 
	 * @return More friendly description. Is empty when null was passed in.
	 */
	public static String generateFriendlyDescription(final String description) {
		return StringUtils.generateFriendlyDescription(description, false);
	}

	/**
	 * Generates a friendly and easily readable description based on the passed in description.
	 * It may add a dot at the end, if no punctuation is set.
	 * 
	 * @param description A description of something. Can be null.
	 * @param bAddDot Set to true to force adding punctuation at the end (dot).
	 * 
	 * @return More friendly description. Is empty when null was passed in.
	 */
	public static String generateFriendlyDescription(final String description,
			final boolean bAddDot) {
		String strRet = "";

		if (description != null) {
			strRet = description.trim();

			// Don't touch, if length is <= 2 or first "word" is only 1..2
			// characters long (usually a variable like x, y, z)
			if (strRet.length() > 2 && Character.isLowerCase(strRet.charAt(0))
					&& strRet.charAt(1) != ' ' && strRet.charAt(2) != ' ') {
				strRet = Character.toUpperCase(strRet.charAt(0))
						+ strRet.substring(1);
			}

			// Adds a dot, if not ended with a sentence end sign character (.,
			// !, ?)
			if (bAddDot && strRet.length() > 1) {
				final char chEnd = strRet.charAt(strRet.length() - 1);
				if (chEnd != '.' && chEnd != '!' && chEnd != '?') {
					strRet += ".";
				}
			}
		}

		return strRet;
	}

	/**
	 * Splits up a version string in its parts. As delimiters it uses . and , characters.
	 * 
	 * @param strVersion A version string.
	 * 
	 * @return A list of version string parts or {1, 0}, of null was passed in.
	 */
	public static List<Integer> tokenizeVersion(final String strVersion) {
		final List<Integer> listVersion = new ArrayList<Integer>();

		if (strVersion == null) {
			listVersion.add(1);
			listVersion.add(0);
		} else {
			final StringTokenizer st = new StringTokenizer(strVersion, ".,", false);
			for (int i = 0; i < st.countTokens(); i++) {
				try {
					final int iVersion = Integer.parseInt(st.nextToken());
					listVersion.add(iVersion);
				} catch (final NumberFormatException excNumber) {
					// Ignore
				}
			}
		}

		return listVersion;
	}

	/**
	 * Truncates a long string, which cannot be shown in full to the user.
	 * At the end of the truncated string it will tell the total length
	 * of the string, if maximum size allows for it.
	 * 
	 * @param str String to truncate, if necessary. Can be null.
	 * @param iMaxLength Maximal length incl. additional information to be added
	 * 		when truncating is necessary.
	 * 
	 * @return A string that is not longer than the maximal length that was
	 * 		specified. Returns null, if null was passed in as string.
	 */
	public static String truncateString(final String str, final int iMaxLength) {
		String strRet = null;

		if (str != null) {
			if (str.length() <= iMaxLength) {
				strRet = str;
			} else if (iMaxLength > 50) {
				final String strAdd = "... (total length of " + str.length()
						+ " characters)";
				strRet = str.substring(0, iMaxLength - strAdd.length())
						+ strAdd;
			} else if (iMaxLength > 3) {
				strRet = str.substring(0, iMaxLength - 3) + "...";
			} else if (iMaxLength >= 0) {
				strRet = "...".substring(0, iMaxLength);
			} else {
				strRet = "";
			}
		}

		return strRet;
	}

	/**
	 * Determines, if the specified string would be empty (size = 0) after
	 * removing leading and trailing whitespaces (trimming).
	 * 
	 * @param str String to check. Can be null.
	 * 
	 * @return True, if string is logically empty. False otherwise.
	 * 		Returns true, if null was passed in.
	 */
	public static boolean isEmptyAfterTrimming(final String str) {
		boolean bEmpty = true;

		if (str != null) {
			final int len = str.length();
			for (int i = 0; i < len; i++) {
				if (!Character.isWhitespace(str.charAt(i))) {
					bEmpty = false;
					break;
				}
			}
		}

		return bEmpty;
	}

	/**
	 * Removes all occurrences of HTML comments from this string.
	 * 
	 * @param str String to manipulate. Can be null.
	 * 
	 * @return String without parts marked as HTML comments.
	 */
	public static String removeHtmlComments(final String str) {
		return removeAreas(str, "<!--", "-->");
	}

	/**
	 * Removes all tag structures from the specified string. A tag here is
	 * defines as an arbitrary string within < and > signs.
	 * 
	 * @param str A string with tag structures. Can be null.
	 * 
	 * @return The passed in string without the tag structures or null, if null
	 * 		was passed in.
	 */
	public static String removeTags(final String str) {
		return removeAreas(str, "<", ">");
	}

	/**
	 * Removes all areas from the string, that start and end with the specified start and end strings.
	 * 
	 * @param str String to manipulate. Can be null.
	 * @param start Area start string. Must not be null.
	 * @param end Area end string. Must not be null.
	 * 
	 * @return String without parts surrounded by start and end string. Returns null, if
	 * 		null was passed in as string.
	 */
	private static String removeAreas(final String str, final String start, final String end) {
		final StringBuilder sbRet = new StringBuilder();

		if (str != null) {
			int iOffset = 0;
			int iStart = str.indexOf(start, iOffset);
			int iEnd = -1;

			while (iStart != -1) {
				if (iOffset == iStart && sbRet.length() > 0 && !Character.isWhitespace(sbRet.charAt(sbRet.length() - 1))) {
					sbRet.append(' ');
				}

				sbRet.append(str.substring(iOffset, iStart));

				iEnd = str.indexOf(end, iStart + start.length());
				if (iEnd != -1) {
					iOffset = iEnd + end.length();
					iStart = str.indexOf(start, iOffset);
				}
				else {
					iOffset = str.length();
					iStart = -1;
				}
			}

			sbRet.append(str.substring(iOffset));
		}

		return (str == null ? null : sbRet.toString());
	}

	/**
	 * Sorts the specified string list. O(N) is n log(n).
	 *
	 * @param  list String list containing the elements to be sorted. Can be <code>null</code>.
	 *
	 * @return Sorted string list or <code>null</code> if <code>null</code> was passed in.
	 */
	public static List<String> sort(final List<String> list) {
		List<String> listSorted = null;

		if (list != null) {
			listSorted = new ArrayList<String>();
			for (final String str : list) {
				listSorted.add(getIndexForSortedInsert(listSorted, str), str);
			}
		}

		return listSorted;
	}

	/**
	 * Sorts the specified string list. O(N) is n log(n).
	 * 
	 * @param set String set containing the elements to be sorted. Can be
	 *            <code>null</code>.
	 * 
	 * @return Sorted string list or <code>null</code> if <code>null</code> was
	 *         passed in.
	 */
	public static List<String> sort(final Set<String> set) {
		List<String> listSorted = null;

		if (set != null) {
			listSorted = new ArrayList<String>();
			for (final String str : set){
				listSorted.add(getIndexForSortedInsert(listSorted, str), str);
			}
		}

		return listSorted;
	}

	//
	// Private Methods
	//

	/**
	 * Determines for a sorted list and an object, what index the string
	 * could be inserted. Note: This method just returns the index, but does not
	 * insert the string.
	 * 
	 * @param listSorted
	 *            Sorted string list. Can be <code>null</code>.
	 * @param item
	 *            String to be inserted. Can be <code>null</code>.
	 * 
	 * @return Index for string insertion.
	 */
	private static int getIndexForSortedInsert(final List<String> listSorted, final String item) {
		if (listSorted == null || item == null) {
			return 0;
		}

		int low = 0;
		int high = listSorted.size() - 1;

		while (low <= high) {
			final int mid = (low + high) >>> 1;
			final String midVal = listSorted.get(mid);
			final int cmp = midVal.compareToIgnoreCase(item);

			if (cmp < 0) {
				low = mid + 1;
			}
			else if (cmp > 0) {
				high = mid - 1;
			}
			else {
				return mid; // key found
			}
		}

		return low; // key not found.
	}

	//
	// Constructor
	//

	/**
	 * This constructor serves only the purpose to avoid instantiation of this class.
	 */
	private StringUtils() {
		// To avoid instantiation of this class.
	}
}
