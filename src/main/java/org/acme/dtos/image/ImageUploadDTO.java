package org.acme.dtos.image;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.MULTIPART_FORM_DATA)
public record ImageUploadDTO(
    @RestForm("file") 
    @NotNull(message = "Arquivo de imagem é obrigatório.")
    FileUpload file
) {}
