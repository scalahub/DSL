/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trap.repl.util;

/**
 *
 * @author Amitabh
 */
public class StringUtil {
 /**
     * From http://www.xinotes.org/notes/note/813/
     */
    static enum ParseState {
	NORMAL,
	ESCAPE,
	UNICODE_ESCAPE
    }
        // convert unicode escapes back to char
    public static String convertUnicodeEscape(String s) {
	char[] out = new char[s.length()];

	ParseState state = ParseState.NORMAL;
	int j = 0, k = 0, unicode = 0;
	char c = ' ';
	for (int i = 0; i < s.length(); i++) {
	    c = s.charAt(i);
	    if (state == ParseState.ESCAPE) {
		if (c == 'u') {
		    state = ParseState.UNICODE_ESCAPE;
		    unicode = 0;
		}
		else { // we don't care about other escapes
		    out[j++] = '\\';
		    out[j++] = c;
		    state = ParseState.NORMAL;
		}
	    }
	    else if (state == ParseState.UNICODE_ESCAPE) {
		if ((c >= '0') && (c <= '9')) {
		    unicode = (unicode << 4) + c - '0';
		}
		else if ((c >= 'a') && (c <= 'f')) {
		    unicode = (unicode << 4) + 10 + c - 'a';
		}
		else if ((c >= 'A') && (c <= 'F')) {
		    unicode = (unicode << 4) + 10 + c - 'A';
		}
		else {
		    throw new IllegalArgumentException("Malformed unicode escape");
		}
		k++;

		if (k == 4) {
		    out[j++] = (char) unicode;
		    k = 0;
		    state = ParseState.NORMAL;
		}
	    }
	    else if (c == '\\') {
		state = ParseState.ESCAPE;
	    }
	    else {
		out[j++] = c;
	    }
	}

	if (state == ParseState.ESCAPE) {
	    out[j++] = c;
	}

	return new String(out, 0, j);
    }
}
