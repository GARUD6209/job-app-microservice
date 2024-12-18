package com.garud.companyms.company.impl;


import com.garud.companyms.company.Company;
import com.garud.companyms.company.CompanyRepository;
import com.garud.companyms.company.CompanyService;
import com.garud.companyms.company.clients.ReviewClient;
import com.garud.companyms.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> findAllCompanies() {


        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {

        try {
            Optional<Company> companyOptional = companyRepository.findById(id);
            if (companyOptional.isPresent()) {
                Company company = companyOptional.get();

                return company;
            }
        } catch (Exception e) {
            return new Company();
        }
        return null;
    }

    @Override
    public Company create(Company company) {

        return companyRepository.save(company);
    }

    @Override
    public boolean updateCompany(Long id, Company updateCompany) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setName(updateCompany.getName());

            company.setDescription(updateCompany.getDescription());
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCompanyById(Long id) {

        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {

        System.out.println(reviewMessage.getDescription());

        Company company =companyRepository.findById(reviewMessage.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not fount" + reviewMessage.getCompanyId()));


        double averageRating = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRepository.save(company);


    }
}
