package net.orderzone.idcard.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles",
        uniqueConstraints = @UniqueConstraint(name = "uk_profile_reg_number", columnNames = "registration_number"))
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @Column(name = "registration_number", nullable = false, unique = true, length = 64)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ProfileType type;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(length = 80)
    private String department;

    @Column(length = 120)
    private String title;

    @Column(length = 120)
    private String email;

    @Column(length = 40)
    private String phone;

    @Column(length = 60)
    private String bloodGroup;

    private LocalDate dateOfBirth;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    @Column(length = 255)
    private String photoFileName;

    @Column(length = 60)
    private String photoContentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private Template template;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private BarcodeType barcodeType = BarcodeType.CODE_128;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ── JPA lifecycle ──────────────────────────────────────────────────────────

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.issueDate == null) this.issueDate = LocalDate.now();
    }

    @PreUpdate
    void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    @Transient
    public boolean hasPhoto() {
        return photoFileName != null && !photoFileName.isBlank();
    }

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

    public String getPhotoFileName() { return photoFileName; }
    public void setPhotoFileName(String photoFileName) { this.photoFileName = photoFileName; }

    public String getPhotoContentType() { return photoContentType; }
    public void setPhotoContentType(String photoContentType) { this.photoContentType = photoContentType; }

    public Template getTemplate() { return template; }
    public void setTemplate(Template template) { this.template = template; }

    public BarcodeType getBarcodeType() { return barcodeType; }
    public void setBarcodeType(BarcodeType barcodeType) { this.barcodeType = barcodeType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ────────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public Profile() {}

    private Profile(Builder b) {
        this.uuid = b.uuid;
        this.registrationNumber = b.registrationNumber;
        this.type = b.type;
        this.fullName = b.fullName;
        this.department = b.department;
        this.title = b.title;
        this.email = b.email;
        this.phone = b.phone;
        this.bloodGroup = b.bloodGroup;
        this.dateOfBirth = b.dateOfBirth;
        this.issueDate = b.issueDate;
        this.expiryDate = b.expiryDate;
        this.photoFileName = b.photoFileName;
        this.photoContentType = b.photoContentType;
        this.template = b.template;
        this.barcodeType = b.barcodeType != null ? b.barcodeType : BarcodeType.CODE_128;
    }

    public static class Builder {
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
        private String photoFileName;
        private String photoContentType;
        private Template template;
        private BarcodeType barcodeType = BarcodeType.CODE_128;

        public Builder uuid(String uuid) { this.uuid = uuid; return this; }
        public Builder registrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; return this; }
        public Builder type(ProfileType type) { this.type = type; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public Builder issueDate(LocalDate issueDate) { this.issueDate = issueDate; return this; }
        public Builder expiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder photoFileName(String photoFileName) { this.photoFileName = photoFileName; return this; }
        public Builder photoContentType(String photoContentType) { this.photoContentType = photoContentType; return this; }
        public Builder template(Template template) { this.template = template; return this; }
        public Builder barcodeType(BarcodeType barcodeType) { this.barcodeType = barcodeType; return this; }
        public Profile build() { return new Profile(this); }
    }
}
