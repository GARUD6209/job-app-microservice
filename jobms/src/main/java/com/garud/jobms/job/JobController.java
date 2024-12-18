package com.garud.jobms.job;

import com.garud.jobms.job.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/jobs")
public class JobController {
    private JobService jobService;




    public JobController(JobService jobService
                        ) {
        this.jobService = jobService;

    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> findAll() {
        List<JobDTO> jobDTOList = jobService.findAll();

        if (jobDTOList != null) {
            return new ResponseEntity<>(jobDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> findJobById(@PathVariable Long id) {
        JobDTO jobDTO = jobService.findJobById(id);

        if (jobDTO != null) {
            return new ResponseEntity<>(jobDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<String> createJob(@RequestBody Job job) {

        String createdMessage = jobService.create(job);

        if (createdMessage != null)
            return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updateJob) {

        boolean updateMessage = jobService.updateJob(id, updateJob);

        if (updateMessage) {
            return new ResponseEntity<String>("Job updated", HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteJobById(@PathVariable Long id) {
        boolean deleteMessage = jobService.deleteJobById(id);

        if (deleteMessage) return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
