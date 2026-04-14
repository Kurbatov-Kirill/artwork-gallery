package com.github.kurbatov.kappi_port.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "folder", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folder folder;

    @OneToOne(mappedBy = "preview")
    @JoinColumn(name = "preview", referencedColumnName = "id")
    private Folder preview;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Long orderNumber;

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isVisible;
    private String url;

    public Artwork() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Folder getPreview() {
        return preview;
    }

    public void setPreview(Folder preview) {
        this.preview = preview;
    }

    public Artwork(String name) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
