package integration.me.kqlqk.behealthy_view.service;

import annotations.ServiceTest;
import me.kqlqk.behealthy.view.model.AuthInfo;
import me.kqlqk.behealthy.view.repository.AuthInfoRepository;
import me.kqlqk.behealthy.view.service.impl.AuthInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
public class AuthInfoServiceTest {
    @Autowired
    private AuthInfoServiceImpl authInfoService;

    @Autowired
    private AuthInfoRepository authInfoRepository;

    @Test
    public void saveOrUpdate_shouldSaveAuthInfo() {
        int oldSize = authInfoRepository.findAll().size();

        AuthInfo authInfo = new AuthInfo(3, "123.213.123.1", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo);

        int newSize = authInfoRepository.findAll().size();

        assertThat(oldSize).isNotEqualTo(newSize);
        assertThat(++oldSize).isEqualTo(newSize);
    }

    @Test
    public void saveOrUpdate_shouldUpdateAuthInfo() {
        AuthInfo authInfo = authInfoService.getByRemoteAddr("192.42.44.11");
        authInfo.setUserId(3);
        authInfoService.saveOrUpdate(authInfo);

        assertThat(authInfoService.getByRemoteAddr("192.42.44.11").getUserId()).isEqualTo(3);
    }

    @Test
    public void saveOrUpdate_shouldRemoveFirstLogInInfoAndAddNew() {
        AuthInfo authInfo = new AuthInfo(3, "123.213.123.1", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo);

        AuthInfo authInfo2 = new AuthInfo(3, "123.213.123.2", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo2);

        AuthInfo authInfo3 = new AuthInfo(3, "123.213.123.3", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo3);

        AuthInfo authInfo4 = new AuthInfo(3, "123.213.123.4", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo4);

        AuthInfo authInfo5 = new AuthInfo(3, "123.213.123.5", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo5);

        AuthInfo authInfo6 = new AuthInfo(3, "123.213.123.6", "some_access", "some_refresh");
        authInfoService.saveOrUpdate(authInfo6);

        assertThat(authInfoService.getByRemoteAddr("123.213.123.1")).isNull();
        assertThat(authInfoService.getByRemoteAddr("123.213.123.2")).isNotNull();
    }
}
