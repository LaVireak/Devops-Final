package net.orderzone.idcard.controller;

import jakarta.validation.Valid;
import net.orderzone.idcard.dto.ProfileRequest;
import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileApiController {

    private final ProfileService profileService;

    public ProfileApiController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public Page<ProfileResponse> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return profileService.listProfiles(
                search, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(request));
    }

    @GetMapping("/{id}")
    public ProfileResponse get(@PathVariable Long id) {
        return profileService.getProfile(id);
    }

    @PutMapping("/{id}")
    public ProfileResponse update(@PathVariable Long id,
                                   @Valid @RequestBody ProfileRequest request) {
        return profileService.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
