package me.kqlqk.behealthy.view.repository;

import me.kqlqk.behealthy.view.model.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {
    AuthInfo findByRemoteAddr(String remoteAddr);

    List<AuthInfo> findByUserId(long userId);
}
