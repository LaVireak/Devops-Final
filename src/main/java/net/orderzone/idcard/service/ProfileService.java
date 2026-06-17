package net.orderzone.idcard.service;

import net.orderzone.idcard.builder.ProfileBuilder;
import net.orderzone.idcard.dto.ProfileRequest;
import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.repository.TemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final TemplateRepository templateRepository;
    private final ProfileBuilder profileBuilder;

    public ProfileService(ProfileRepository profileRepository,
                          TemplateRepository templateRepository,
                          ProfileBuilder profileBuilder) {
        this.profileRepository = profileRepository;
        this.templateRepository = templateRepository;
        this.profileBuilder = profileBuilder;
    }

    public ProfileResponse createProfile(ProfileRequest request) {
        Template template = resolveTemplate(request.getTemplateId());
        Profile profile = profileBuilder.buildDefault(
                request.getFullName(), request.getType(), request.getDepartment(), template);
        applyRequest(profile, request, template);
        return toResponse(profileRepository.save(profile));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByUuid(String uuid) {
        Profile p = profileRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with UUID: " + uuid));
        return toResponse(p);
    }

    @Transactional(readOnly = true)
    public Page<ProfileResponse> listProfiles(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return profileRepository.findByFullNameContainingIgnoreCase(search, pageable)
                    .map(this::toResponse);
        }
        return profileRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ProfileResponse> listByType(ProfileType type) {
        return profileRepository.findByType(type).stream().map(this::toResponse).toList();
    }

    public ProfileResponse updateProfile(Long id, ProfileRequest request) {
        Profile profile = findById(id);
        Template template = resolveTemplate(request.getTemplateId());
        applyRequest(profile, request, template);
        return toResponse(profileRepository.save(profile));
    }

    public void deleteProfile(Long id) {
        profileRepository.delete(findById(id));
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));
    }

    private Template resolveTemplate(Long templateId) {
        if (templateId == null) return null;
        return templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));
    }

    private void applyRequest(Profile profile, ProfileRequest req, Template template) {
        profile.setFullName(req.getFullName());
        profile.setType(req.getType());
        profile.setDepartment(req.getDepartment());
        profile.setTitle(req.getTitle());
        profile.setEmail(req.getEmail());
        profile.setPhone(req.getPhone());
        profile.setBloodGroup(req.getBloodGroup());
        profile.setDateOfBirth(req.getDateOfBirth());
        if (req.getIssueDate() != null)  profile.setIssueDate(req.getIssueDate());
        if (req.getExpiryDate() != null) profile.setExpiryDate(req.getExpiryDate());
        if (req.getBarcodeType() != null) profile.setBarcodeType(req.getBarcodeType());
        if (template != null) profile.setTemplate(template);
    }

    public ProfileResponse toResponse(Profile p) {
        return ProfileResponse.builder()
                .id(p.getId())
                .uuid(p.getUuid())
                .registrationNumber(p.getRegistrationNumber())
                .type(p.getType())
                .fullName(p.getFullName())
                .department(p.getDepartment())
                .title(p.getTitle())
                .email(p.getEmail())
                .phone(p.getPhone())
                .bloodGroup(p.getBloodGroup())
                .dateOfBirth(p.getDateOfBirth())
                .issueDate(p.getIssueDate())
                .expiryDate(p.getExpiryDate())
                .photoUrl(p.hasPhoto() ? "/profiles/" + p.getId() + "/photo" : null)
                .barcodeType(p.getBarcodeType())
                .templateId(p.getTemplate() != null ? p.getTemplate().getId() : null)
                .templateName(p.getTemplate() != null ? p.getTemplate().getName() : null)
                .templatePrimaryColor(p.getTemplate() != null ? p.getTemplate().getPrimaryColor() : "#1d4ed8")
                .templateSecondaryColor(p.getTemplate() != null ? p.getTemplate().getSecondaryColor() : "#e0e7ff")
                .organizationName(p.getTemplate() != null ? p.getTemplate().getOrganizationName() : "ID Card")
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
