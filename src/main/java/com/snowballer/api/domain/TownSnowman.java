package com.snowballer.api.domain;

import com.snowballer.api.common.domain.BaseTimeEntity;
import com.snowballer.api.common.enums.ErrorCode;
import com.snowballer.api.common.exception.RestApiException;
import com.snowballer.api.dto.request.SubmitLetterRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class TownSnowman extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String letter;

    @Column(name = "sender_name")
    private String senderName;

    @Column(nullable = false)
    private Boolean seen;

    @Column(name = "have_letter")
    private Boolean haveLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snowman_id")
    private Snowman snowman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id")
    private Town town;

    public TownSnowman changeSeen() {
        if (this.seen == false) {
            this.seen = true;
        }
        return this;
    }

    public void checkHaveLetter() {
        if (this.haveLetter == false) {
            throw new RuntimeException("Snowballers Error: 접근할 수 없는 눈사람입니다.");
        }
    }

    public void writeLetter(Long townId, SubmitLetterRequest submitLetterRequest) {
        if (town.getId() != townId) {
            throw new RestApiException(ErrorCode.NOT_FOUNT_SNOWMAN);
        }

        if (haveLetter == true) {
            throw new RuntimeException("Snowballers Error: 이미 작성한 편지가 있습니다.");
        }
        this.letter = submitLetterRequest.getLetter();
        this.haveLetter = true;
    }

    public static TownSnowman buildSnowman(Snowman snowman, Town town, String sender) {
        return TownSnowman.builder()
            .senderName(sender)
            .seen(false)
            .snowman(snowman)
            .town(town)
            .build();
    }
}
