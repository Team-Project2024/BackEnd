package Hoseo.GraduationProject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * 특별한 경우가 아니라면 SimpleAsyncTaskExecutor 보다는
     * ThreadPoolTaskExecutor 같은 제한된 리소스를 사용하는 스레드풀을 사용하는 것이 좋다.
    */
    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 스레드 풀이 실행을 시작할 때 생성되는 스레드의 수를 정의, 스레드 풀의 기본 사이즈로 설정
        executor.setCorePoolSize(2);
        // 스레드 풀이 확장될 수 있는 스레드의 상한선을 설정, 스레드 풀이 관리할 수 있는 최대 스레드 수를 정의
        executor.setMaxPoolSize(10);
        // corePoolSize가 가득 찬 상태에서 추가 작업을 처리할 수 없을 때 대기하는 작업의 최대 개수를 정의, 스레드 풀의 작업 대기열 크기를 설정
        executor.setQueueCapacity(500);
        // 생성되는 스레드의 이름 접두사를 정의, 각 스레드의 이름이 GP Executor-1, GP Executor-2와 같이 설정됨
        executor.setThreadNamePrefix("GP Executor-");
        // 스레드 풀을 초기화
        executor.initialize();
        return executor;
    }
}