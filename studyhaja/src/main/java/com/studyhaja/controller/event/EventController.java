package com.studyhaja.controller.event;

import com.studyhaja.annotation.CurrentAccount;
import com.studyhaja.domain.account.form.Account;
import com.studyhaja.domain.event.form.Event;
import com.studyhaja.domain.event.form.EventForm;
import com.studyhaja.domain.event.validator.EventValidator;
import com.studyhaja.domain.study.form.Study;
import com.studyhaja.repository.event.EventRepository;
import com.studyhaja.service.event.EventService;
import com.studyhaja.service.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * packageName : com.studyhaja.controller.event
 * fileName : EventController
 * author : rovert
 * date : 2022/03/14
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/14       rovert         최초 생성
 */

@Controller
@RequestMapping("/study/{path}")
@RequiredArgsConstructor
public class EventController {

    private final StudyService studyService;

    private final EventService eventService;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    private final EventRepository eventRepository;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(eventValidator);
    }

    @GetMapping("/new-event")
    public String newEventForm(@CurrentAccount Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute(new EventForm());

        return "event/form";
    }

    @PostMapping("/new-event")
    public String newEventSubmit(@CurrentAccount Account account, @PathVariable String path, @Valid EventForm eventForm,
                                 Errors errors, Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(study);

            return "event/form";
        }

        Event event = eventService.createEvent(modelMapper.map(eventForm, Event.class), study, account);

        return "redirect:/study/" + study.getEncodePath() + "/events/" + event.getId();
    }

    @GetMapping("/events/{id}")
    public String getEvent(@CurrentAccount Account account, @PathVariable String path, @PathVariable Long id, Model model) {
        model.addAttribute(account);
        model.addAttribute(eventRepository.findById(id).orElseThrow());
        model.addAttribute(studyService.getStudy(path));

        return "event/view";
    }
}
