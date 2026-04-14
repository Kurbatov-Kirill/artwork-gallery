package com.github.kurbatov.kappi_port.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int orderNumber;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isVisible;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isFeatured;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Artwork> artworks;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "preview", referencedColumnName = "id")
    private Artwork preview;

    public Folder() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Folder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getFeatured() {
        return isFeatured;
    }

    public void setFeatured(Boolean featured) {
        isFeatured = featured;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }

    public Artwork getPreview() {
        return preview;
    }

    public void setPreview(Artwork preview) {
        this.preview = preview;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
