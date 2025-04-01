package kr.kro.majab.owner;

import kr.kro.majab.owner.request.OwnerSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional
    public void signUpOwner(OwnerSignUpRequest request) {

        if (request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        Owner owner = Owner.builder()
                .email(request.getEmail())
                .password(request.getPassword()) //todo: 비밀번호 해싱 필요함
                .phoneNumber(request.getPhoneNumber())
                .build();

        ownerRepository.save(owner);
    }

}
