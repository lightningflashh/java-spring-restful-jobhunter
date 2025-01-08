package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Email sent successfully")
    public String sendEmail() {
        // this.emailService.sendEmailSync("thanhnguyenql2904@gmail.com",
        // "Testing from Spring Boot",
        // "<h1> <b> Hello World from Spring Boot Email </b> </h1>",
        // false, true);
        this.emailService.sendEmailFromTemplateSync("thanhnguyenql2904@gmail.com",
                "Testing send email from template", "job");
        return "Email sent successfully";
    }

}
