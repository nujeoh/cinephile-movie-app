package com.jhj.cinephile.util;

import com.jhj.cinephile.exception.InvalidHashIdException;
import org.hashids.Hashids;
import org.springframework.stereotype.Component;

/**
 * Hashidsを使ったIDのエンコード・デコードを提供
 */
@Component
public class HashidsService {

    private final Hashids hashids;

    public HashidsService(Hashids hashids) {
        this.hashids = hashids;
    }

    // 数値IDをHashids形式に変換
    public String encode(Long id) {
        return hashids.encode(id);
    }

    // Hashids文字列を元の数値IDにデコード
    public Long decode(String hash) {
        long[] decoded = hashids.decode(hash);
        if (decoded.length == 0) {
            throw new InvalidHashIdException(hash);
        }
        return decoded[0];
    }
}
