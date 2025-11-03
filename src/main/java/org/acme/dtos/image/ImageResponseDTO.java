package org.acme.dtos.image;

import org.acme.models.Image;

public record ImageResponseDTO(
    Long id,
    String url,
    String altText
) {
    public static ImageResponseDTO valueOf(Image image) {
        if (image == null) return null;
        return new ImageResponseDTO(image.getId(), image.getUrl(), image.getAltText());
    }
}
