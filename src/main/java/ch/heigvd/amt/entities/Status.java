package ch.heigvd.amt.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

@Entity
public class Status {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Probe probe;

    private Instant timestamp;

    private boolean up;

    private int duration;

    public Status() {}

    public Status(Long id, Probe probe, Instant timestamp, boolean up, int duration) {
        this.id = id;
        this.probe = probe;
        this.timestamp = timestamp;
        this.up = up;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Probe getProbe() {
        return probe;
    }

    public void setProbe(Probe probe) {
        this.probe = probe;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int responseTime) {
        this.duration = responseTime;
    }
}
