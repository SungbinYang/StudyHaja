package com.studyhaja.controller.settings;

import com.studyhaja.annotation.CurrentUser;
import com.studyhaja.domain.account.Account;
import com.studyhaja.domain.settings.PasswordForm;
import com.studyhaja.domain.settings.PasswordFormValidator;
import com.studyhaja.domain.settings.Profile;
import com.studyhaja.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * packageName : com.studyhaja.controller.settings
 * fileName : SettingsController
 * author : rovert
 * date : 2022/03/06
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/06       rovert         최초 생성
 */

@Controller
@RequiredArgsConstructor
public class SettingsController {

    static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";

    static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";

    private final AccountService accountService;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @GetMapping("/" + SETTINGS_PROFILE_VIEW_NAME)
    public String updateProfileForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new Profile(account));

        return SETTINGS_PROFILE_VIEW_NAME;
    }

    @PostMapping("/" + SETTINGS_PROFILE_VIEW_NAME)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors, Model model,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }

        accountService.updateProfile(account, profile);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다.");

        return "redirect:/" + SETTINGS_PROFILE_VIEW_NAME;
    }

    @GetMapping("/" + SETTINGS_PASSWORD_VIEW_NAME)
    public String updatePasswordForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());

        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    @PostMapping("/" + SETTINGS_PASSWORD_VIEW_NAME)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, Model model,
                                 RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_PASSWORD_VIEW_NAME;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message", "패스워드를 변경했습니다.");

        return "redirect:/" + SETTINGS_PASSWORD_VIEW_NAME;
    }
}
