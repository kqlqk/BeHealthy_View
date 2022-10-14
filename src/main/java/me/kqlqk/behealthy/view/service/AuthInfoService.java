package me.kqlqk.behealthy.view.service;

import me.kqlqk.behealthy.view.model.AuthInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthInfoService {
    AuthInfo getByRemoteAddr(String remoteAddr);

    List<AuthInfo> getByUserId(long userId);

    void saveOrUpdate(AuthInfo authInfo);

    void deleteByRemoteAddr(String remoteAddr);
}
