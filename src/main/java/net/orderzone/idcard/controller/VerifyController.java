package net.orderzone.idcard.controller;

import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.repository.TemplateRepository;
import net.orderzone.idcard.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VerifyController {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final TemplateRepository templateRepository;

    public VerifyController(ProfileService profileService,
                            ProfileRepository profileRepository,
                            TemplateRepository templateRepository) {
        this.profileService = profileService;
        this.profileRepository = profileRepository;
        this.templateRepository = templateRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalProfiles", profileRepository.count());
        model.addAttribute("studentCount", profileRepository.countByType(ProfileType.STUDENT));
        model.addAttribute("employeeCount", profileRepository.countByType(ProfileType.EMPLOYEE));
        model.addAttribute("templateCount", templateRepository.count());
        return "index";
    }

    @GetMapping("/verify/{uuid}")
    public String verify(@PathVariable String uuid, Model model) {
        try {
            ProfileResponse profile = profileService.getProfileByUuid(uuid);
            model.addAttribute("profile", profile);
            model.addAttribute("valid", true);
        } catch (Exception e) {
            model.addAttribute("valid", false);
            model.addAttribute("error", "No profile found for this QR code.");
        }
        return "verify";
    }
}
