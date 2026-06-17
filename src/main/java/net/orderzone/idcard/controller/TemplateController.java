package net.orderzone.idcard.controller;

import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", templateService.listTemplates());
        model.addAttribute("newTemplate", new Template());
        return "templates/list";
    }

    @PostMapping
    public String create(@ModelAttribute Template template, RedirectAttributes ra) {
        templateService.createTemplate(template);
        ra.addFlashAttribute("successMessage", "Template created!");
        return "redirect:/templates";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("template", templateService.getTemplate(id));
        return "templates/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                          @ModelAttribute Template template,
                          RedirectAttributes ra) {
        templateService.updateTemplate(id, template);
        ra.addFlashAttribute("successMessage", "Template updated!");
        return "redirect:/templates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        templateService.deleteTemplate(id);
        ra.addFlashAttribute("successMessage", "Template deleted.");
        return "redirect:/templates";
    }
}
