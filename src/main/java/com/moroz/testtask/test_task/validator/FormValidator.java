package com.moroz.testtask.test_task.validator;

import com.moroz.testtask.test_task.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FormValidator {
    private UserService userService;
    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String checkFieldName(String parameter) {
        String errorMessage = null;
        String regex = "[a-zA-Z]{3,16}";
        if (parameter.equals("") || parameter == null) {
            errorMessage = messageSource.getMessage("error.emptyUserName", new Object[]{"error.emptyUserName"}, LocaleContextHolder.getLocale());
        } else if (parameter.toCharArray().length <= 2 || parameter.toCharArray().length >= 16) {
            errorMessage = messageSource.getMessage("error.sizeUserName", new Object[]{"error.sizeUserName"}, LocaleContextHolder.getLocale());
        } else if (!parameter.matches(regex)){
            errorMessage = messageSource.getMessage("error.wrongCharacters", new Object[]{"error.wrongCharacters"}, LocaleContextHolder.getLocale());
        }
        return errorMessage;
    }
    public String checkFieldUserName(String parameter) {
        if (userService.isUsernameAlreadyInUse(parameter)) {
            return messageSource.getMessage("duplicate.form.name", new Object[]{"duplicate.form.name"}, LocaleContextHolder.getLocale());
        }
        return checkFieldName(parameter);
    }
    public String checkFieldUserNameToUpdate(String parameter, String parameter2) {
        if (parameter.equals(parameter2)){
            return null;
        } else {
            return checkFieldUserName(parameter);
        }
    }

    public String checkFieldPass(String parameter) {
        String errorMessage = null;
        String regex ="(?=(.*\\d){1})(?=(.*[a-zA-Z]){1}).[a-zA-Z0-9]*";
        if (parameter.equals("") || parameter == null) {
            errorMessage = messageSource.getMessage("error.emptyUserPass", new Object[]{"error.emptyUserPass"}, LocaleContextHolder.getLocale());
        } else if (parameter.toCharArray().length <= 2 || parameter.toCharArray().length >= 17) {
            errorMessage = messageSource.getMessage("error.sizeUserPass", new Object[]{"error.sizeUserPass"}, LocaleContextHolder.getLocale());
        } else if (!parameter.matches(regex)){
            errorMessage = messageSource.getMessage("error.wrongCharactersInUserPass", new Object[]{"error.wrongCharactersInUserPass"}, LocaleContextHolder.getLocale());
        }
        return errorMessage;
    }

    public String checkFieldPassToUpdate(String parameter, String parametr2) {
        String errorMessage = null;
        if (parameter.equals(parametr2)){
            return null;
        } else{
            checkFieldPass(parameter);
        }
        return null;
    }

    public boolean checkAddUserFormValid(String errorUsername, String errorPassword, String errorFirstname, String errorLastname) {
        boolean isValid;
        errorUsername=checkFieldUserName(errorUsername);
        errorPassword=checkFieldPass(errorPassword);
        errorFirstname=checkFieldName(errorFirstname);
        errorLastname=checkFieldName(errorLastname);

        if ((errorUsername == null || errorUsername.equals("")) && (errorPassword == null || errorPassword.equals(""))
                && (errorFirstname == null || errorFirstname.equals("")) && (errorLastname == null || errorLastname.equals(""))) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    public boolean checkUpdateUserFormValid(String errorUsername, String currentUsername, String errorPassword, String currentPass, String errorFirstname, String errorLastname) {
        boolean isValid;
        errorUsername=checkFieldUserNameToUpdate(errorUsername, currentUsername);
        errorPassword=checkFieldPassToUpdate(errorPassword, currentPass);
        errorFirstname=checkFieldName(errorFirstname);
        errorLastname=checkFieldName(errorLastname);
        if ((errorUsername == null || errorUsername.equals("")) && (errorPassword == null || errorPassword.equals(""))
                && (errorFirstname == null || errorFirstname.equals("")) && (errorLastname == null || errorLastname.equals(""))) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }
}
