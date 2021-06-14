package com.moroz.testtask.test_task.controller;

import com.moroz.testtask.test_task.model.Status;
import com.moroz.testtask.test_task.model.UserAccount;
import com.moroz.testtask.test_task.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/login")
public class LoginPageController {
    private UserService userService;
    private MessageSource messageSource;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping
    public String showLoginPage() {
        return "loginPage/loginPage";
    }

    @PostMapping("/logout")
    public String logout() {
        return "loginPage/loginPage";
    }

    @GetMapping("/successEnter")
    public String successEnter(Model model, Principal principal) {
        UserAccount user = userService.returnUserAccountByUserName(principal.getName());
        model.addAttribute("user", user);
        return "redirect:/user";
    }

    @GetMapping("/failedEnter")
    public String failedEnter(Model model) {
        model.addAttribute("msg", messageSource.getMessage("error.authorization", new String[]{"error.authorization"}, LocaleContextHolder.getLocale()));
        return "loginPage/loginPage";
    }
}
