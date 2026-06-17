package net.orderzone.idcard.controller;

import jakarta.validation.Valid;
import net.orderzone.idcard.dto.ProfileRequest;
import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.service.ProfileService;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final TemplateService templateService;

    public ProfileController(ProfileService profileService, TemplateService templateService) {
        this.profileService = profileService;
        this.templateService = templateService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size,
                       Model model) {
        Page<ProfileResponse> profiles = profileService.listProfiles(
                search, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        model.addAttribute("profiles", profiles);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", profiles.getTotalPages());
        return "profiles/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("profileRequest", new ProfileRequest());
        model.addAttribute("templates", templateService.listTemplates());
        model.addAttribute("profileTypes", ProfileType.values());
        model.addAttribute("formAction", "/profiles");
        model.addAttribute("pageTitle", "New Profile");
        return "profiles/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("profileRequest") ProfileRequest request,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("templates", templateService.listTemplates());
            model.addAttribute("profileTypes", ProfileType.values());
            model.addAttribute("formAction", "/profiles");
            model.addAttribute("pageTitle", "New Profile");
            return "profiles/form";
        }
        ProfileResponse created = profileService.createProfile(request);
        ra.addFlashAttribute("successMessage", "Profile created successfully!");
        return "redirect:/profiles/" + created.getId() + "/preview";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProfileResponse profile = profileService.getProfile(id);
        ProfileRequest req = new ProfileRequest();
        req.setFullName(profile.getFullName());
        req.setType(profile.getType());
        req.setDepartment(profile.getDepartment());
        req.setTitle(profile.getTitle());
        req.setEmail(profile.getEmail());
        req.setPhone(profile.getPhone());
        req.setBloodGroup(profile.getBloodGroup());
        req.setDateOfBirth(profile.getDateOfBirth());
        req.setIssueDate(profile.getIssueDate());
        req.setExpiryDate(profile.getExpiryDate());
        req.setTemplateId(profile.getTemplateId());
        req.setBarcodeType(profile.getBarcodeType());

        model.addAttribute("profileRequest", req);
        model.addAttribute("profile", profile);
        model.addAttribute("profileId", id);
        model.addAttribute("templates", templateService.listTemplates());
        model.addAttribute("profileTypes", ProfileType.values());
        model.addAttribute("formAction", "/profiles/" + id + "/update");
        model.addAttribute("pageTitle", "Edit Profile");
        return "profiles/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("profileRequest") ProfileRequest request,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("templates", templateService.listTemplates());
            model.addAttribute("profileTypes", ProfileType.values());
            model.addAttribute("formAction", "/profiles/" + id + "/update");
            model.addAttribute("pageTitle", "Edit Profile");
            return "profiles/form";
        }
        profileService.updateProfile(id, request);
        ra.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/profiles/" + id + "/preview";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        profileService.deleteProfile(id);
        ra.addFlashAttribute("successMessage", "Profile deleted.");
        return "redirect:/profiles";
    }

    @GetMapping("/{id}/preview")
    public String preview(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id);
        model.addAttribute("profile", profile);
        return "card-preview";
    }
}
