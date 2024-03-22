package Hoseo.GraduationProject.Security.Redis;

import jakarta.persistence.Id;

public class RefreshToken {

    @Id
    private String refreshToken;

    private String userId;

    public RefreshToken(final String refreshToken, final String userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getMemberId() {
        return userId;
    }
}
