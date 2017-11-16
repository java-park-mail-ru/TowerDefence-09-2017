package com.td.domain;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;


    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private OffsetDateTime regDate;

    @OneToMany(mappedBy = "user")
    private List<Progress> progress;

    @OneToMany(mappedBy = "owner")
    private List<Score> scores;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private GameProfile profile;

    public User() {
        this.nickname = "";
        this.password = "";
        this.email = "";
        this.id = 0L;
        this.regDate = OffsetDateTime.now();
    }

    private String hashPassword(String somePassword) {
        return BCrypt.hashpw(somePassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String outerPassword) {
        return BCrypt.checkpw(outerPassword, this.password);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void updateEmail(String newEmail) {
        if (newEmail != null) {
            setEmail(newEmail);
        }
    }

    public void updateNickname(String newNickname) {
        if (newNickname != null) {
            setNickname(newNickname);
        }
    }

    public void updatePassword(String newPassword) {
        if (newPassword != null) {
            setPassword(newPassword);
        }
    }

    public OffsetDateTime getRegDate() {
        return regDate;
    }

    public void setRegDate(OffsetDateTime regDate) {
        this.regDate = regDate;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public void setProfile(GameProfile profile) {
        profile.setUser(this);
        this.profile = profile;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}