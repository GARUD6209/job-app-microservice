package com.garud.jobms.job;

import com.garud.jobms.job.dto.JobDTO;

import java.util.List;


public interface JobService {

    List<JobDTO> findAll();

    String create(Job job);

    JobDTO findJobById(Long id) ;

    Boolean deleteJobById(Long id);

    boolean updateJob(Long id, Job updateJob);
}
