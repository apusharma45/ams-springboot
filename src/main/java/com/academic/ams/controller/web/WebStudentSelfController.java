package com.academic.ams.controller.web;

import com.academic.ams.dto.StudentSelfUpdateRequest;
import com.academic.ams.dto.StudentResponse;
import com.academic.ams.dto.web.StudentProfileForm;
import com.academic.ams.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/students")
@PreAuthorize("hasRole('STUDENT')")
public class WebStudentSelfController {

    private final StudentService studentService;

    public WebStudentSelfController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 1) Show "My Profile"
    @GetMapping("/me")
    public String myProfile(Authentication auth, Model model) {
        String studentId = auth.getName();

        StudentResponse me = studentService.getMyProfile(studentId);

        StudentProfileForm form = new StudentProfileForm();
        form.setName(me.getName());
        form.setEmail(me.getEmail());
        form.setPhone(me.getPhone());

        model.addAttribute("me", me);                 // for display
        model.addAttribute("profileForm", form);      // for editing
        return "web/student-me";
    }

    // 2) Update "My Profile"
    @PostMapping("/me")
    public String updateMyProfile(@Valid @ModelAttribute("profileForm") StudentProfileForm form,
                                  BindingResult br,
                                  Authentication auth,
                                  Model model) {

        String studentId = auth.getName();

        if (br.hasErrors()) {
            // reload "me" so page can still show current info
            model.addAttribute("me", studentService.getMyProfile(studentId));
            return "web/student-me";
        }

        StudentSelfUpdateRequest req = new StudentSelfUpdateRequest();
        req.setName(form.getName());
        req.setEmail(form.getEmail());
        req.setPhone(form.getPhone());

        try {
            studentService.updateOwnProfile(studentId, req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("me", studentService.getMyProfile(studentId));
            return "web/student-me";
        }

        return "redirect:/web/students/me?success=true";
    }
}
