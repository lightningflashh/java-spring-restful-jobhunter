package vn.cheesethank.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.cheesethank.jobhunter.service.EmailService;
import vn.cheesethank.jobhunter.service.SubscriberService;
import vn.cheesethank.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Email sent successfully")
    // @Scheduled(cron = "*/30 * * * * *")
    // @Transactional
    public String sendEmail() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "Email sent successfully";
    }

}
