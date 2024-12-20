package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO handleGetCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta mt = new Meta();
        mt.setPage(pCompany.getNumber() + 1);
        mt.setPageSize(pCompany.getSize());
        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pCompany.getContent());

        return rs;
    }

    public Company handleUpdateCompany(Company reqCompany) {
        Company currentCompany = this.fetchCompanyById(reqCompany.getId());
        if (currentCompany != null) {
            boolean hasChange = false;

            if (reqCompany.getName() != null && !reqCompany.getName().isEmpty()
                    && !reqCompany.getName().equals(currentCompany.getName())) {
                currentCompany.setName(reqCompany.getName());
                hasChange = true;
            }

            if (reqCompany.getAddress() != null && !reqCompany.getAddress().isEmpty()
                    && !reqCompany.getAddress().equals(currentCompany.getAddress())) {
                currentCompany.setAddress(reqCompany.getAddress());
                hasChange = true;
            }

            if (reqCompany.getDescription() != null && !reqCompany.getDescription().isEmpty()
                    && !reqCompany.getDescription().equals(currentCompany.getDescription())) {
                currentCompany.setDescription(reqCompany.getDescription());
                hasChange = true;
            }

            if (reqCompany.getLogo() != null && !reqCompany.getLogo().isEmpty()
                    && !reqCompany.getLogo().equals(currentCompany.getLogo())) {
                currentCompany.setLogo(reqCompany.getLogo());
                hasChange = true;
            }

            if (hasChange) {
                currentCompany.setUpdatedAt(reqCompany.getUpdatedAt());
                currentCompany.setUpdatedBy(reqCompany.getUpdatedBy());
                currentCompany = this.companyRepository.save(currentCompany);
            }
        }
        return currentCompany;
    }

    public Company fetchCompanyById(long id) {
        return this.companyRepository.findById(id).isPresent() ? this.companyRepository.findById(id).get() : null;
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }

}
