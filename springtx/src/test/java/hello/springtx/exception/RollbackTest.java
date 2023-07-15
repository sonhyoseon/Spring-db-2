package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RollbackTest {

    @Autowired
    static RollbackService service;

    @Test
    void runtimeException() {
        Assertions.assertThatThrownBy(() ->  service.runtimeException()).isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        Assertions.assertThatThrownBy(() ->  service.checkException()).isInstanceOf(MyException .class);
    }

    @TestConfiguration
    static class RollbackTestConfig{
        @Bean
        RollbackService rollbackService() {
            return new RollbackService();
        }
    }
    @Slf4j
    static class RollbackService{
        // 런타임 예외 발생: 롤백
        @Transactional
        public void runtimeException() {
            log.info("Runtime exception");
            throw new RuntimeException();
        }

        // 체크 예외 발생: 커밋
        @Transactional
        public void checkException() throws MyException {
            log.info("Check exception");
            throw new MyException();
        }

        //체크 예외 rollbackFor 지정: 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("Rollback for");
            throw new MyException();
        }
    }

    static class MyException extends Exception {

    }
}
