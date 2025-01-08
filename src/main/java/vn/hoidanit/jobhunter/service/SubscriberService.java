package vn.hoidanit.jobhunter.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Optional<Subscriber> findSubscriberById(Long id) {
        return this.subscriberRepository.findById(id);
    }

    private void checkValidSkills(Subscriber subscriber, Subscriber dbSubscriber) {
        if (subscriber.getSkills() != null) {
            List<Long> reqSkills = subscriber.getSkills()
                    .stream().map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            if (dbSubscriber != null) {
                dbSubscriber.setSkills(dbSkills); // update skills
            } else {
                subscriber.setSkills(dbSkills); // create skills
            }
        }
    }

    public Subscriber handleCreateSubscriber(Subscriber subscriber) {
        checkValidSkills(subscriber, null);
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriber, Subscriber dbSubscriber) {
        checkValidSkills(subscriber, dbSubscriber);
        return this.subscriberRepository.save(dbSubscriber);
    }

}
