package com.example._251022_spring_practice_basicbatch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job helloWorldJob;

    @PostMapping("/hello")
    public Map<String, Object> runHelloWorldBatch() {
        Map<String, Object> response = new HashMap<>();
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestTime", LocalDateTime.now().toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            var jobExecution = jobLauncher.run(helloWorldJob, jobParameters);
            response.put("status", "SUCCESS");
            response.put("message", "Hello World 배치가 실행되었습니다.");
            response.put("jobExecutionId", jobExecution.getId());
            response.put("jobStatus", jobExecution.getStatus().name());
            log.info("Hello 배치 실행 완료: {}", jobExecution.getId());
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "배치 실행 중 오류가 발생했습니다: " + e.getMessage());
            log.error("배치 실행 오류", e);
        }
        return response;
    }
}
