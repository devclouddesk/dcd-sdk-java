package com.dcd.storage;

import java.util.*;

/**
 * Lightweight, zero-dependency JSON parser and serializer for DevCloudDesk SDK.
 * Enables the Java SDK to operate out-of-the-box on Java 11+ with zero external JAR requirements.
 */
public final class SimpleJson {

    private SimpleJson() {}

    public static Object parse(String json) {
        if (json == null) return null;
        String trimmed = json.trim();
        if (trimmed.isEmpty()) return null;
        int[] index = new int[]{0};
        return parseValue(trimmed, index);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseObject(String json) {
        Object result = parse(json);
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }
        return Collections.emptyMap();
    }

    private static Object parseValue(String s, int[] idx) {
        skipWhitespace(s, idx);
        if (idx[0] >= s.length()) return null;

        char c = s.charAt(idx[0]);
        if (c == '{') return parseObjectInternal(s, idx);
        if (c == '[') return parseArrayInternal(s, idx);
        if (c == '"') return parseString(s, idx);
        if (c == 't' || c == 'T') {
            idx[0] += 4; // true
            return Boolean.TRUE;
        }
        if (c == 'f' || c == 'F') {
            idx[0] += 5; // false
            return Boolean.FALSE;
        }
        if (c == 'n' || c == 'N') {
            idx[0] += 4; // null
            return null;
        }
        return parseNumber(s, idx);
    }

    private static Map<String, Object> parseObjectInternal(String s, int[] idx) {
        Map<String, Object> map = new LinkedHashMap<>();
        idx[0]++; // skip '{'

        while (idx[0] < s.length()) {
            skipWhitespace(s, idx);
            if (idx[0] >= s.length()) break;
            if (s.charAt(idx[0]) == '}') {
                idx[0]++;
                return map;
            }

            if (s.charAt(idx[0]) == ',') {
                idx[0]++;
                skipWhitespace(s, idx);
            }

            if (idx[0] >= s.length() || s.charAt(idx[0]) == '}') {
                if (idx[0] < s.length()) idx[0]++;
                return map;
            }

            String key = parseString(s, idx);
            skipWhitespace(s, idx);

            if (idx[0] < s.length() && s.charAt(idx[0]) == ':') {
                idx[0]++; // skip ':'
            }

            Object val = parseValue(s, idx);
            map.put(key, val);
        }
        return map;
    }

    private static List<Object> parseArrayInternal(String s, int[] idx) {
        List<Object> list = new ArrayList<>();
        idx[0]++; // skip '['

        while (idx[0] < s.length()) {
            skipWhitespace(s, idx);
            if (idx[0] >= s.length()) break;
            if (s.charAt(idx[0]) == ']') {
                idx[0]++;
                return list;
            }

            if (s.charAt(idx[0]) == ',') {
                idx[0]++;
                skipWhitespace(s, idx);
            }

            if (idx[0] >= s.length() || s.charAt(idx[0]) == ']') {
                if (idx[0] < s.length()) idx[0]++;
                return list;
            }

            Object item = parseValue(s, idx);
            list.add(item);
        }
        return list;
    }

    private static String parseString(String s, int[] idx) {
        skipWhitespace(s, idx);
        if (idx[0] >= s.length() || s.charAt(idx[0]) != '"') return "";
        idx[0]++; // skip opening '"'

        StringBuilder sb = new StringBuilder();
        while (idx[0] < s.length()) {
            char c = s.charAt(idx[0]++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\' && idx[0] < s.length()) {
                char esc = s.charAt(idx[0]++);
                switch (esc) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        if (idx[0] + 4 <= s.length()) {
                            String hex = s.substring(idx[0], idx[0] + 4);
                            idx[0] += 4;
                            try {
                                sb.append((char) Integer.parseInt(hex, 16));
                            } catch (NumberFormatException ignored) {}
                        }
                        break;
                    default: sb.append(esc); break;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static Number parseNumber(String s, int[] idx) {
        int start = idx[0];
        boolean isFloating = false;
        while (idx[0] < s.length()) {
            char c = s.charAt(idx[0]);
            if (c == '.' || c == 'e' || c == 'E') {
                isFloating = true;
            } else if (c != '-' && c != '+' && (c < '0' || c > '9')) {
                break;
            }
            idx[0]++;
        }
        String numStr = s.substring(start, idx[0]);
        try {
            if (isFloating) {
                return Double.parseDouble(numStr);
            } else {
                long val = Long.parseLong(numStr);
                if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
                    return (int) val;
                }
                return val;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void skipWhitespace(String s, int[] idx) {
        while (idx[0] < s.length() && Character.isWhitespace(s.charAt(idx[0]))) {
            idx[0]++;
        }
    }

    public static String serialize(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) return "\"" + escapeString((String) obj) + "\"";
        if (obj instanceof Number || obj instanceof Boolean) return String.valueOf(obj);
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(escapeString(String.valueOf(entry.getKey()))).append("\":");
                sb.append(serialize(entry.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : col) {
                if (!first) sb.append(",");
                sb.append(serialize(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + escapeString(String.valueOf(obj)) + "\"";
    }

    private static String escapeString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

