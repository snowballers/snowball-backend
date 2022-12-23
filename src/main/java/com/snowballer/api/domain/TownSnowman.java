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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snowman_id")
    private Snowman snowman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id")
    private Town town;

    /**
     * 열람했을 때, 열람했다고 표시하는 로직
     */
    public void changeSeen() {
        if (this.seen == false) {
            this.seen = true;
        }
    }

    /**
     * Letter 작성
     * - url의 town과 눈사람의 town 일치 확인
     * - 이미 letter 존재 시 편지 등록 불가
     * @param townId
     * @param submitLetterRequest
     */
    public void writeLetter(Long townId, SubmitLetterRequest submitLetterRequest) {
        if (town.getId() != townId) {
            throw new RestApiException(ErrorCode.INVALID_TOWN_LINK);
        }

        if (letter != null) {
            throw new RestApiException(ErrorCode.ALREADY_EXIST_LETTER);
        }

        this.letter = submitLetterRequest.getLetter();
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
