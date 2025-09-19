package com.piattaforme.educonnect.presentation.controller;

import com.piattaforme.educonnect.business.service.UserService;
import com.piattaforme.educonnect.business.dto.UserRegistrationDTO;
import com.piattaforme.educonnect.persistence.entity.User;
import com.piattaforme.educonnect.persistence.entity.Student;
import com.piattaforme.educonnect.persistence.entity.Tutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Mostra pagina di login
     */
    @GetMapping("/login")
    public String showLoginForm(Model model,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout) {

        if (error != null) {
            model.addAttribute("error", "Username o password non corretti!");
        }

        if (logout != null) {
            model.addAttribute("message", "Logout effettuato con successo!");
        }

        return "auth/login";
    }

    /**
     * Gestisce login form
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Optional<User> userOpt = userService.authenticateUser(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userType", user.getClass().getSimpleName().toUpperCase());

            // Redirect basato sul tipo utente
            if (user instanceof Student) {
                return "redirect:/student/dashboard";
            } else if (user instanceof Tutor) {
                return "redirect:/tutor/dashboard";
            } else {
                return "redirect:/admin/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Credenziali non valide");
            return "redirect:/auth/login";
        }
    }

    /**
     * Mostra pagina di registrazione
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationDTO", new UserRegistrationDTO());
        return "auth/register";
    }

    /**
     * Gestisce registrazione studente
     */
    @PostMapping("/register/student")
    public String registerStudent(@Valid @ModelAttribute UserRegistrationDTO registrationDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationDTO", registrationDTO);
            return "auth/register";
        }

        // Validazione password
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            model.addAttribute("error", "Le password non coincidono");
            model.addAttribute("registrationDTO", registrationDTO);
            return "auth/register";
        }

        try {
            Student student = userService.registerStudent(registrationDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Registrazione completata! Puoi effettuare il login.");
            return "redirect:/auth/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registrationDTO", registrationDTO);
            return "auth/register";
        }
    }

    /**
     * Gestisce registrazione tutor
     */
    @PostMapping("/register/tutor")
    public String registerTutor(@Valid @ModelAttribute UserRegistrationDTO registrationDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationDTO", registrationDTO);
            return "auth/register";
        }

        // Validazione password
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            model.addAttribute("error", "Le password non coincidono");
            model.addAttribute("registrationDTO", registrationDTO);
            return "auth/register";
        }

        try {
            Tutor tutor = userService.registerTutor(registrationDTO);
            redirectAttributes.addFlashAttribute("success",
                    "Registrazione tutor completata! Puoi effettuare il login.");
            return "redirect:/auth/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registrationDTO", registrationDTO);
            return "auth/register";
        }
    }

    /**
     * Gestisce logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout";
    }
}