package com.example.demo.rest;

import com.example.demo.service.PdfAnalyzer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Api
@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldEndpoint {

    private Logger logger = Logger.getLogger(this.getClass());

    @GET
    @Path("/now")
    @ApiOperation(value = "Get the current time",
            notes = "Returns the time as a string",
            response = String.class
    )
    public String getNow() {
        return String.format("{\"value\" : \"The time is %s\"}", LocalDateTime.now());
    }

    @GET
    @Path("/analyze")
    public List<String> getList() {

        PdfAnalyzer pdfAnalyzer = new PdfAnalyzer();

        try {
            return pdfAnalyzer.analyze();
        } catch (Exception e) {
            logger.error(e);
            return Collections.emptyList();
        }
    }
}