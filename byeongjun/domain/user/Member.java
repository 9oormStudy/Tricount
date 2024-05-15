package proj.tricount.domain.user;

import lombok.Data;

@Data
public class Member {
    private Long id;

    private String username;
    private String password;
    private String loginId;

    public Member() {
    }

    public Member(Long id, String username, String password, String loginId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.loginId = loginId;
    }
}
