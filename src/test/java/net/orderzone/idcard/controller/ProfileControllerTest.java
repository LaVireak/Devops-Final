package net.orderzone.idcard.controller;

import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.service.ProfileService;
import net.orderzone.idcard.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean ProfileService profileService;
    @MockBean TemplateService templateService;

    @Test
    void listProfiles_shouldReturn200AndRenderView() throws Exception {
        ProfileResponse sample = ProfileResponse.builder()
                .id(1L)
                .uuid(UUID.randomUUID().toString())
                .registrationNumber("2026-STU-001")
                .fullName("Test User")
                .type(ProfileType.STUDENT)
                .barcodeType(BarcodeType.CODE_128)
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(2))
                .templatePrimaryColor("#1d4ed8")
                .templateSecondaryColor("#e0e7ff")
                .build();

        when(profileService.listProfiles(isNull(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sample)));
        when(templateService.listTemplates()).thenReturn(List.of());

        mockMvc.perform(get("/profiles"))
                .andExpect(status().isOk())
                .andExpect(view().name("profiles/list"))
                .andExpect(model().attributeExists("profiles"));
    }

    @Test
    void newProfileForm_shouldReturn200AndShowForm() throws Exception {
        when(templateService.listTemplates()).thenReturn(List.of());

        mockMvc.perform(get("/profiles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("profiles/form"))
                .andExpect(model().attributeExists("profileRequest"))
                .andExpect(model().attributeExists("templates"))
                .andExpect(model().attributeExists("profileTypes"));
    }
}
