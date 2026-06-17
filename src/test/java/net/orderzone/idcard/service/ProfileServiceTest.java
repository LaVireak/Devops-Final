package net.orderzone.idcard.service;

import net.orderzone.idcard.builder.ProfileBuilder;
import net.orderzone.idcard.dto.ProfileRequest;
import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock ProfileRepository profileRepository;
    @Mock TemplateRepository templateRepository;
    @Mock ProfileBuilder profileBuilder;

    @InjectMocks ProfileService profileService;

    private Profile sampleProfile;

    @BeforeEach
    void setUp() {
        sampleProfile = Profile.builder()
                .uuid(UUID.randomUUID().toString())
                .registrationNumber("2026-ENG-001")
                .fullName("Sophea Chan")
                .type(ProfileType.STUDENT)
                .department("Engineering")
                .barcodeType(BarcodeType.CODE_128)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(2))
                .build();
        // Manually set id since it's normally set by persistence
        sampleProfile.setId(1L);
    }

    @Test
    void createProfile_shouldSaveAndReturnResponse() {
        ProfileRequest req = new ProfileRequest();
        req.setFullName("Sophea Chan");
        req.setType(ProfileType.STUDENT);
        req.setDepartment("Engineering");

        when(profileBuilder.buildDefault(anyString(), any(), anyString(), any()))
                .thenReturn(sampleProfile);
        when(profileRepository.save(any())).thenReturn(sampleProfile);

        ProfileResponse result = profileService.createProfile(req);

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo("Sophea Chan");
        assertThat(result.getRegistrationNumber()).isEqualTo("2026-ENG-001");
        verify(profileRepository, times(1)).save(any());
    }

    @Test
    void getProfile_shouldReturnProfileById() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(sampleProfile));

        ProfileResponse response = profileService.getProfile(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFullName()).isEqualTo("Sophea Chan");
    }

    @Test
    void getProfile_shouldThrowWhenNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getProfile(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Profile not found: 99");
    }

    @Test
    void updateProfile_shouldUpdateAndReturnUpdatedResponse() {
        ProfileRequest req = new ProfileRequest();
        req.setFullName("Updated Name");
        req.setType(ProfileType.EMPLOYEE);
        req.setDepartment("HR");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(sampleProfile));
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProfileResponse result = profileService.updateProfile(1L, req);

        assertThat(result.getFullName()).isEqualTo("Updated Name");
        assertThat(result.getType()).isEqualTo(ProfileType.EMPLOYEE);
    }

    @Test
    void deleteProfile_shouldCallRepositoryDelete() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(sampleProfile));
        doNothing().when(profileRepository).delete(sampleProfile);

        assertThatCode(() -> profileService.deleteProfile(1L)).doesNotThrowAnyException();
        verify(profileRepository, times(1)).delete(sampleProfile);
    }

    @Test
    void toResponse_shouldMapPhotoUrlCorrectly() {
        sampleProfile.setPhotoFileName("test.jpg");
        ProfileResponse response = profileService.toResponse(sampleProfile);

        assertThat(response.getPhotoUrl()).isEqualTo("/profiles/1/photo");
    }

    @Test
    void toResponse_shouldReturnNullPhotoUrlWhenNoPhoto() {
        ProfileResponse response = profileService.toResponse(sampleProfile);
        assertThat(response.getPhotoUrl()).isNull();
    }
}
