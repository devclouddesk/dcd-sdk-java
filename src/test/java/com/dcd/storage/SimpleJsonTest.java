package com.dcd.storage;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleJsonTest {

    @Test
    public void testParseSimpleObject() {
        String json = "{\"name\":\"John Doe\",\"age\":30,\"active\":true,\"balance\":123.45}";
        Map<String, Object> map = SimpleJson.parseObject(json);

        assertNotNull(map);
        assertEquals("John Doe", map.get("name"));
        assertEquals(30, map.get("age"));
        assertEquals(Boolean.TRUE, map.get("active"));
        assertEquals(123.45, (Double) map.get("balance"), 0.001);
    }

    @Test
    public void testParseNestedObject() {
        String json = "{\"user\":{\"id\":101,\"profile\":{\"role\":\"admin\"}}}";
        Map<String, Object> map = SimpleJson.parseObject(json);

        assertNotNull(map);
        assertTrue(map.get("user") instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) map.get("user");
        assertEquals(101, user.get("id"));

        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) user.get("profile");
        assertEquals("admin", profile.get("role"));
    }

    @Test
    public void testParseArrays() {
        String json = "{\"items\":[\"apple\",\"banana\",\"cherry\"],\"counts\":[1,2,3]}";
        Map<String, Object> map = SimpleJson.parseObject(json);

        assertNotNull(map);
        assertTrue(map.get("items") instanceof List);
        @SuppressWarnings("unchecked")
        List<Object> items = (List<Object>) map.get("items");
        assertEquals(3, items.size());
        assertEquals("apple", items.get(0));
        assertEquals("banana", items.get(1));
    }

    @Test
    public void testParseEmptyAndNullValues() {
        String json = "{\"empty\":null,\"str\":\"\"}";
        Map<String, Object> map = SimpleJson.parseObject(json);

        assertNotNull(map);
        assertNull(map.get("empty"));
        assertEquals("", map.get("str"));

        Map<String, Object> emptyMap = SimpleJson.parseObject("{}");
        assertNotNull(emptyMap);
        assertTrue(emptyMap.isEmpty());
    }

    @Test
    public void testSerializeMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("title", "Test");
        map.put("count", 5);
        map.put("tags", Arrays.asList("a", "b"));

        String json = SimpleJson.serialize(map);
        assertEquals("{\"title\":\"Test\",\"count\":5,\"tags\":[\"a\",\"b\"]}", json);
    }

    @Test
    public void testSerializeEscapedStrings() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("text", "Hello \"World\"\nLine 2\tTabbed\\Backslash");

        String json = SimpleJson.serialize(map);
        assertTrue(json.contains("\\\"World\\\""));
        assertTrue(json.contains("\\n"));
        assertTrue(json.contains("\\t"));
        assertTrue(json.contains("\\\\"));
    }
}

