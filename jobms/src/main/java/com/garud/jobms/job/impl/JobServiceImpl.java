package com.garud.jobms.job.impl;


import com.garud.jobms.job.Job;
import com.garud.jobms.job.JobRepository;
import com.garud.jobms.job.JobService;
import com.garud.jobms.job.clients.CompanyClient;
import com.garud.jobms.job.clients.ReviewClient;
import com.garud.jobms.job.dto.JobDTO;
import com.garud.jobms.job.external.Company;
import com.garud.jobms.job.external.Review;
import com.garud.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
//    List<Job> jobs = new ArrayList<>();

    int attempt = 0;

    private final JobRepository jobRepository;


    private final RestTemplate restTemplate;

    private final CompanyClient companyClient;

    private final ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository,
                          RestTemplate restTemplate,
                          CompanyClient companyClient,
                          ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

//    private Long nextId= 1L;

    @Override
//    @CircuitBreaker(name="companyBreaker" ,
//            fallbackMethod = "companyBreakerFallback")
//    @Retry(name="companyBreaker" ,
//           fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name="companyRateLimiter" ,
            fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt : " + ++attempt);
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job) {
        Company company = companyClient.getCompany(job.getCompanyId());

        attempt = 0;

        List<Review> reviewList = reviewClient.getReviews(job.getCompanyId());

        return JobMapper.mapToJobWithCompanyDTO(job,company,reviewList);
    }

    @Override
    public String create(Job job) {

      try {

          jobRepository.save(job);
          return "Job created successfully." ;
      }catch (Exception ex){
          return "job not created.";
      }
    }

    @Override
    public JobDTO findJobById(Long id)  {

        Job job = jobRepository.findById(id).orElse(null);

        return convertToDto(job);

    }

   public List<String> companyBreakerFallback(Exception e){
      List<String> list = new ArrayList<>();
      list.add("Dummy");
      return list;
    }

    @Override
    public Boolean deleteJobById(Long id) {

        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }

        return false;



    }

    @Override
    public boolean updateJob(Long id, Job updateJob) {

        Optional<Job> jobOptional = jobRepository.findById(id);


            if (jobOptional.isPresent()) {
                Job job = jobOptional.get();

                job.setDescription(updateJob.getDescription());
                job.setTitle(updateJob.getTitle());
                job.setMinSalary(updateJob.getMinSalary());
                job.setMaxSalary(updateJob.getMaxSalary());
                job.setLocation(updateJob.getLocation());
                jobRepository.save(job);
                return true;
            }


        return false;
    }
}
