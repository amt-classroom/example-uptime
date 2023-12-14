package ch.heigvd.amt.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

@Entity
public class Probe implements Serializable {

    @Id
    @NotNull
    @NotBlank
    @URL
    private String url;

    public Probe() {
    }

    public Probe(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
