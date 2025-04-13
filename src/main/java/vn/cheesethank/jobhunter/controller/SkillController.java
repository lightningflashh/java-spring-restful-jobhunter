package vn.cheesethank.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.cheesethank.jobhunter.domain.Skill;
import vn.cheesethank.jobhunter.domain.response.ResultPaginationDTO;
import vn.cheesethank.jobhunter.service.SkillService;
import vn.cheesethank.jobhunter.util.annotation.ApiMessage;
import vn.cheesethank.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (this.skillService.isSkillExist(skill.getName())) {
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }
        Skill newSkill = this.skillService.handleCreateSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(skill.getId());

        // check id
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + skill.getId() + " không tồn tại");
        }
        // check name
        if (skill.getName() != null && this.skillService.isNameExist(skill.getName())) {
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }

        return ResponseEntity.ok(currentSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.skillService.fetchAllSkill(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(id);

        // check id
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id = " + id + " không tồn tại");
        }

        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

}
