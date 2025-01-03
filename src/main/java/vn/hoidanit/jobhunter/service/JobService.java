package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository,
            SkillRepository skillRepository, CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResCreateJobDTO handleCreateJob(Job job) {
        checkJobSkills(job, null);
        checkCompany(job, null);
        Job currentJob = this.jobRepository.save(job);
        return convertToResCreateJobDTO(currentJob);
    }

    public ResUpdateJobDTO handleUpdateJob(Job job, Job dbJob) {
        checkJobSkills(job, dbJob);
        checkCompany(job, dbJob);

        // update
        dbJob.setName(job.getName());
        dbJob.setSalary(job.getSalary());
        dbJob.setQuantity(job.getQuantity());
        dbJob.setLocation(job.getLocation());
        dbJob.setLevel(job.getLevel());
        dbJob.setStartDate(job.getStartDate());
        dbJob.setEndDate(job.getEndDate());
        dbJob.setActive(job.isActive());

        Job currentJob = this.jobRepository.save(dbJob);

        return convertToResUpdateJobDTO(currentJob);
    }

    private void checkCompany(Job job, Job dbJob) {
        if (job.getCompany() != null) {
            Optional<Company> company = this.companyRepository.findById(job.getCompany().getId());
            if (company.isPresent()) {
                if (dbJob != null) {
                    dbJob.setCompany(company.get()); // update company
                } else {
                    job.setCompany(company.get()); // create company
                }
            }
        }
    }

    private void checkJobSkills(Job job, Job dbJob) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(Skill::getId)
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            if (dbJob != null) {
                dbJob.setSkills(dbSkills); // update skills
            } else {
                job.setSkills(dbSkills); // create skills
            }
        }
    }

    private ResCreateJobDTO convertToResCreateJobDTO(Job job) {
        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setSalary(job.getSalary());
        dto.setQuantity(job.getQuantity());
        dto.setLocation(job.getLocation());
        dto.setLevel(job.getLevel());
        dto.setStartDate(job.getStartDate());
        dto.setEndDate(job.getEndDate());
        dto.setActive(job.isActive());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setCreatedBy(job.getCreatedBy());
        if (job.getSkills() != null) {
            List<String> skills = job.getSkills()
                    .stream().map(Skill::getName)
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    private ResUpdateJobDTO convertToResUpdateJobDTO(Job job) {
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setSalary(job.getSalary());
        dto.setQuantity(job.getQuantity());
        dto.setLocation(job.getLocation());
        dto.setLevel(job.getLevel());
        dto.setStartDate(job.getStartDate());
        dto.setEndDate(job.getEndDate());
        dto.setActive(job.isActive());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setUpdatedBy(job.getUpdatedBy());
        if (job.getSkills() != null) {
            List<String> skills = job.getSkills()
                    .stream().map(Skill::getName)
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    public Optional<Job> fetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResultPaginationDTO fetchAll(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();

        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());

        return rs;
    }

}