package net.orderzone.idcard.dto;

import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.ProfileType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProfileResponse {

    private Long id;
    private String uuid;
    private String registrationNumber;
    private ProfileType type;
    private String fullName;
    private String department;
    private String title;
    private String email;
    private String phone;
    private String bloodGroup;
    private LocalDate dateOfBirth;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String photoUrl;
    private BarcodeType barcodeType;
    private Long templateId;
    private String templateName;
    private String templatePrimaryColor;
    private String templateSecondaryColor;
    private String organizationName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProfileResponse() {}

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public ProfileType getType() { return type; }
    public void setType(ProfileType type) { this.type = type; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public BarcodeType getBarcodeType() { return barcodeType; }
    public void setBarcodeType(BarcodeType barcodeType) { this.barcodeType = barcodeType; }

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getTemplatePrimaryColor() { return templatePrimaryColor; }
    public void setTemplatePrimaryColor(String templatePrimaryColor) { this.templatePrimaryColor = templatePrimaryColor; }

    public String getTemplateSecondaryColor() { return templateSecondaryColor; }
    public void setTemplateSecondaryColor(String templateSecondaryColor) { this.templateSecondaryColor = templateSecondaryColor; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ────────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final ProfileResponse r = new ProfileResponse();
        public Builder id(Long id) { r.id = id; return this; }
        public Builder uuid(String uuid) { r.uuid = uuid; return this; }
        public Builder registrationNumber(String v) { r.registrationNumber = v; return this; }
        public Builder type(ProfileType v) { r.type = v; return this; }
        public Builder fullName(String v) { r.fullName = v; return this; }
        public Builder department(String v) { r.department = v; return this; }
        public Builder title(String v) { r.title = v; return this; }
        public Builder email(String v) { r.email = v; return this; }
        public Builder phone(String v) { r.phone = v; return this; }
        public Builder bloodGroup(String v) { r.bloodGroup = v; return this; }
        public Builder dateOfBirth(LocalDate v) { r.dateOfBirth = v; return this; }
        public Builder issueDate(LocalDate v) { r.issueDate = v; return this; }
        public Builder expiryDate(LocalDate v) { r.expiryDate = v; return this; }
        public Builder photoUrl(String v) { r.photoUrl = v; return this; }
        public Builder barcodeType(BarcodeType v) { r.barcodeType = v; return this; }
        public Builder templateId(Long v) { r.templateId = v; return this; }
        public Builder templateName(String v) { r.templateName = v; return this; }
        public Builder templatePrimaryColor(String v) { r.templatePrimaryColor = v; return this; }
        public Builder templateSecondaryColor(String v) { r.templateSecondaryColor = v; return this; }
        public Builder organizationName(String v) { r.organizationName = v; return this; }
        public Builder createdAt(LocalDateTime v) { r.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v) { r.updatedAt = v; return this; }
        public ProfileResponse build() { return r; }
    }
}
