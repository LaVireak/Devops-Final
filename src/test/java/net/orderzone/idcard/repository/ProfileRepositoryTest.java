package net.orderzone.idcard.repository;

import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProfileRepositoryTest {

    @Autowired
    ProfileRepository profileRepository;

    private Profile savedProfile;

    @BeforeEach
    void setUp() {
        savedProfile = profileRepository.save(Profile.builder()
                .uuid(UUID.randomUUID().toString())
                .registrationNumber("2026-ENG-001")
                .fullName("Sophea Chan")
                .type(ProfileType.STUDENT)
                .department("Engineering")
                .barcodeType(BarcodeType.CODE_128)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(2))
                .build());
    }

    @Test
    void findByUuid_shouldReturnProfile() {
        Optional<Profile> found = profileRepository.findByUuid(savedProfile.getUuid());
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Sophea Chan");
    }

    @Test
    void findByRegistrationNumber_shouldReturnProfile() {
        Optional<Profile> found = profileRepository.findByRegistrationNumber("2026-ENG-001");
        assertThat(found).isPresent();
    }

    @Test
    void existsByRegistrationNumber_shouldReturnTrue() {
        boolean exists = profileRepository.existsByRegistrationNumber("2026-ENG-001");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByRegistrationNumber_shouldReturnFalseWhenMissing() {
        boolean exists = profileRepository.existsByRegistrationNumber("9999-XXX-999");
        assertThat(exists).isFalse();
    }

    @Test
    void findByType_shouldReturnProfilesOfType() {
        List<Profile> students = profileRepository.findByType(ProfileType.STUDENT);
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getFullName()).isEqualTo("Sophea Chan");
    }

    @Test
    void findByFullNameContainingIgnoreCase_shouldFindByPartialName() {
        var page = profileRepository.findByFullNameContainingIgnoreCase(
                "sophea", org.springframework.data.domain.Pageable.unpaged());
        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void countByRegistrationNumberPrefix_shouldCountCorrectly() {
        long count = profileRepository.countByRegistrationNumberPrefix("2026-ENG-");
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void save_shouldPersistAllFields() {
        assertThat(savedProfile.getId()).isNotNull();
        assertThat(savedProfile.getCreatedAt()).isNotNull();
        assertThat(savedProfile.getUpdatedAt()).isNotNull();
        assertThat(savedProfile.getIssueDate()).isNotNull();
    }
}
