package com.ecommercebackoffice.service;

import com.ecommercebackoffice.config.PasswordEncoder;
import com.ecommercebackoffice.dto.AdminSignupRequest;
import com.ecommercebackoffice.entity.Admin;
import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // 관리자 회원가입 처리
    public void signup(AdminSignupRequest request) {

        // 관리자 이름을 입력하지 않으면 400 상태 코드로 예외 발생
        if (request.getAdminName() == null || request.getAdminName().isBlank()) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "관리자 이름을 입력해야 합니다."
            );
        }

        // 관리자 이름이 20자를 초과하면 400 상태 코드로 예외 발생
        if (request.getAdminName().length() > 20) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "관리자 이름은 20자 이하여야 합니다."
            );
        }
        // 비밀번호가 8자 미만이거나 20자를 초과하면 400 상태 코드로 예외 발생
        if (request.getPassword() == null
                || request.getPassword().length() < 8
                || request.getPassword().length() > 20) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "비밀번호는 8자 이상 20자 이하여야 합니다."
            );
        }

        // 입력받은 전화번호를 변수에 저장
        String phoneNumber = request.getAdminPhoneNumber();

        // 전화번호를 입력하지 않으면 400 상태 코드로 예외 발생
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "전화번호를 입력해야 합니다."
            );
        }

        // 전화번호에 포함된 하이픈과 공백 제거
        phoneNumber = phoneNumber.replace("-", "").replace(" ", "");

        // 국내 휴대전화 번호를 국가번호(+82)가 포함된 형식으로 변환
        if (phoneNumber.startsWith("010") && phoneNumber.length() == 11) {
            phoneNumber = "+82" + phoneNumber.substring(1);
        }

        // 전화번호가 +로 시작하지 않으면 400 상태 코드로 예외 발생
        if (!phoneNumber.startsWith("+")) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "전화번호는 국가번호를 포함해야 합니다."
            );
        }

        // 전화번호 길이가 국제 형식의 허용 범위를 벗어나면 400 상태 코드로 예외 발생
        if (phoneNumber.length() < 3 || phoneNumber.length() > 16) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "전화번호는 국가번호를 포함해 최대 15자리 숫자로 입력해야 합니다."
            );
        }

        // 국가번호(+) 뒤에 숫자가 아닌 문자가 있는지 검사
        for (int i = 1; i < phoneNumber.length(); i++) {

            // 숫자 0~9 범위를 벗어나면 400 상태 코드로 예외 발생
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
                throw new ServiceException(
                        HttpStatus.BAD_REQUEST,
                        "전화번호에는 숫자만 입력할 수 있습니다."
                );
            }
        }

        // 관리자 이메일을 입력하지 않으면 400 상태 코드로 예외 발생
        if (request.getAdminEmail() == null || request.getAdminEmail().isBlank()) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "관리자 이메일을 입력해야 합니다."
            );
        }

        // 이메일에 @가 없으면 400 상태 코드로 예외 발생
        if (!request.getAdminEmail().contains("@")) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "올바른 이메일 형식으로 입력해야 합니다."
            );
        }

        // 이메일이 @로 끝나면 400 상태 코드로 예외 발생
        if (request.getAdminEmail().endsWith("@")) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "올바른 이메일 형식으로 입력해야 합니다."
            );
        }

        // 이메일이 @로 시작하면 400 상태 코드로 예외 발생
        if (request.getAdminEmail().startsWith("@")) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "올바른 이메일 형식으로 입력해야 합니다."
            );
        }

        // 이메일에 포함된 @ 개수를 확인
        int count = 0;
        for (int i = 0; i < request.getAdminEmail().length(); i++) {
            if (request.getAdminEmail().charAt(i) == '@') {
                count++;
            }
        }

        // 이메일에 @가 정확히 1개가 아니면 400 상태 코드로 예외 발생
        if (count != 1) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "올바른 이메일 형식으로 입력해야 합니다."
            );
        }

        // 관리자 이메일이 40자를 초과하면 400 상태 코드로 예외 발생
        if (request.getAdminEmail().length() > 40) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "관리자 이메일은 40자 이하여야 합니다."
            );
        }

        // 이미 등록된 이메일인지 확인
        if (adminRepository.existsByAdminEmail(request.getAdminEmail())) {

            // 중복된 이메일이면 409 상태 코드로 예외 발생
            throw new ServiceException(
                    HttpStatus.CONFLICT,
                    "이미 사용 중인 이메일입니다."
            );
        }

        // 입력받은 관리자 역할을 변수에 저장
        String adminRole = request.getAdminRole();

        // 관리자 역할을 선택하지 않으면 400 상태 코드로 예외 발생
        if (request.getAdminRole() == null || request.getAdminRole().isBlank()) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "관리자 역할을 선택해야 합니다."
            );
        }

        // 한글로 입력된 관리자 역할을 영문 코드로 변환
        if (adminRole.equals("CS관리자")) {
            adminRole = "CS_Admin";
        } else if (adminRole.equals("운영관리자")) {
            adminRole = "Operation_Admin";
        }


        // 허용되지 않은 관리자 역할이면 400 상태 코드로 예외 발생
        if (!adminRole.equals("CS_Admin")
                && !adminRole.equals("Operation_Admin")) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "선택할 수 없는 관리자 역할입니다."
            );
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 관리자 객체 생성
        Admin admin = new Admin(
                request.getAdminName(),
                request.getAdminEmail(),
                encodedPassword,
                phoneNumber,
                adminRole
        );

        // 관리자 정보 DB에 저장
        adminRepository.save(admin);
    }
}