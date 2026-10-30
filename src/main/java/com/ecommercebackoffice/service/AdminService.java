package com.ecommercebackoffice.service;

import com.ecommercebackoffice.config.PasswordEncoder;
import com.ecommercebackoffice.dto.AdminListResponse;
import com.ecommercebackoffice.dto.AdminLoginRequest;
import com.ecommercebackoffice.dto.AdminPageResponse;
import com.ecommercebackoffice.dto.AdminSignupRequest;
import com.ecommercebackoffice.entity.Admin;
import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // 관리자 로그인
    public void login(AdminLoginRequest request, HttpServletRequest httpRequest) {

        // 이메일로 관리자 정보 조회
        Optional<Admin> adminOptional =
                adminRepository.findByAdminEmail(request.getAdminEmail());

        // 해당 이메일의 관리자가 존재하지 않으면 예외 발생
        if (adminOptional.isEmpty()) {
            throw new ServiceException(
                    HttpStatus.UNAUTHORIZED,
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        // 조회한 관리자 정보 가져오기
        Admin admin = adminOptional.get();

        // 입력한 비밀번호가 없거나 일치하지 않으면 예외 발생
        if (request.getPassword() == null
                || !passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new ServiceException(
                    HttpStatus.UNAUTHORIZED,
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        // 활성 상태가 아닌 관리자는 로그인 차단
        if (!"Active".equals(admin.getAdminStatus())) {
            throw new ServiceException(
                    HttpStatus.FORBIDDEN,
                    "활성화되지 않은 관리자 계정입니다."
            );
        }

        // 로그인 성공 후 세션 가져오기
        HttpSession session = httpRequest.getSession();

        // 로그인 성공 시 세션 ID 변경
        httpRequest.changeSessionId();

        // 세션이 48시간 동안 사용되지 않으면 만료
        session.setMaxInactiveInterval(60 * 60 * 48);

        // 세션에 관리자 정보 저장
        session.setAttribute("adminId", admin.getId());
    }

    // 관리자 로그아웃
    public void logout(HttpServletRequest httpRequest) {

        // 기존 세션 가져오기
        HttpSession session = httpRequest.getSession(false);

        // 기존 세션이 존재하면 무효화
        if (session != null) {
            session.invalidate();
        }
    }

    // 관리자 리스트 조회
    public AdminPageResponse getAdminList(
            String keyword,
            String role,
            String status,
            int page,
            int size,
            HttpServletRequest httpRequest) {

        // 기존 세션 가져오기
        HttpSession session = httpRequest.getSession(false);

        // 로그인 정보가 없으면 관리자 리스트 조회 차단
        if (session == null || session.getAttribute("adminId") == null) {
            throw new ServiceException(
                    HttpStatus.UNAUTHORIZED,
                    "로그인이 필요합니다."
            );
        }

        // 세션에 저장된 관리자 ID 가져오기
        Long adminId = (Long) session.getAttribute("adminId");

        // 관리자 ID로 DB에서 최신 관리자 정보 조회
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        // DB에 관리자 정보가 없으면 접근 차단
        if (adminOptional.isEmpty()) {
            throw new ServiceException(
                    HttpStatus.UNAUTHORIZED,
                    "존재하지 않는 관리자 계정입니다."
            );
        }

        // DB에서 조회한 관리자 정보 가져오기
        Admin loginAdmin = adminOptional.get();

        // 활성 상태가 아닌 관리자는 접근 차단
        if (!"Active".equals(loginAdmin.getAdminStatus())) {
            throw new ServiceException(
                    HttpStatus.FORBIDDEN,
                    "활성화되지 않은 관리자 계정입니다."
            );
        }

        // 페이지 번호가 1보다 작으면 예외 발생
        if (page < 1) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "페이지 번호는 1 이상이어야 합니다."
            );
        }

        // 페이지당 개수가 1보다 작으면 예외 발생
        if (size < 1) {
            throw new ServiceException(
                    HttpStatus.BAD_REQUEST,
                    "페이지당 개수는 1 이상이어야 합니다."
            );
        }

        // 관리자 이름 가나다순 오름차순 정렬
        Sort sort = Sort.by(Sort.Direction.ASC, "adminName");

        // 페이지 번호와 페이지당 개수를 설정
        PageRequest pageable = PageRequest.of(page - 1, size, sort);


        // 관리자 페이지 조회 결과를 저장
        Page<Admin> adminPage;

        // 검색어 없이 역할과 상태를 모두 선택한 경우
        if ((keyword == null || keyword.isBlank())
                && role != null && !role.isBlank()
                && status != null && !status.isBlank()) {

            // 관리자 역할과 상태별 조회
            adminPage = adminRepository.findByAdminRoleAndAdminStatus(role, status, pageable);


            // 검색어와 역할 없이 상태만 선택한 경우
        } else if ((keyword == null || keyword.isBlank())
                && (role == null || role.isBlank())
                && status != null && !status.isBlank()) {

            // 관리자 상태별 조회
            adminPage = adminRepository.findByAdminStatus(status, pageable);

            // 검색어 없이 역할만 선택한 경우
        } else if ((keyword == null || keyword.isBlank())
                && role != null && !role.isBlank()) {

            // 관리자 역할별 조회
            adminPage = adminRepository.findByAdminRole(role, pageable);

            // 검색어, 역할, 상태가 모두 없는 경우
        } else if ((keyword == null || keyword.isBlank())
                && (role == null || role.isBlank())
                && (status == null || status.isBlank())) {

            // 관리자 전체 조회
            adminPage = adminRepository.findAll(pageable);

        } else {

            // 관리자 이름 부분 일치 검색
            adminPage = adminRepository.findByAdminNameContaining(keyword, pageable);
        }
        // 조회한 페이지에서 관리자 목록 가져오기
        List<Admin> admins = adminPage.getContent();

        // 관리자 리스트 응답을 저장할 목록 생성
        List<AdminListResponse> responses = new ArrayList<>();

        // 조회한 관리자 정보를 하나씩 꺼내기
        for (Admin admin : admins) {
            AdminListResponse response = new AdminListResponse(
                    admin.getId(),
                    admin.getAdminName(),
                    admin.getAdminEmail(),
                    admin.getAdminPhoneNumber(),
                    admin.getAdminRole(),
                    admin.getAdminStatus(),
                    admin.getAdminRegisteredAt(),
                    admin.getAdminApprovedAt()
            );

            // 변환한 관리자 정보를 목록에 추가
            responses.add(response);
        }

        // 관리자 리스트와 페이징 정보 반환
        return new AdminPageResponse(
                page,
                size,
                adminPage.getTotalElements(),
                adminPage.getTotalPages(),
                responses
        );
    }
}