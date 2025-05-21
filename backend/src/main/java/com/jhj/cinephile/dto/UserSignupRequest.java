package com.jhj.cinephile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignupRequest {

    @NotBlank(message = "名前は必須です。")
    @Size(min = 3, message = "名前は3文字以上で入力してください。")
    private String name;

    @NotBlank(message = "メールアドレスは必須です。")
    @Email(message = "有効なメールアドレスの形式で入力してください。")
    private String email;

    @NotBlank(message = "パスワードは必須です。")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,}$",
            message = "10文字以上で、英字と数字を含めてください。"
    )
    private String password;
}
