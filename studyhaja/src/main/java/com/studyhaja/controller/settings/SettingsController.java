package com.studyhaja.controller.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyhaja.annotation.CurrentUser;
import com.studyhaja.common.Method;
import com.studyhaja.common.UiUtils;
import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.settings.form.NicknameForm;
import com.studyhaja.domain.settings.form.Notifications;
import com.studyhaja.domain.settings.form.PasswordForm;
import com.studyhaja.domain.settings.form.Profile;
import com.studyhaja.domain.settings.validator.NicknameValidator;
import com.studyhaja.domain.settings.validator.PasswordFormValidator;
import com.studyhaja.domain.tag.form.Tag;
import com.studyhaja.domain.tag.form.TagForm;
import com.studyhaja.repository.tag.TagRepository;
import com.studyhaja.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
public class SettingsController extends UiUtils {

    static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";

    static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";

    static final String SETTINGS_NOTIFICATIONS_VIEW_NAME = "settings/notifications";

    static final String SETTINGS_ACCOUNT_VIEW_NAME = "settings/account";

    static final String SETTINGS_TAGS_VIEW_NAME = "settings/tags";

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    private final NicknameValidator nicknameValidator;

    private final TagRepository tagRepository;

    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping("/" + SETTINGS_PROFILE_VIEW_NAME)
    public String updateProfileForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));

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

    @GetMapping("/" + SETTINGS_NOTIFICATIONS_VIEW_NAME)
    public String updateNotificationsForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));

        return SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    @PostMapping("/" + SETTINGS_NOTIFICATIONS_VIEW_NAME)
    public String updateNotifications(@CurrentUser Account account, @Valid Notifications notifications, Errors errors, Model model,
                                      RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_NOTIFICATIONS_VIEW_NAME;
        }

        accountService.updateNotifications(account, notifications);
        redirectAttributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");

        return "redirect:/" + SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    @GetMapping("/" + SETTINGS_ACCOUNT_VIEW_NAME)
    public String updateAccountForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));

        return SETTINGS_ACCOUNT_VIEW_NAME;
    }

    @PostMapping("/" + SETTINGS_ACCOUNT_VIEW_NAME)
    public String updateAccount(@CurrentUser Account account, @Valid NicknameForm nicknameForm, Errors errors, Model model,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS_ACCOUNT_VIEW_NAME;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
        redirectAttributes.addFlashAttribute("message", "닉네임을 수정했습니다.");

        return "redirect:/" + SETTINGS_ACCOUNT_VIEW_NAME;
    }

    @PostMapping("/" + SETTINGS_ACCOUNT_VIEW_NAME + "/delete")
    public String deleteAccount(@CurrentUser Account account, Model model) {
        if (account.getNickname() == null) {
            model.addAttribute(account);
            return "/" + SETTINGS_ACCOUNT_VIEW_NAME;
        }

        accountService.deleteAccount(account.getNickname());
        SecurityContextHolder.clearContext();

        return showMessageWithRedirect("정상적으로 회원탈퇴 처리가 완료되었습니다.", "/", Method.GET, null, model);
    }

    @GetMapping("/" + SETTINGS_TAGS_VIEW_NAME)
    public String updateTags(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).toList();
        model.addAttribute("whiteList", objectMapper.writeValueAsString(allTags));

        return SETTINGS_TAGS_VIEW_NAME;
    }

    @ResponseBody
    @PostMapping("/" + SETTINGS_TAGS_VIEW_NAME + "/add")
    public ResponseEntity addTags(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);

        if (tag == null) {
            tag = tagRepository.save(Tag.builder().title(tagForm.getTagTitle()).build());
        }

        accountService.addTag(account, tag);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/" + SETTINGS_TAGS_VIEW_NAME + "/remove")
    public ResponseEntity removeTags(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);

        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag);

        return ResponseEntity.ok().build();
    }
}
