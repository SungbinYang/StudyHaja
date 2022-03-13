package com.studyhaja.controller.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyhaja.annotation.CurrentAccount;
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
import com.studyhaja.domain.zone.form.Zone;
import com.studyhaja.domain.zone.form.ZoneForm;
import com.studyhaja.repository.tag.TagRepository;
import com.studyhaja.repository.zone.ZoneRepository;
import com.studyhaja.service.account.AccountService;
import com.studyhaja.service.tag.TagService;
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

import static com.studyhaja.controller.settings.SettingsController.ROOT;
import static com.studyhaja.controller.settings.SettingsController.SETTINGS;

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
@RequestMapping(ROOT + SETTINGS)
@RequiredArgsConstructor
public class SettingsController extends UiUtils {

    static final String ROOT = "/";

    static final String SETTINGS = "settings";

    static final String PROFILE = "/profile";

    static final String PASSWORD = "/password";

    static final String NOTIFICATIONS = "/notifications";

    static final String ACCOUNT = "/account";

    static final String TAGS = "/tags";

    static final String ZONES = "/zones";

    private final AccountService accountService;

    private final ModelMapper modelMapper;

    private final NicknameValidator nicknameValidator;

    private final TagRepository tagRepository;

    private final ObjectMapper objectMapper;

    private final ZoneRepository zoneRepository;

    private final TagService tagService;

    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping(PROFILE)
    public String updateProfileForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));

        return SETTINGS + PROFILE;
    }

    @PostMapping(PROFILE)
    public String updateProfile(@CurrentAccount Account account, @Valid Profile profile, Errors errors, Model model,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PROFILE;
        }

        accountService.updateProfile(account, profile);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정했습니다.");

        return "redirect:/" + SETTINGS + PROFILE;
    }

    @GetMapping(PASSWORD)
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());

        return SETTINGS + PASSWORD;
    }

    @PostMapping(PASSWORD)
    public String updatePassword(@CurrentAccount Account account, @Valid PasswordForm passwordForm, Errors errors, Model model,
                                 RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS + PASSWORD;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message", "패스워드를 변경했습니다.");

        return "redirect:/" + SETTINGS + PASSWORD;
    }

    @GetMapping(NOTIFICATIONS)
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));

        return SETTINGS + NOTIFICATIONS;
    }

    @PostMapping(NOTIFICATIONS)
    public String updateNotifications(@CurrentAccount Account account, @Valid Notifications notifications, Errors errors, Model model,
                                      RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS + NOTIFICATIONS;
        }

        accountService.updateNotifications(account, notifications);
        redirectAttributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");

        return "redirect:/" + SETTINGS + NOTIFICATIONS;
    }

    @GetMapping(ACCOUNT)
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));

        return SETTINGS + ACCOUNT;
    }

    @PostMapping(ACCOUNT)
    public String updateAccount(@CurrentAccount Account account, @Valid NicknameForm nicknameForm, Errors errors, Model model,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);

            return SETTINGS + ACCOUNT;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
        redirectAttributes.addFlashAttribute("message", "닉네임을 수정했습니다.");

        return "redirect:/" + SETTINGS + ACCOUNT;
    }

    @PostMapping(ACCOUNT + "/delete")
    public String deleteAccount(@CurrentAccount Account account, Model model) {
        if (account.getNickname() == null) {
            model.addAttribute(account);
            return "/" + SETTINGS + ACCOUNT;
        }

        accountService.deleteAccount(account.getNickname());
        SecurityContextHolder.clearContext();

        return showMessageWithRedirect("정상적으로 회원탈퇴 처리가 완료되었습니다.", "/", Method.GET, null, model);
    }

    @GetMapping(TAGS)
    public String updateTags(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        Set<Tag> tags = accountService.getTags(account);
        model.addAttribute("tags", tags.stream().map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).toList();
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

        return SETTINGS + TAGS;
    }

    @ResponseBody
    @PostMapping(TAGS + "/add")
    public ResponseEntity addTags(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        accountService.addTag(account, tag);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping(TAGS + "/remove")
    public ResponseEntity removeTags(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title);

        if (tag == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeTag(account, tag);

        return ResponseEntity.ok().build();
    }

    @GetMapping(ZONES)
    public String updateZonesForm(@CurrentAccount Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);

        Set<Zone> zones = accountService.getZones(account);
        model.addAttribute("zones", zones.stream().map(Zone::toString).collect(Collectors.toList()));

        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString).toList();
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

        return SETTINGS + ZONES;
    }

    @ResponseBody
    @PostMapping(ZONES + "/add")
    public ResponseEntity addZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());

        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping(ZONES + "/remove")
    public ResponseEntity removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());

        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);

        return ResponseEntity.ok().build();
    }
}
