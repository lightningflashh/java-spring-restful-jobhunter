package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtils;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {
        if (subscriber.getEmail() == null || subscriber.getEmail().isEmpty()) {
            throw new IdInvalidException("Email is required");
        }

        if (subscriber.getName() == null || subscriber.getName().isEmpty()) {
            throw new IdInvalidException("Name is required");
        }

        if (this.subscriberService.isExistEmail(subscriber.getEmail())) {
            throw new IdInvalidException("Email is already exist");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.subscriberService.handleCreateSubscriber(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subscriber)
            throws IdInvalidException {
        Optional<Subscriber> currentSubscriber = this.subscriberService.findSubscriberById(subscriber.getId());
        if (!currentSubscriber.isPresent()) {
            throw new IdInvalidException("Subscriber not found");
        }
        return ResponseEntity.ok()
                .body(this.subscriberService.handleUpdateSubscriber(subscriber, currentSubscriber.get()));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill()
            throws IdInvalidException {
        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        return ResponseEntity.ok()
                .body(this.subscriberService.findByEmail(email));
    }

}
