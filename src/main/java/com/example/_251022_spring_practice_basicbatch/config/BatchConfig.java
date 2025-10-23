package com.example._251022_spring_practice_basicbatch.config;

import com.example._251022_spring_practice_basicbatch.model.Message;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job helloWorldJob() {
        return new JobBuilder("helloWorldJob",  jobRepository)
                .start(helloWorldStep())
                .build();
    }

    @Bean
    public Step helloWorldStep() {
        return new StepBuilder("helloWorldStep", jobRepository)
                .<Message, Message>chunk(3, transactionManager)
                .reader(messageItemReader())
                .processor(messageItemProcessor())
                .writer(messageItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Message> messageItemReader() {
        return new JdbcPagingItemReaderBuilder<Message>()
                .name("messageItemReader")
                .dataSource(dataSource)
                .pageSize(3)
                .selectClause("SELECT id, content, author, processed, processed_content")
                .fromClause("FROM messages")
                .whereClause("WHERE processed = false")
                .sortKeys(Map.of("id", Order.ASCENDING))
                .rowMapper(new BeanPropertyRowMapper<>(Message.class))
                .build();
    }

    @Bean
    public ItemProcessor<Message, Message> messageItemProcessor() {
        return message -> {
            log.info("처리 중인 메시지: {}", message.getContent());
            String processedContent = "Hello, World! " + message.getContent() + " (processed by " + message.getAuthor() + ")";
            message.setProcessedContent(processedContent);
            message.setProcessed(true);
            log.info("처리 완료: {}", processedContent);
            return message;
        };
    }

    @Bean
    public ItemWriter<Message> messageItemWriter() {
        JpaItemWriter<Message> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
