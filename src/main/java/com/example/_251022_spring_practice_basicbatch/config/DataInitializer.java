package com.example._251022_spring_practice_basicbatch.config;

import com.example._251022_spring_practice_basicbatch.model.Message;
import com.example._251022_spring_practice_basicbatch.repository.MessageRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final MessageRepository messageRepository;

    @Bean
    public CommandLineRunner initializeTestData() {
        return args -> {
            initializeMessages();
        };
    }

    public void initializeMessages() {
        log.info("Hello Batch 테스트 데이터 초기화 시작");
        String[] messages = {
                "Spring Batch를 배워 봅시다!",
                "ItemReader가 데이터를 읽습니다.",
                "ItemProcessor가 데이터를 가공합니다.",
                "ItemWriter가 결과를 저장합니다.",
                "배치 처리는 정말 강력합니다.",
                "대용량 데이터도 문제 없어요.",
                "청크 단위로 안전하게 처리됩니다."
        };
        String[] authors = {"개발자A", "개발자B", "개발자C", "개발자D", "개발자E", "개발자F", "개발자G"};
        for (int i = 0; i < messages.length; i++) {
            Message message = Message.builder()
                    .content(messages[i])
                    .author(authors[i])
                    .processed(false)
                    .build();
            messageRepository.save(message);
        }
        log.info("테스트 데이터 초기화 완료: {} 건의 메시지 생성", messages.length);
    }
}
