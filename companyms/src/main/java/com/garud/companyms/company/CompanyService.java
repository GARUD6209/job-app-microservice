package com.garud.companyms.company;

import com.garud.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<Company> findAllCompanies();

    Company getCompanyById(Long id) ;

    Company create(Company company);

    boolean updateCompany(Long id, Company updateCompany);

    boolean deleteCompanyById(Long id);

    void updateCompanyRating(ReviewMessage reviewMessage);
}
