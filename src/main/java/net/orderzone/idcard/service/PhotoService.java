package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoService {

    private static final long MAX_SIZE_BYTES = 5L * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES =
            List.of("image/jpeg", "image/png", "image/jpg");

    @Value("${app.photo-dir:./uploads/photos}")
    private String photoDir;

    private final ProfileRepository profileRepository;

    public PhotoService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public String savePhoto(Long profileId, MultipartFile file) throws IOException {
        validatePhoto(file);
        Profile profile = findProfile(profileId);

        if (profile.hasPhoto()) deleteFile(profile.getPhotoFileName());

        Path dir = Paths.get(photoDir);
        Files.createDirectories(dir);

        String ext = extractExtension(file.getOriginalFilename());
        String fileName = profileId + "_" + UUID.randomUUID() + "." + ext;
        file.transferTo(dir.resolve(fileName));

        profile.setPhotoFileName(fileName);
        profile.setPhotoContentType(file.getContentType());
        profileRepository.save(profile);
        return fileName;
    }

    public byte[] getPhotoBytes(Long profileId) throws IOException {
        Profile profile = findProfile(profileId);
        if (!profile.hasPhoto()) {
            throw new IllegalStateException("Profile " + profileId + " has no photo");
        }
        return Files.readAllBytes(Paths.get(photoDir).resolve(profile.getPhotoFileName()));
    }

    public String getContentType(Long profileId) {
        return profileRepository.findById(profileId)
                .map(p -> p.getPhotoContentType() != null ? p.getPhotoContentType() : "image/jpeg")
                .orElse("image/jpeg");
    }

    private void validatePhoto(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Uploaded file is empty");
        if (file.getSize() > MAX_SIZE_BYTES) throw new IllegalArgumentException("Photo exceeds 5 MB limit");
        String ct = file.getContentType();
        if (ct == null || !ALLOWED_CONTENT_TYPES.contains(ct.toLowerCase()))
            throw new IllegalArgumentException("Only JPEG and PNG images are accepted");
    }

    private Profile findProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));
    }

    private void deleteFile(String fileName) {
        try { Files.deleteIfExists(Paths.get(photoDir).resolve(fileName)); } catch (IOException ignored) {}
    }

    private String extractExtension(String filename) {
        if (filename == null) return "jpg";
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot + 1).toLowerCase() : "jpg";
    }
}
