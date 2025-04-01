package kr.kro.majab.owner.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OwnerSignUpRequest {

    @NotEmpty(message = "이메일 입력은 필수입니다")
    private String email;

    @NotEmpty(message = "비밀번호 입력은 필수입니다")
    private String password;

    @NotEmpty(message = "비밀번호 확인 입력은 필수입니다")
    private String passwordConfirmation;

    @NotEmpty(message = "전화번호 입력은 필수입니다")
    private String phoneNumber;

    @Builder
    public OwnerSignUpRequest(String email, String password, String passwordConfirmation, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.phoneNumber = phoneNumber;
    }
}
