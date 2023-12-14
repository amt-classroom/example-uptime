package ch.heigvd.amt.services;

import ch.heigvd.amt.entities.Probe;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.apache.groovy.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class ProbeServiceProfile implements QuarkusTestProfile {

    public ProbeServiceProfile() {
        super();
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Maps.of(
                "quarkus.amqp.devservices.enabled","false",
                "quarkus.arc.exclude-types", "ch.heigvd.amt.messaging.*");
    }

}