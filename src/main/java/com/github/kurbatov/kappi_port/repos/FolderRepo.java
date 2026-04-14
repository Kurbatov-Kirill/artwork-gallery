package com.github.kurbatov.kappi_port.repos;

import com.github.kurbatov.kappi_port.domain.Artwork;
import com.github.kurbatov.kappi_port.domain.Folder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FolderRepo extends CrudRepository<Folder, Integer> {
    boolean existsByName(String email);
    Folder getFolderByName(String name);
    Folder getFolderById(int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Folder a WHERE a.id IN :ids")
    void deleteFoldersWithIds(@Param("ids") List<Integer> ids);

    @Query("UPDATE Folder a SET isVisible = NOT isVisible WHERE a.id IN :ids")
    @Modifying
    @Transactional
    void toggleVisibilityFoldersWithIds(@Param("ids") List<Integer> ids);

    @Query("UPDATE Folder a SET isFeatured = NOT isFeatured WHERE a.id IN :ids")
    @Modifying
    @Transactional
    void toggleFeaturedFoldersWithIds(@Param("ids") List<Integer> ids);

    @Query("UPDATE Folder f SET preview = :artId WHERE f.id IN :folderId")
    @Modifying
    @Transactional
    void setAsFolderPreview(@Param("artId") Artwork artId, @Param("folderId")  Integer folderId);

    @Query("SELECT f FROM Folder f WHERE f.isFeatured = true AND f.isVisible = true ORDER BY f.id")
    Iterable<Folder> findFeaturedArtworksByFolderOrdered();

    @Query("SELECT f FROM Folder f WHERE f.isVisible = true AND f.isFeatured = false ORDER BY f.id")
    Iterable<Folder> findVisibleArtworksByFolderOrdered();

    @Query("SELECT f FROM Folder f ORDER BY f.orderNumber")
    Iterable<Folder> findAllFoldersOrdered();
}
