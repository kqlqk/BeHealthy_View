package me.kqlqk.behealthy.view.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "auth_info")
@Data
@NoArgsConstructor
public class AuthInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "remote_addr", unique = true, nullable = false, length = 100)
    private String remoteAddr;

    @Column(name = "access_token", nullable = false, length = 200)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 200)
    private String refreshToken;

    public AuthInfo(long userId, String remoteAddr, String accessToken, String refreshToken) {
        this.userId = userId;
        this.remoteAddr = remoteAddr;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
