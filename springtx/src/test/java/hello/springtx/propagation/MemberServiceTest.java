package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import java.rmi.UnexpectedException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;
    /**
     * MemberService @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON
     */
    @Test
    void outerTxOff_success() {
        String username = "outerTxOff_success";
        memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isPresent());

        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOff_fail() {
        String username = "로그예외_outerTxOff_fail";

        Assertions.assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        //memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isPresent());

        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * MemberService @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON
     */
    @Test
    void singleTx() {
        String username = "singleTx";
        memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isPresent());

        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON
     */
    @Test
    void outerTxOn_success() {
        String username = "outerTxOn_success";
        memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isPresent());

        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOn_fail() {
        String username = "로그예외_outerTxOn_fail";

        Assertions.assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        //memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isEmpty());

        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON Exception
     */
    @Test
    void recoverException_fail() {
        String username = "로그예외_recoverException_fail";

        Assertions.assertThatThrownBy(() -> memberService.joinV2(username)).isInstanceOf(UnexpectedRollbackException.class);

        //memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isEmpty());

        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON(Requires_New) Exception
     */
    @Test
    void recoverException_success() {
        String username = "로그예외_recoverException_success";

       memberService.joinV2(username);

        //memberService.joinV1(username);
        assertTrue(memberRepository.find(username).isPresent());

        assertTrue(logRepository.find(username).isEmpty());
    }
}