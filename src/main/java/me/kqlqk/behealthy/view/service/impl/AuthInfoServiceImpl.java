package me.kqlqk.behealthy.view.service.impl;

import lombok.NonNull;
import me.kqlqk.behealthy.view.model.AuthInfo;
import me.kqlqk.behealthy.view.repository.AuthInfoRepository;
import me.kqlqk.behealthy.view.service.AuthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthInfoServiceImpl implements AuthInfoService {
    private final AuthInfoRepository authInfoRepository;

    @Autowired
    public AuthInfoServiceImpl(AuthInfoRepository authInfoRepository) {
        this.authInfoRepository = authInfoRepository;
    }

    @Override
    public AuthInfo getByRemoteAddr(String remoteAddr) {
        return authInfoRepository.findByRemoteAddr(remoteAddr);
    }

    @Override
    public List<AuthInfo> getByUserId(long userId) {
        return authInfoRepository.findByUserId(userId);
    }

    @Override
    public void saveOrUpdate(@NonNull AuthInfo authInfo) {
        if (getByUserId(authInfo.getUserId()).size() > 4) {
            List<AuthInfo> authInfoList = getByUserId(authInfo.getUserId())
                    .stream()
                    .sorted(Comparator.comparingInt(e -> (int) e.getId()))
                    .collect(Collectors.toList());

            authInfoRepository.delete(authInfoList.get(0));
        }
        authInfoRepository.save(authInfo);
    }

    @Override
    public void deleteByRemoteAddr(@NonNull String remoteAddr) {
        AuthInfo authInfo = getByRemoteAddr(remoteAddr);

        if (authInfo == null) {
            return;
        }

        authInfoRepository.delete(authInfo);
    }
}
