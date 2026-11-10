package com.dcd.storage;

import com.dcd.storage.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testAssetResult() {
        AssetResult asset = new AssetResult();
        asset.setId("ast_100");
        asset.setPublicId("avatars/user.jpg");
        asset.setFolder("avatars");
        asset.setVersion(1);
        asset.setFilename("user.jpg");
        asset.setOriginalFilename("orig-user.jpg");
        asset.setFormat("jpg");
        asset.setMimeType("image/jpeg");
        asset.setResourceType("image");
        asset.setWidth(800);
        asset.setHeight(600);
        asset.setBytes(204800L);
        asset.setEtag("etag-xyz");
        asset.setTags(Arrays.asList("avatar", "profile"));
        asset.setAccess("public");
        asset.setUrl("http://storage.example.com/image/upload/avatars/user.jpg");
        asset.setSecureUrl("https://storage.example.com/image/upload/avatars/user.jpg");
        asset.setRawUrl("https://storage.example.com/file/avatars/user.jpg");

        assertEquals("ast_100", asset.getId());
        assertEquals("avatars/user.jpg", asset.getPublicId());
        assertEquals("avatars", asset.getFolder());
        assertEquals(1, asset.getVersion());
        assertEquals("user.jpg", asset.getFilename());
        assertEquals("orig-user.jpg", asset.getOriginalFilename());
        assertEquals("jpg", asset.getFormat());
        assertEquals("image/jpeg", asset.getMimeType());
        assertEquals("image", asset.getResourceType());
        assertEquals(800, asset.getWidth());
        assertEquals(600, asset.getHeight());
        assertEquals(204800L, asset.getBytes());
        assertEquals("etag-xyz", asset.getEtag());
        assertEquals(2, asset.getTags().size());
        assertEquals("public", asset.getAccess());
        assertEquals("https://storage.example.com/image/upload/avatars/user.jpg", asset.getSecureUrl());
        assertEquals("https://storage.example.com/file/avatars/user.jpg", asset.getRawUrl());
        assertNotNull(asset.toString());
    }

    @Test
    public void testHealthCheckResult() {
        HealthCheckResult result = new HealthCheckResult();
        result.setStatus("ok");
        result.setUptime(86400L);
        result.setVersion("1.0.0");

        assertTrue(result.isHealthy());
        assertEquals("ok", result.getStatus());
        assertEquals(86400L, result.getUptime());
        assertEquals("1.0.0", result.getVersion());

        result.setStatus("HEALTHY");
        assertTrue(result.isHealthy());

        result.setStatus("down");
        assertFalse(result.isHealthy());
    }

    @Test
    public void testPagination() {
        Pagination p = new Pagination();
        p.setPage(2);
        p.setLimit(25);
        p.setTotal(100);
        p.setTotalPages(4);
        p.setHasNext(true);
        p.setHasPrev(true);

        assertEquals(2, p.getPage());
        assertEquals(25, p.getLimit());
        assertEquals(100, p.getTotal());
        assertEquals(4, p.getTotalPages());
        assertTrue(p.isHasNext());
        assertTrue(p.isHasPrev());
        assertNotNull(p.toString());
    }

    @Test
    public void testFileListResult() {
        FileListResult list = new FileListResult();
        list.setSuccess(true);

        AssetResult asset = new AssetResult();
        asset.setPublicId("item.png");
        list.setFiles(Collections.singletonList(asset));

        Pagination p = new Pagination();
        p.setTotal(1);
        list.setPagination(p);

        assertTrue(list.isSuccess());
        assertEquals(1, list.getFiles().size());
        assertEquals("item.png", list.getFiles().get(0).getPublicId());
        assertEquals(1, list.getPagination().getTotal());
        assertNotNull(list.toString());
    }

    @Test
    public void testServerStats() {
        ServerStats stats = new ServerStats();
        stats.setFilesCount(500);
        stats.setTotalStorageBytes(1073741824L);
        stats.setCacheVariantsCount(120);
        stats.setCacheSizeBytes(52428800L);
        stats.setUptimeSeconds(3600L);
        stats.setMemoryUsageMb(64.5);
        stats.setStorageDriver("s3");

        assertEquals(500, stats.getFilesCount());
        assertEquals(1073741824L, stats.getTotalStorageBytes());
        assertEquals(120, stats.getCacheVariantsCount());
        assertEquals(52428800L, stats.getCacheSizeBytes());
        assertEquals(3600L, stats.getUptimeSeconds());
        assertEquals(64.5, stats.getMemoryUsageMb(), 0.001);
        assertEquals("s3", stats.getStorageDriver());
        assertNotNull(stats.toString());
    }

    @Test
    public void testSignedUploadResult() {
        SignedUploadResult res = new SignedUploadResult();
        res.setSuccess(true);
        res.setToken("tok_abc123");
        res.setUploadUrl("https://storage.devclouddesk.com/v1/upload");
        res.setExpiresInSeconds(600);
        res.setFolder("docs");
        res.setMaxBytes(10485760L);
        res.setAccess("private");
        res.setWebhookUrl("https://app.example.com/hooks/upload");

        assertTrue(res.isSuccess());
        assertEquals("tok_abc123", res.getToken());
        assertEquals("https://storage.devclouddesk.com/v1/upload", res.getUploadUrl());
        assertEquals(600, res.getExpiresInSeconds());
        assertEquals("docs", res.getFolder());
        assertEquals(10485760L, res.getMaxBytes());
        assertEquals("private", res.getAccess());
        assertEquals("https://app.example.com/hooks/upload", res.getWebhookUrl());
        assertNotNull(res.toString());
    }
}

