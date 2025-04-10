package org.haruni.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.item.entity.Item;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(hidden = true)
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = true, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(name = "alarm_active")
    private Boolean alarmActive;

    @Column(name = "alarm_active_time")
    private String alarmActiveTime;

    @Column(name = "haruni_name", length = 50)
    private String haruniName;

    @Column(length = 300)
    private String prompts;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_id")
    private OAuth2Provider providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Haruni haruni;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Chatroom> chatrooms = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Item> items = new ArrayList<>();

    @Builder
    protected User(SignUpRequestDto req, String encodedPassword, Haruni haruni) {
        this.email = req.getEmail();
        this.password = encodedPassword;
        this.providerId = OAuth2Provider.fromOAuth2Provider(req.getProviderId());
        this.gender = Gender.fromGender(req.getGender());
        this.nickname = req.getNickname();
        this.alarmActive = req.getAlarmActive();
        this.alarmActiveTime = req.getAlarmActiveTime();
        this.fcmToken = req.getFcmToken();
        this.haruniName = req.getHaruniName();
        this.prompts = req.getPrompt();
        this.role = "ROLE_USER";
        this.haruni = haruni;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
    public void updateAlarmActiveTime(String alarmActiveTime) {
        this.alarmActiveTime = alarmActiveTime;
    }
}