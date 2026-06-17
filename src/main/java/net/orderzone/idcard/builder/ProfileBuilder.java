package net.orderzone.idcard.builder;

import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.repository.ProfileRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.UUID;

@Component
public class ProfileBuilder {

    private final ProfileRepository profileRepository;

    public ProfileBuilder(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile buildDefault(String fullName, ProfileType type, String department, Template template) {
        String uuid = UUID.randomUUID().toString();
        String regNumber = generateRegistrationNumber(type, department);

        return Profile.builder()
                .uuid(uuid)
                .registrationNumber(regNumber)
                .fullName(fullName)
                .type(type)
                .department(department)
                .template(template)
                .barcodeType(BarcodeType.CODE_128)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(2))
                .build();
    }

    public String generateRegistrationNumber(ProfileType type, String department) {
        int year = Year.now().getValue();
        String deptCode = normalizeDept(department, type);
        String prefix = year + "-" + deptCode + "-";
        long count = profileRepository.countByRegistrationNumberPrefix(prefix);
        return String.format("%s%03d", prefix, count + 1);
    }

    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

    private String normalizeDept(String department, ProfileType type) {
        if (department != null && !department.isBlank()) {
            String cleaned = department.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
            return cleaned.substring(0, Math.min(cleaned.length(), 4));
        }
        return switch (type) {
            case STUDENT  -> "STU";
            case EMPLOYEE -> "EMP";
            case USER     -> "USR";
        };
    }
}
