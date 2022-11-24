package integration.me.kqlqk.behealthy_view.repository;

import annotations.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
public class AuthInfoRepositoryTest {
    @Autowired
    private AuthInfoRepository authInfoRepository;

    @Test
    public void findByRemoteAddr_shouldFindByRemoteAddrOrReturnNull() {
        AuthInfo authInfo = authInfoRepository.findByRemoteAddr("192.42.44.11");

        assertThat(authInfo).isNotNull();
        assertThat(authInfo.getUserId()).isEqualTo(1);

        AuthInfo nullAuthInfo = authInfoRepository.findByRemoteAddr("random");
        assertThat(nullAuthInfo).isNull();
    }

    @Test
    public void findByUserId_shouldFindByUserIdOrReturnEmptyMap() {
        List<AuthInfo> authInfos = authInfoRepository.findByUserId(1);

        assertThat(authInfos).isNotEmpty();
        assertThat(authInfos).hasSize(1);
        assertThat(authInfos.get(0).getRemoteAddr()).isEqualTo("192.42.44.11");

        List<AuthInfo> nullAuthInfo = authInfoRepository.findByUserId(99);
        assertThat(nullAuthInfo).isEmpty();
    }
}
