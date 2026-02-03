package com.academic.ams.controller.web;

import com.academic.ams.dto.web.DeptForm;
import com.academic.ams.dto.DeptCreateRequest;
import com.academic.ams.service.DeptService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/depts")
@PreAuthorize("hasRole('TEACHER')")
public class WebDeptController {

    private final DeptService deptService;

    public WebDeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping("/new")
    public String newDeptForm(Model model) {
        model.addAttribute("deptForm", new DeptForm());
        return "web/dept-new";
    }

    @PostMapping
    public String createDept(@Valid @ModelAttribute("deptForm") DeptForm form,
                             BindingResult br,
                             Model model) {
        if (br.hasErrors()) {
            return "web/dept-new";
        }

        // reuse your existing API DTO internally
        DeptCreateRequest req = new DeptCreateRequest();
        req.setCode(form.getCode());
        req.setName(form.getName());

        try {
            deptService.createDept(req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "web/dept-new";
        }

        return "redirect:/web/home";
    }
}
