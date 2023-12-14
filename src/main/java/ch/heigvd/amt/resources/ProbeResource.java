package ch.heigvd.amt.resources;

import ch.heigvd.amt.entities.Probe;
import ch.heigvd.amt.services.ProbeService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class ProbeResource {

    @Inject
    ProbeService probeService;

    @Inject
    Template indexPage;

    @Inject
    Template probePage;

    @Inject
    Template statusPage;

    @Inject
    Template statusContent;

    @Inject
    Template registerPage;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return indexPage.instance();
    }

    @Blocking
    @GET
    @Path("/probes")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance list() {
        return probePage.data("probeList", probeService.listProbes());
    }

    @Blocking
    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance status(@QueryParam("url") String url) {
        return statusPage
                .data("probe", probeService.createProbeIfNotExists(new Probe(url)))
                .data("lastStatus", probeService.getLastStatus(url))
                .data("statusList", probeService.getStatusList(url, 60));
    }

    @Blocking
    @GET
    @Path("/status/content")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance statusContent(@QueryParam("url") String url) {
        return statusContent
                .data("probe", probeService.createProbeIfNotExists(new Probe(url)))
                .data("lastStatus", probeService.getLastStatus(url))
                .data("statusList", probeService.getStatusList(url, 60));
    }

    @Blocking
    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance register() {
        return registerPage.instance();
    }

}
