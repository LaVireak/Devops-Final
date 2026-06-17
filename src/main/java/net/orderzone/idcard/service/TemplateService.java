package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template createTemplate(Template template) {
        if (templateRepository.existsByCode(template.getCode())) {
            throw new IllegalArgumentException(
                    "Template with code '" + template.getCode() + "' already exists");
        }
        return templateRepository.save(template);
    }

    @Transactional(readOnly = true)
    public List<Template> listTemplates() {
        return templateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Template getTemplate(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
    }

    public Template updateTemplate(Long id, Template updated) {
        Template existing = getTemplate(id);
        existing.setName(updated.getName());
        existing.setOrganizationName(updated.getOrganizationName());
        existing.setLayout(updated.getLayout());
        existing.setPrimaryColor(updated.getPrimaryColor());
        existing.setSecondaryColor(updated.getSecondaryColor());
        existing.setTextColor(updated.getTextColor());
        existing.setTagline(updated.getTagline());
        return templateRepository.save(existing);
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    public void seedDefaultTemplates() {
        if (templateRepository.count() > 0) return;

        templateRepository.save(Template.builder()
                .code("DEFAULT").name("Classic Blue").organizationName("ITC University")
                .layout("VERTICAL").primaryColor("#1d4ed8").secondaryColor("#e0e7ff")
                .textColor("#111827").tagline("Excellence in Technology").build());

        templateRepository.save(Template.builder()
                .code("EMERALD").name("Emerald Green").organizationName("ITC University")
                .layout("VERTICAL").primaryColor("#059669").secondaryColor("#d1fae5")
                .textColor("#064e3b").tagline("Growing Together").build());

        templateRepository.save(Template.builder()
                .code("CRIMSON").name("Crimson Red").organizationName("ITC University")
                .layout("VERTICAL").primaryColor("#dc2626").secondaryColor("#fee2e2")
                .textColor("#7f1d1d").tagline("Leadership & Excellence").build());
    }
}
