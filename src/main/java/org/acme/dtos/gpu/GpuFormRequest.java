package org.acme.dtos.gpu;
import java.util.List;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;

public record GpuFormRequest(

        @RestForm("data")
        @Valid
        @PartType(MediaType.APPLICATION_JSON)
        GpuRequestDTO data,

        @RestForm("images")
        List<FileUpload> images

) {}
