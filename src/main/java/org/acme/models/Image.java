package org.acme.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image extends DefaultEntity {

    @Column(name="url", length=1000, nullable = false)
    private String url;

    @Column(name="alt_text", length=150, nullable = true)
    private String altText;

    @Column(name="object_name", length=1000, nullable = false)
    private String objectName;

    @ManyToOne
    @JoinColumn(name="gpu_id", nullable = false)
    private Gpu gpu;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Gpu getGpu() {
        return gpu;
    }

    public void setGpu(Gpu gpu) {
        this.gpu = gpu;
    }

}
