package com.jhj.cinephile.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

// 映画キャスト情報を表す埋め込みクラス
@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieCast {
    private String name;
    private String character;
    private String profilePath;
}
