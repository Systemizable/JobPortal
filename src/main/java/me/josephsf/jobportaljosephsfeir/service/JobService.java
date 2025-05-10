package me.josephsf.jobportaljosephsfeir.service;

import me.josephsf.jobportaljosephsfeir.dto.JobDto;
import me.josephsf.jobportaljosephsfeir.model.Job;
import me.josephsf.jobportaljosephsfeir.repository.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    public Job getJobById(String id) {
        return jobRepository.findById(id).orElse(null);
    }

    public Job createJob(JobDto jobDto) {
        Job job = new Job();
        mapDtoToJob(jobDto, job);
        job.setPostedDate(LocalDateTime.now());
        job.setIsActive(true);
        return jobRepository.save(job);
    }

    public Job updateJob(String id, JobDto jobDto) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            mapDtoToJob(jobDto, job);
            job.setUpdatedAt(LocalDateTime.now());
            return jobRepository.save(job);
        }
        return null;
    }

    public boolean deleteJob(String id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Job> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchJobs(keyword, pageable);
    }

    public List<Job> getJobsByCategory(String category) {
        return jobRepository.findByCategory(category);
    }

    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByLocation(location);
    }

    public List<Job> getJobsByRecruiter(String recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }

    public List<Job> getJobsByCompany(String companyName) {
        return jobRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }

    public List<Job> getJobsBySalaryRange(Double minSalary, Double maxSalary) {
        return jobRepository.findBySalaryRange(minSalary, maxSalary);
    }

    public Job toggleJobActive(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setIsActive(!job.getIsActive());
            job.setUpdatedAt(LocalDateTime.now());
            return jobRepository.save(job);
        }
        return null;
    }

    public List<Job> getActiveJobs() {
        return jobRepository.findByIsActive(true);
    }

    public long countJobsByRecruiter(String recruiterId) {
        return jobRepository.countByRecruiterIdAndIsActive(recruiterId, true);
    }

    private void mapDtoToJob(JobDto jobDto, Job job) {
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setCompanyName(jobDto.getCompanyName());
        job.setLocation(jobDto.getLocation());
        job.setCategory(jobDto.getCategory());
        job.setEmploymentType(jobDto.getEmploymentType());
        job.setSalary(jobDto.getSalary());
        job.setRecruiterId(jobDto.getRecruiterId());
        job.setRequirements(jobDto.getRequirements());
        job.setResponsibilities(jobDto.getResponsibilities());
        job.setDeadline(jobDto.getDeadline());
    }

}