package com.skycloud.management.portal.common.utils;

public class HtmlEncodeHelper {
	public static String htmEncode(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int j = s.length();
		for (int i = 0; i < j; i++) {
			char c = s.charAt(i);
			switch (c) {
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && s.charAt(i + 1) == 10) {
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && s.charAt(i + 1) == ' ') {
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}

	public static final String escapeHTML(String s) {
		StringBuffer sb = new StringBuffer();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case 'à':
				sb.append("&agrave;");
				break;
			// case '&Agrave; ': sb.append( "&Agrave; ");break;
			// case '&acirc; ': sb.append( "&acirc; ");break;
			// case '&auml; ': sb.append( "&auml; ");break;
			// case '&Auml; ': sb.append( "&auml; ");break;
			// case '&Acirc; ': sb.append( "&Acirc; ");break;
			// case '&aring; ': sb.append( "&aring; ");break;
			// case '&Aring; ': sb.append( "&Aring; ");break;
			// case '&aelig; ': sb.append( "&aelig; ");break;
			// case '&AElig; ': sb.append( "&AElig; ");break;
			// case '&ccedil; ': sb.append( "&ccedil; ");break;
			// case '&Ccedil; ': sb.append( "&Ccedil; ");break;
			case 'é':
				sb.append("&eacute;");
				break;
			// case '&Eacute; ': sb.append( "&Eacute; ");break;
			case 'è':
				sb.append("&egrave;");
				break;
			// case '&Egrave; ': sb.append( "&Egrave; ");break;
			case 'ê':
				sb.append("&ecirc;");
				break;
			// case '&Ecirc; ': sb.append( "&Ecirc; ");break;
			// case '&euml; ': sb.append( "&euml; ");break;
			// case '&Euml; ': sb.append( "&Euml; ");break;
			// case '&iuml; ': sb.append( "&iuml; ");break;
			// case '&Iuml; ': sb.append( "&Iuml; ");break;
			// case '&ocirc; ': sb.append( "&ocirc; ");break;
			// case '&Ocirc; ': sb.append( "&Ocirc; ");break;
			// case '&ouml; ': sb.append( "&ouml; ");break;
			// case '&Ouml; ': sb.append( "&Ouml; ");break;
			// case '&oslash; ': sb.append( "&oslash; ");break;
			// case '&Oslash; ': sb.append( "&Oslash; ");break;
			// case '&szlig; ': sb.append( "&szlig; ");break;
			case 'ù':
				sb.append("&ugrave; ");
				break;
			// case '&Ugrave; ': sb.append( "&Ugrave; ");break;
			// case '&ucirc; ': sb.append( "&ucirc; ");break;
			// case '&Ucirc; ': sb.append( "&Ucirc; ");break;
			case 'ü':
				sb.append("&uuml; ");
				break;
			// case '&Uuml; ': sb.append( "&Uuml; ");break;
			// case '&reg; ': sb.append( "&reg; ");break;
			// case '&copy; ': sb.append( "&copy; ");break;
			// case '&#8364; ': sb.append( "&euro; "); break;
			// be carefull with this one (non-breaking whitee space)
			case ' ':
				sb.append("&nbsp; ");
				break;

			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}
}
