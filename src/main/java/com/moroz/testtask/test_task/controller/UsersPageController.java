package com.moroz.testtask.test_task.controller;

import com.moroz.testtask.test_task.model.Status;
import com.moroz.testtask.test_task.model.UserAccount;
import com.moroz.testtask.test_task.service.interfaces.RoleService;
import com.moroz.testtask.test_task.service.interfaces.UserService;
import com.moroz.testtask.test_task.validator.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
public class UsersPageController {
    private UserService userService;
    private RoleService roleService;
    private MessageSource messageSource;
    private FormValidator validator;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setValidator(FormValidator validator) {
        this.validator = validator;
    }

    public void getCurrentUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            if (userDetail != null) {
                String name = userDetail.getUsername();
                UserAccount userAccount = (UserAccount) userService.returnUserAccountByUserName(name);
                model.addAttribute("username", userAccount.getFirstName());
                model.addAttribute("status", userAccount.getStatus());
                model.addAttribute("roles", userAccount.getUsersRole().toString());
            }
        }
    }

    @GetMapping
    public String showAllUsers(HttpServletRequest request, @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               Model model) {
        getCurrentUser(model);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Page<UserAccount> usersPage = userService.returnAllUserAccountList(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("usersList", usersPage);
        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("listOfRoles", roleService.returnAllRoles());
        return "userPage/usersPage";
    }

    @PostMapping
    public String changeStatus(HttpServletRequest request,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size, Model model) {
        List<UserAccount> users = new ArrayList<>();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        if (request.getParameter("form").equals("findByRole")) {
            try {
                long roleId = Long.parseLong(request.getParameter("roleId"));
                if (roleId == 0) {
                    currentPage = page.orElse(1);
                    pageSize = size.orElse(3);
                    users = userService.returnAllUserAccountList(PageRequest.of(currentPage - 1, pageSize)).getContent();
                } else {
                    users = userService.returnUsersByRoleId(roleId, PageRequest.of(currentPage - 1, pageSize)).getContent();
                }
                getCurrentUser(model);
            } catch (NumberFormatException e) {
                getCurrentUser(model);
                String errorType = messageSource.getMessage("error.wrongType", new Object[]{"error.wrongType"}, LocaleContextHolder.getLocale());
                model.addAttribute("error", errorType);
            }
        }
        if (request.getParameter("form").equals("findByUserName")) {
            String name = request.getParameter("name");
            users = userService.findByPartOfUsername(name, PageRequest.of(currentPage - 1, pageSize)).getContent();
            getCurrentUser(model);
        }
        model.addAttribute("usersList", users);
        model.addAttribute("listOfRoles", roleService.returnAllRoles());
        return "userPage/usersPage";
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute("user") UserAccount userAccount, Model model) {
        model.addAttribute("status", Status.values());
        model.addAttribute("roles", roleService.returnAllRoles());
        return "userPage/addNewUser";
    }

    @PostMapping("/new")
    public String saveNewUser(HttpServletRequest request,
                              @RequestParam(value = "role") int roleID,
                              @RequestParam(value = "status", defaultValue = "INACTIVE") String status,
                              @ModelAttribute("user") UserAccount userAccount, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("user", userAccount);
        model.addAttribute("fieldNameError", validator.checkFieldUserName(request.getParameter("username")));
        model.addAttribute("fieldPassError", validator.checkFieldPass(request.getParameter("password")));
        model.addAttribute("fieldFirstNameError", validator.checkFieldName(request.getParameter("firstName")));
        model.addAttribute("fieldLastNameError", validator.checkFieldName(request.getParameter("lastName")));
        if (!validator.checkAddUserFormValid(request.getParameter("username"), request.getParameter("password"),
                request.getParameter("firstName"), request.getParameter("lastName"))) {
            model.addAttribute("status", Status.values());
            model.addAttribute("roles", roleService.returnAllRoles());
            return "userPage/addNewUser";
        } else if (bindingResult.hasErrors()) {
            redirectAttributes.getFlashAttributes().clear();
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.manager", bindingResult);
            redirectAttributes.addFlashAttribute("user", userAccount);
            model.addAttribute("status", Status.values());
            model.addAttribute("roles", roleService.returnAllRoles());
            return "userPage/addNewUser";
        } else {
            userService.saveNewUserAccount(userAccount, roleID, status);
        }
        return "redirect:/user";
    }

    @GetMapping("/{id}")
    public String showUserDetailsPage(Model model, @PathVariable("id") long id) {
        UserAccount userAccount = userService.returnUserAccountByID(id);
        model.addAttribute("user", userAccount);
        return "userPage/userInfo";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") long id) {
        UserAccount userAccount = userService.returnUserAccountByID(id);
        model.addAttribute("user", userAccount);
        model.addAttribute("status", Status.values());
        model.addAttribute("roles", roleService.returnAllRoles());
        return "userPage/editUser";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(HttpServletRequest request,
                             @ModelAttribute("user") UserAccount userAccount, BindingResult bindingResult,
                             @PathVariable("id") long id,
                             @RequestParam(value = "role") int roleID,
                             @RequestParam(value = "status", defaultValue = "INACTIVE") String status,
                             Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("user", userAccount);
        String er1 = validator.checkFieldUserNameToUpdate(request.getParameter("username"), userService.returnUserAccountByID(id).getUsername());
        String er2 = validator.checkFieldPassToUpdate(request.getParameter("password"), userAccount.getPassword());
        String er3 = validator.checkFieldName(request.getParameter("firstName"));
        String er4 = validator.checkFieldName(request.getParameter("firstName"));
        model.addAttribute("fieldNameError", validator.checkFieldUserNameToUpdate(request.getParameter("username"), userService.returnUserAccountByID(id).getUsername()));
        model.addAttribute("fieldPassError", validator.checkFieldPassToUpdate(request.getParameter("password"), userAccount.getPassword()));
        model.addAttribute("fieldFirstNameError", validator.checkFieldName(request.getParameter("firstName")));
        model.addAttribute("fieldLastNameError", validator.checkFieldName(request.getParameter("lastName")));
        if (!validator.checkUpdateUserFormValid(request.getParameter("username"), userService.returnUserAccountByID(id).getUsername(),
                request.getParameter("password"), userService.returnUserAccountByID(id).getPassword(),
                request.getParameter("firstName"), request.getParameter("lastName"))) {
            model.addAttribute("status", Status.values());
            model.addAttribute("roles", roleService.returnAllRoles());
            return "userPage/editUser";
        } else if (bindingResult.hasErrors()) {
            redirectAttributes.getFlashAttributes().clear();
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.manager", bindingResult);
            redirectAttributes.addFlashAttribute("user", userAccount);
            model.addAttribute("status", Status.values());
            model.addAttribute("roles", roleService.returnAllRoles());
            return "userPage/editUser";
        } else {
            userService.updateUserAccount(userAccount, roleID, status);
        }
        return "redirect:/user";
    }

}
