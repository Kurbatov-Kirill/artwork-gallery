package com.github.kurbatov.kappi_port.repos;

import com.github.kurbatov.kappi_port.domain.Artwork;
import com.github.kurbatov.kappi_port.domain.Folder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArtworkRepo extends CrudRepository<Artwork, Integer> {
    @Query("SELECT f,a FROM Folder f LEFT JOIN f.id a WHERE f.id = a")
    Iterable<Artwork> findArtworksByFolder(Folder folder);

    @Query("SELECT a FROM Artwork a WHERE a.folder = :folder ORDER BY a.orderNumber")
    Iterable<Artwork> findArtworksByFolderOrdered(Folder folder);

    @Query("SELECT a FROM Artwork a WHERE a.folder = :folder ORDER BY a.id")
    Iterable<Artwork> findArtworksByFolderOrderedById(Folder folder);
    Artwork findArtworkById(Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Artwork a WHERE a.id IN :ids")
    void deleteArtworksWithIds(@Param("ids") List<Integer> ids);

    @Query("UPDATE Artwork a SET isVisible = NOT isVisible WHERE a.id IN :ids")
    @Modifying
    @Transactional
    void toggleVisibilityArtworksWithIds(@Param("ids") List<Integer> ids);
}
