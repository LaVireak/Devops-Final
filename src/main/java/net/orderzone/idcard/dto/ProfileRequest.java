package net.orderzone.idcard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.ProfileType;

import java.time.LocalDate;

public class ProfileRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Profile type is required")
    private ProfileType type;

    private String department;
    private String title;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String bloodGroup;
    private LocalDate dateOfBirth;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Long templateId;
    private BarcodeType barcodeType = BarcodeType.CODE_128;

    public ProfileRequest() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public ProfileType getType() { return type; }
    public void setType(ProfileType type) { this.type = type; }

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

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }

    public BarcodeType getBarcodeType() { return barcodeType; }
    public void setBarcodeType(BarcodeType barcodeType) { this.barcodeType = barcodeType; }
}
