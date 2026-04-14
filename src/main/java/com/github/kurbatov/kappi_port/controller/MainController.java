package com.github.kurbatov.kappi_port.controller;

import com.github.kurbatov.kappi_port.domain.Artwork;
import com.github.kurbatov.kappi_port.domain.Folder;
import com.github.kurbatov.kappi_port.domain.Role;
import com.github.kurbatov.kappi_port.domain.User;
import com.github.kurbatov.kappi_port.repos.ArtworkRepo;
import com.github.kurbatov.kappi_port.repos.FolderRepo;
import com.github.kurbatov.kappi_port.repos.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private FolderRepo folderRepo;

    @Autowired
    private ArtworkRepo artworkRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/greeting")
    public String greeting(Model model, HttpServletRequest request) {
        model.addAttribute("currentUrl", request.getRequestURI());

        Iterable<Folder> folders = folderRepo.findAllFoldersOrdered();
        model.addAttribute("folders", folders);

        Iterable<Artwork> artworks = artworkRepo.findAll();
        model.addAttribute("artworks", artworks);
        return "greeting";
    }

    @GetMapping("/greeting/{folder}")
    public String greetingFolder(@PathVariable int folder, Model model, HttpServletRequest request) {
        model.addAttribute("currentUrl", request.getRequestURI());

        Folder currentFolder = folderRepo.getFolderById(folder);
        model.addAttribute("folder", currentFolder);

        Iterable<Artwork> artworks = artworkRepo.findArtworksByFolderOrdered(currentFolder);
        model.addAttribute("artworks", artworks);
        return "admFolder";
    }

    @GetMapping("/greeting/{folder}/artwork/{artwork}")
    public String greetingArtwork(@PathVariable Artwork artwork, @PathVariable int folder, Model model, HttpServletRequest request) {
        model.addAttribute("currentUrl", request.getRequestURI());

        model.addAttribute("folder", folder);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);

        model.addAttribute("artwork", artwork);
        return "admArtwork";
    }

    @GetMapping
    public String index(Model model) {
        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);

        Iterable<Artwork> artworks = artworkRepo.findAll();
        model.addAttribute("artworks", artworks);
        return "index";
    }

    @PostMapping("/greeting") public String add(@RequestParam String name, @RequestParam("file") MultipartFile[] files, Model model) {
        String dirUrl = "src/main/resources/static";
        Folder currentFolder;
        if(!folderRepo.existsByName(name)) {
            Iterable<Folder> folders = folderRepo.findAllFoldersOrdered();

            Folder folder = new Folder(name);
            folder.setVisible(false);
            folder.setFeatured(false);

            if (folders != null && folders.iterator().hasNext()) {
                folder.setOrderNumber((int) (folderRepo.count() + 1));
            } else {
                folder.setOrderNumber(1);
            }

            currentFolder = folderRepo.save(folder);
        } else {
            currentFolder = folderRepo.getFolderByName(name);
        }
        long x = 0;

        List<Artwork> artworkList = new ArrayList<>();
        String artUrl;
        for (MultipartFile file: files) {
            x++;

            String fileName = file.getOriginalFilename();
            String fileId = UUID.randomUUID().toString();
            artUrl = "/images/artworks/" + fileId + "." + FilenameUtils.getExtension(fileName);

            Artwork artwork = new Artwork();
            artwork.setName(name + "_" + x);
            artwork.setOrderNumber(x);
            artwork.setUrl(artUrl);
            artwork.setFolder(currentFolder);
            artwork.setVisible(true);

            File file1 = new File(dirUrl + artUrl);

            try {
                file.transferTo(file1.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            artworkList.add(artwork);
        }

        //

        artworkRepo.saveAll(artworkList);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);

        Iterable<Artwork> artworks = artworkRepo.findAll();
        System.out.println(currentFolder.getId());
        model.addAttribute("artworks", artworks);
        return "redirect:/greeting";
    }

    @PostMapping("/greeting/addFromFolder") public String addFromFolder(@RequestParam("returnUrl") String returnUrl, @RequestParam String name, @RequestParam("file") MultipartFile[] files, Model model) {
        String dirUrl = "src/main/resources/static";
        Folder currentFolder;
        if(!folderRepo.existsByName(name)) {
            Folder folder = new Folder(name);
            folder.setVisible(false);
            folder.setFeatured(false);
            currentFolder = folderRepo.save(folder);
        } else {
            currentFolder = folderRepo.getFolderByName(name);
        }

        long x = 0;

        List<Artwork> artworkList = new ArrayList<>();
        String artUrl;
        for (MultipartFile file: files) {
            x++;

            String fileName = file.getOriginalFilename();
            String fileId = UUID.randomUUID().toString();
            artUrl = "/images/artworks/" + fileId + "." + FilenameUtils.getExtension(fileName);

            Artwork artwork = new Artwork();
            artwork.setName(name + "_" + x);
            artwork.setOrderNumber(x);
            artwork.setUrl(artUrl);
            artwork.setFolder(currentFolder);
            artwork.setVisible(true);

            File file1 = new File(dirUrl + artUrl);

            try {
                file.transferTo(file1.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            artworkList.add(artwork);
        }

        artworkRepo.saveAll(artworkList);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);

        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/toggleVisibility") public String toggleVisibility(@RequestParam("returnUrl") String returnUrl, @RequestParam List<Integer> picked_img_list,@RequestParam Folder folder, Model model) {
        artworkRepo.toggleVisibilityArtworksWithIds(picked_img_list);

        Iterable<Artwork> artworks = artworkRepo.findArtworksByFolder(folder);
        model.addAttribute("artworks", artworks);

        model.addAttribute("folder", folder);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/folder/toggleVisibility") public String toggleFolderVisibility(@RequestParam List<Integer> picked_folders_list, Model model) {
        folderRepo.toggleVisibilityFoldersWithIds(picked_folders_list);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);
        return "redirect:/greeting";
    }

    @PostMapping("/greeting/folder/toggleFeatured") public String toggleFolderFeatured(@RequestParam List<Integer> picked_folders_list, Model model) {
        folderRepo.toggleFeaturedFoldersWithIds(picked_folders_list);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);
        return "redirect:/greeting";
    }

    @PostMapping("/greeting/delete")
    public String delete(@RequestParam("returnUrl") String returnUrl, @RequestParam List<Integer> picked_img_list, @RequestParam Folder folder, Model model) {
        artworkRepo.deleteArtworksWithIds(picked_img_list);

        Iterable<Artwork> artworks = artworkRepo.findArtworksByFolder(folder);
        model.addAttribute("artworks", artworks);

        model.addAttribute("folder", folder);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/setPreview")
    public String setPreview(@RequestParam("returnUrl") String returnUrl, @RequestParam Integer picked_img_list, @RequestParam Folder folder, Model model) {
        Artwork art = artworkRepo.findArtworkById(picked_img_list);
        folderRepo.setAsFolderPreview(art, folder.getId());

        Iterable<Artwork> artworks = artworkRepo.findArtworksByFolder(folder);
        model.addAttribute("artworks", artworks);

        model.addAttribute("folder", folder);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/folder/delete")
    public String deleteFolder(@RequestParam List<Integer> picked_folders_list, Model model) {
        folderRepo.deleteFoldersWithIds(picked_folders_list);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);
        return "redirect:/greeting";
    }

    @PostMapping("/greeting/artwork/edit")
    public String editArtwork(@RequestParam("returnUrl") String returnUrl, @RequestParam Artwork artwork, @RequestParam String artwork_name, @RequestParam String artwork_description, Model model) {
        artwork.setName(artwork_name);
        artwork.setDescription(artwork_description);
        artworkRepo.save(artwork);

        model.addAttribute("artwork", artwork);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/folder/edit")
    public String editFolder(@RequestParam("returnUrl") String returnUrl, @RequestParam Folder folder, @RequestParam String folder_name, @RequestParam String folder_description, Model model) {
        folder.setName(folder_name);
        folder.setDescription(folder_description);
        folderRepo.save(folder);

        model.addAttribute("folder", folder);

        Iterable<Artwork> artworks = artworkRepo.findArtworksByFolder(folder);
        model.addAttribute("artworks", artworks);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/artwork/move")
    public String moveFolder(@RequestParam("returnUrl") String returnUrl, @RequestParam Artwork artwork, @RequestParam String destination_folder_id, Model model) {
        Folder folder = folderRepo.getFolderByName(destination_folder_id);
        artwork.setFolder(folder);
        artworkRepo.save(artwork);

        model.addAttribute("folder", folder);

        Iterable<Folder> folders = folderRepo.findAll();
        model.addAttribute("folders", folders);

        model.addAttribute("artwork", artwork);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/artwork/order/edit")
    public String editArtworksOrder(@RequestParam("returnUrl") String returnUrl, @RequestParam List<Integer> newOrder, @RequestParam Folder folder, Model model) {
        Iterable<Artwork> artworks =  artworkRepo.findArtworksByFolderOrderedById(folder);
        for (int i = 0; i < newOrder.size(); i++) {
            for (Artwork artwork: artworks) {
                if (Objects.equals(artwork.getId(), newOrder.get(i))) {
                    artwork.setOrderNumber((long) i + 1);
                    System.out.println(i);
                }
            }
        }
        System.out.println(newOrder.size());
        artworkRepo.saveAll(artworks);

        model.addAttribute("folder", folder);

        artworks = artworkRepo.findArtworksByFolderOrdered(folder);
        model.addAttribute("artworks", artworks);
        return "redirect:" + returnUrl;
    }

    @PostMapping("/greeting/folder/order/edit")
    public String editFoldersOrder(@RequestParam("returnUrl") String returnUrl, @RequestParam List<Integer> newOrder) {
        Iterable<Folder> folders =  folderRepo.findAllFoldersOrdered();
        for (int i = 0; i < newOrder.size(); i++) {
            for (Folder folder: folders) {
                if (Objects.equals(folder.getId(), newOrder.get(i))) {
                    folder.setOrderNumber(i + 1);
                    System.out.println(i);
                }
            }
        }
        System.out.println(newOrder.size());
        folderRepo.saveAll(folders);

        return "redirect:" + returnUrl;
    }

    @GetMapping("/art/{folderId}")
    public String artwork(@PathVariable int folderId, Model model) {
        int prevFolderId = -1;
        int nextFolderId = -1;

        Folder folder = folderRepo.getFolderById(folderId);
        model.addAttribute("folder", folder);
        List<Folder> folders;

        if (folder.getFeatured()) {
            folders = (List<Folder>) folderRepo.findFeaturedArtworksByFolderOrdered();
            for (int i = 0; i < folders.size(); i++) {
                if ( ( folders.get(i).getId().equals(folderId)  && (i + 1) < folders.size() ) ) {
                    nextFolderId = folders.get(i + 1).getId();
                }
                if ( ( folders.get(i).getId().equals(folderId)  && (i - 1) >= 0 ) ) {
                    prevFolderId = folders.get(i - 1).getId();
                }
            }
        } else {
            folders =  (List<Folder>) folderRepo.findVisibleArtworksByFolderOrdered();
            for (int i = 0; i < folders.size(); i++) {
                if ( ( folders.get(i).getId().equals(folderId)  && (i + 1) < folders.size() ) ) {
                    nextFolderId = folders.get(i + 1).getId();
                }
                if ( ( folders.get(i).getId().equals(folderId)  && (i - 1) >= 0 ) ) {
                    prevFolderId = folders.get(i - 1).getId();
                }
            }
        }

        Iterable<Artwork> artworks = artworkRepo.findArtworksByFolderOrdered(folder);
        model.addAttribute("artworks", artworks);
        if (nextFolderId > 0) {
            model.addAttribute("nextFolderId", nextFolderId);
        }
        if (prevFolderId > 0) {
            model.addAttribute("prevFolderId", prevFolderId);
        }
        return "artwork";
    }

    @GetMapping("/register")
    public String register() {
        if (userRepo.findByRole(Role.ADMIN) == null) {
            return "register";
        }
        return "registerNotAllowed";
    }

    @PostMapping("/register")
    public String registerAllow(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
        if (userRepo.findByRole(Role.ADMIN) == null) {
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);
            userRepo.save(admin);
            System.out.println("Администратор создан.");

            return "redirect:/login";
        }
        return "registerNotAllowed";
    }
}