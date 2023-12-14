package ch.heigvd.amt.services;

import ch.heigvd.amt.entities.Probe;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(ProbeServiceProfile.class)
class ProbeServiceTest {

    @Inject
    ProbeService probeService;

    @Test
    void computeStatus500() {
        var probe = new Probe("https://httpstat.us/500");
        var status = probeService.computeStatus(probe);
        assertEquals("https://httpstat.us/500", status.getProbe().getUrl());
        assertNotNull(status.getTimestamp());
        assertFalse(status.isUp());
        assertTrue(status.getDuration() >= 0);
    }

    @Test
    void computeStatus200() {
        var probe = new Probe("https://httpstat.us/200");
        var status = probeService.computeStatus(probe);
        assertEquals("https://httpstat.us/200", status.getProbe().getUrl());
        assertNotNull(status.getTimestamp());
        assertTrue(status.isUp());
        assertTrue(status.getDuration() >= 0);
    }

    @Test
    void listProbes() {
        var list1 = probeService.listProbes();
        assertEquals(0, list1.size());
        var probe1 = probeService.createProbeIfNotExists(new Probe("https://httpstat.us/201"));
        var probe2 = probeService.createProbeIfNotExists(new Probe("https://httpstat.us/202"));
        var list2 = probeService.listProbes();
        assertEquals(2, list2.size());
        assertEquals(probe1.getUrl(), list2.get(0).getUrl());
        assertEquals(probe2.getUrl(), list2.get(1).getUrl());
    }

    @Test
    void createProbeIfNotExists() {
        var probe1 = probeService.createProbeIfNotExists(new Probe("https://httpstat.us/200"));
        assertEquals("https://httpstat.us/200", probe1.getUrl());
        var probe2 = probeService.createProbeIfNotExists(new Probe("https://httpstat.us/200"));
        assertEquals("https://httpstat.us/200", probe2.getUrl());
        var list1 = probeService.listProbes();
        assertEquals(1, list1.size());
    }

    @Test
    void createInvalidProbes() {
        assertThrows(Exception.class, () -> probeService.createProbeIfNotExists(new Probe(null)));
        assertThrows(Exception.class, () -> probeService.createProbeIfNotExists(new Probe("")));
        assertThrows(Exception.class, () -> probeService.createProbeIfNotExists(new Probe("test")));
    }

    @Test
    void getLastStatus() {
        var probe = probeService.createProbeIfNotExists(new Probe("https://httpstat.us/200"));
        var status1 = probeService.executeProbe(probe);
        var lastStatus1 = probeService.getLastStatus("https://httpstat.us/200");
        assertEquals(status1.getId(), lastStatus1.getId());
        var status2 = probeService.executeProbe(probe);
        var lastStatus2 = probeService.getLastStatus("https://httpstat.us/200");
        assertEquals(status2.getId(), lastStatus2.getId());
        assertTrue(status1.getTimestamp().isBefore(status2.getTimestamp()));
    }

    @Test
    void getStatusList() {
        var probe = probeService.createProbeIfNotExists(new Probe("https://httpstat.us/200"));
        var list1 = probeService.getStatusList(probe.getUrl(), 2);
        assertEquals(2, list1.size());
        for (var status : list1) {
            assertEquals(probe.getUrl(), status.getProbe().getUrl());
            assertFalse(status.isUp());
            assertEquals(Instant.EPOCH, status.getTimestamp());
        }
        probeService.executeProbe(probe);
        probeService.executeProbe(probe);
        var list2 = probeService.getStatusList(probe.getUrl(), 2);
        assertEquals(2, list2.size());
        for (var status : list2) {
            assertEquals(probe.getUrl(), status.getProbe().getUrl());
            assertTrue(status.isUp());
            assertNotEquals(Instant.EPOCH, status.getTimestamp());
        }
    }
}