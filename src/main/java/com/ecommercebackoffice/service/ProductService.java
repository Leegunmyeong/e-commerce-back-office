package com.ecommercebackoffice.service;

import com.ecommercebackoffice.dto.*;
import com.ecommercebackoffice.entity.Admin;
import com.ecommercebackoffice.entity.Product;
import com.ecommercebackoffice.entity.ProductStatus;
import com.ecommercebackoffice.exception.ServiceException;
import com.ecommercebackoffice.repository.AdminRepository;
import com.ecommercebackoffice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;


    // 상품 등록
    @Transactional
    public ProductCreateResponseDto createdProduct(ProductCreateRequestDto requestDto) {
        // 요청 DTO에서 관리자 ID 꺼내서 변수에 담기
        Long adminId = requestDto.getAdminId();

        // 관리자 ID로 DB에서 관리자 조회
        // 관리자가 없으면 NOT_FOUND 예외 발생
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다.")
        );
        // 요청 DTO의 상품 정보와 조회한 관리자로 Product 객체 생성
        // 이 부분에서 Product 생성자에서 재고 수령과 단종 여부로 상품 상태를 결정
        Product product = new Product(
                requestDto.getItemName(),
                requestDto.getItemCategory(),
                requestDto.getItemPrice(),
                requestDto.getItemQuantity(),
                requestDto.getItemDiscontinuedStatus(),
                admin
        );
        // 생성한 Product 객체를 DB에 저장
        Product savedProduct = productRepository.save(product);
        // 저장된 상품 정보를 응답 DTO에 담기
        ProductCreateResponseDto responseDto = new ProductCreateResponseDto(
                savedProduct.getAdmin().getAdminName(),
                savedProduct.getAdmin().getAdminEmail(),
                savedProduct.getId(),
                savedProduct.getItemName(),
                savedProduct.getItemCategory(),
                savedProduct.getItemPrice(),
                savedProduct.getItemQuantity(),
                savedProduct.getItemStockStatus(),
                savedProduct.getItemRegistrationAt()
        );
        // Controller에게 응답 DTO 반환
        return responseDto;
    }

    // 상품 리스트 조회
    @Transactional(readOnly = true)
    public ProductListGetResponse getProductList(String itemName, int page, int size, String filter, String sort, String category, ProductStatus status) {

        // 상품명을 입력한 경우 빈 문자열이나 공백만 입력했는지 검사
        // 상품명 자체를 입력하지 않는 null은 전체 조회를 위해 허용
        // 상품명은 null이 아닌데 (true) 문자열이 비어있거나 공백이면(true) 예외를 발생시킨다
        if (itemName != null && itemName.isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요.");
        }
        // 페이지 번호는 1 이상이어야한다
        if (page < 1) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "페이지 번호는 1 이상이어야 합니다.");
        }

        // 페이지당 보여지는 상품 개수 검사
        if (size < 1) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "페이지당 상품 개수는 1개 이상이어야 합니다.");
        }

        // 정렬 기준을 입력하지 않았다면 기본값 price 적용
        if (filter == null) {
            filter = "price";
        }

        // 정렬 순서를 입력하지 않았다면 기본값 asc 적용
        if (sort == null) {
            sort = "asc";
        }

        // 정렬 기준이 price도 아니고 quantity도 아니라면 예외 발생
        if (!filter.equals("price") && !filter.equals("quantity")) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "정렬 기준은 price(상품가격),quantity(상품수량)만 가능합니다.");
        }
        // 정렬 순서가 asc도 아니고 desc도 아니라면 예외 발생
        if (!sort.equals("asc") && !sort.equals("desc")) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "정렬 순서는 asc(오름차순), desc(내림차순)만 가능합니다.");
        }
        // 기본 정렬 순서를 오름차순으로 설정
        Sort.Direction direction = Sort.Direction.ASC;
        // 내림차순을 요청했다면 정렬 순서 변경
        if (sort.equals("desc")) {
            direction = Sort.Direction.DESC;
        }
        // 정렬 기준에 따라 상품 조회
        List<Product> products;

        if (filter.equals("price")) {
            // 상품 가격 기준 정렬
            products = productRepository.findAll(Sort.by(direction, "itemPrice"));
        } else {
            // 재고 수량 기준 정렬
            products = productRepository.findAll(Sort.by(direction, "itemQuantity"));
        }


        // 조건에 맞는 상품 DTO를 저장할 리스트
        List<ProductDto> productDtoList = new ArrayList<>();
        // 조회한 상품 반복문으로 하나씩 검사
        for (Product product : products) {

            // 상품명을 입력한 경우에만 상품명 검색
            if (itemName != null) {
                // 상품명에 검색어가 포함되어 있지 않다면 해당 상품 제외
                if (!product.getItemName().contains(itemName)) {
                    continue;
                }
            }

            // 카테고리를 선택했다면 같은 카테고리의 상품만 조회
            if (category != null) {
                if (!product.getItemCategory().equals(category)) {
                    continue;
                }
            }
            // 상품 상태가 다르면 제외
            if (product.getItemStockStatus() != status) {
                continue;
            }

            // 조건에 맞는 Product Entity를 ProductDto로 변환
            ProductDto productDto = new ProductDto(
                    product.getId(),
                    product.getItemName(),
                    product.getItemCategory(),
                    product.getItemPrice(),
                    product.getItemQuantity(),
                    product.getItemStockStatus(),
                    product.getItemRegistrationAt(),
                    product.getAdmin().getAdminName()
            );
            // 모든 조건을 통과한 상품만 추가
            productDtoList.add(productDto);
        }

        // 조건에 맞는 전체 상품 개수
        long totalCount = productDtoList.size();

        // 전체 페이지 수 계산
        long totalPage = totalCount / size;

        // 나머지가 있다면 페이지가 하나 더 필요
        if (totalCount % size != 0) {
            totalPage++;
        }

        // 현재 페이지의 시작 위치 계산
        long startIndex = (long) (page - 1) * size;
        // 현재 페이지의 끝 위치 계산
        long endIndex = startIndex + size;

        // 현재 페이지에 해당하는 상품을 담을 리스트 생성
        List<ProductDto> pageProdcutList = new ArrayList<>();
        // 현재 페이지에 해당하는 상품만 추가
        for (int i = 0; i < productDtoList.size(); i++) {
            // 시작 위치 이전의 상품은 건너뛰기
            if (i < startIndex) {
                continue;
            }
            // 끝 위치에 도달하면 반복 종료
            if (i >= endIndex) {
                break;
            }
            // 현재 페이지에 해당하는 상품 추가
            pageProdcutList.add(productDtoList.get(i));
        }
        // 최종 응답 DTO 생성
        ProductListGetResponse response = new ProductListGetResponse(
                (long) page,
                (long) size,
                totalCount,
                totalPage,
                pageProdcutList
        );
        // Controller에게 응답 DTO 반환
        return response;

    }


    // 상품 상세 조회
    @Transactional(readOnly = true)
    public GetProductDetailResponse getProductDetail(Long productId) {
        // 전달받은 상품 ID를 DB에서 조회했을때 상품 ID가 존재하지않으면 NOT FOUND 예외 발생
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "없는 상품 입니다")
        );
        // 존재하는 상품이면 조회한 ID에 맞는 상품 상세정보 담아서 응답 Dto 생성
        GetProductDetailResponse response = new GetProductDetailResponse(
                product.getItemName(),
                product.getItemCategory(),
                product.getItemPrice(),
                product.getItemQuantity(),
                product.getItemStockStatus(),
                product.getItemRegistrationAt(),
                product.getAdmin().getAdminName(),
                product.getAdmin().getAdminEmail()
        );
        // Controller에게 응답 Dto 반환
        return response;
    }

    // 상품 정보 수정
    @Transactional
    public ProductUpdatedInfoResponseDto updatedProductInfo(Long productId, ProductUpdatedInfoRequestDto requestDto) {
        // 1. 요청 DTO에서 수정할 상품 정보 꺼내기
        String itemName = requestDto.getItemName();
        String itemCategory = requestDto.getItemCategory();
        Long itemPrice = requestDto.getItemPrice();

        // 2. 수정할 정보를 입력안했을때 예외
        if (itemName == null && itemCategory == null && itemPrice == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "수정할 상품 정보를 입력해주세요.");
        }

        // 상품명 입력 시 빈 문자열 예외 처리
        if (itemName != null) {
            if (itemName.isEmpty()) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요.");
            }
        }

        // 상품 카테고리를 빈 문자열로 입력한 경우 예외 처리
        if (itemCategory != null) {
            if (itemCategory.isEmpty()) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "카테고리를 입력해주세요.");
            }
        }

        // 수정할 카테고리 명 20자 이상 작성했을 때 예외 처리
        if (itemCategory != null) {
            if (itemCategory.length() > 20) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "카테고리는 20자 이하로 입력해주세요.");
            }
        }
        // 수정할 상품 가격 0원 이하일 때 오류 처리
        if (itemPrice != null) {
            if (itemPrice < 0) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "상품 가격은 0원 이상이어야 합니다");
            }
        }


        // 전달받은 상품 ID로 DB에서 상품 조회
        // 조회하지 않는 상품이면 404 Not Found 예외 발생
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
        );

        // Product Entity에 수정할 정보 전달
        // null이 아닌 필드만 수정
        product.updatedProductInfo(itemName, itemCategory, itemPrice);

        // 수정된 상품 정보를 ResponseDto에 담기
        ProductUpdatedInfoResponseDto responseDto = new ProductUpdatedInfoResponseDto(
                product.getId(),
                product.getItemName(),
                product.getItemCategory(),
                product.getItemPrice()
        );

        // Controller에게 수정 결과 반환
        return responseDto;
    }

    // 상품 재고 변경
    @Transactional
    public UpdatedProductStockQuantityResponseDto updatedProductStockQuantity(Long productId, UpdatedProductStockQuantityRequestDto requestdto) {
        // 1. 상품 ID를 이용하여 DB에서 상품 조회
        // 상품이 존재하지 않으면 404 예외 발생
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
        );
        // 2. 요청 DTO에서 변경할 재고 수량 가져오기
        Long itemQuantity = requestdto.getItemQuantity();
        // 3. 재고 수량을 입력하지 않았다면 예외 발생
        if (itemQuantity == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "상품 재고 수량을 입력해주세요.");
        }
        // 4. 재고 수량이 음수라면 예외 발생
        if (itemQuantity < 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "상품 재고 수량은 0 이상이어야 합니다.");
        }
        // 5. 상품의 재고 수량 변경
        // Product 엔티티 내부에서 재고 상태까지 자동으로 변경
        product.updateProductQuantity(itemQuantity);

        // 6. 변경된 상품 정보를 응답 DTO에 담기
        UpdatedProductStockQuantityResponseDto responseDto = new UpdatedProductStockQuantityResponseDto(
                product.getId(),
                product.getItemName(),
                product.getItemQuantity(),
                product.getItemStockStatus()
        );
        // 7. Controller에게 응답 DTO 반환
        return responseDto;
    }

    // (order 담당자분이 사용하실 메서드)
    // 고객 주문 시 주문한 수량만큼 상품 재고 차감
    @Transactional
    public void decreaseStock(Long productId, Long orderQuantity) {
        // 1. 주문 수량이 null이거나 1개 미만이면 예외 발생
        if (orderQuantity == null || orderQuantity < 1) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "주문 수량은 1개 이상이어야 합니다.");
        }
        // 2. 상품 ID로 DB에서 상품조회
        // 상품이 존재하지 않으면 404 예외 발생
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
        );
        // 3. 상품이 단종 상태 및 품절 상태인 경우라면 신규 주문 불가능
        if (product.getItemDiscontinuedStatus()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "단종된 상품은 주문할 수 없습니다.");
        } else if (product.getItemStockStatus().equals("SOLD_OUT")) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "품절된 상품은 주문할 수 없습니다.");
        }
        // 4. 현재 상품 재고 수량 가져오기
        Long currentItemQuantity = product.getItemQuantity();
        // 5. 주문 수량이 현재 재고보다 많다면 예외 발생
        if (orderQuantity > currentItemQuantity) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다.");
        }
        // 6. 현재 재고에서 주문 수량만큼 차감
        Long remainingItemQuantity = currentItemQuantity - orderQuantity;
        // 7. 차감한 수량으로 상품 재고 변경
        // Product 엔티티 내부에서 상품 상태도 자동으로 변경
        product.updateProductQuantity(remainingItemQuantity);
    }

    // (order 담당자분이 사용하실 메서드)
    // 주문 취소 시 재고 자동 복구
    @Transactional
    public void restoreStock(Long productId, Long cancelQuantity) {
        // 1. 복구할 수량이 null이거나 1개 미만이면 예외 발생
        if (cancelQuantity == null || cancelQuantity < 1) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "복구 수량은 1개 이상이어야 합니다.");
        }
        // 2. 상품 ID로 DB에서 상품 조회
        // 상품 존재하지 않으면 404 예외 발생
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
        );
        // 3. 현재 상품의 재고 수량 가져오기
        Long currentItemQuantity = product.getItemQuantity();
        // 4. 현재 재고 수량에 주문 취소 수량 더하기
        Long restoredItemQuantity = currentItemQuantity + cancelQuantity;
        // 5. 복구한 수량으로 상품 재고 변경
        // Product Entity 내무에서 상품 상태까지 자동 변경
        // 단종된 상품이라면 DISC 상태 유지 되고 수량은 변경됨
        product.updateProductQuantity(restoredItemQuantity);
    }

    // 상품 상태 변경
    @Transactional
    public UpdatedProductStockStatusResponseDto updatedProductStockStatus(Long productId, UpdatedProductStockStatusRequestDto requestDto) {

        // 요청 DTO에서 단종 여부 가져오기
        Boolean itemDiscontinuedStatus = requestDto.getItemDiscontinuedStatus();

        // 단종 여부 미입력 및 단종 여부 null일 때 예외 처리
        if (itemDiscontinuedStatus == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "상품 단종 여부를 입력해주세요.");
        }

        /* 잘못된 자료형 입력 처리 설명
        * itemDiscontinuedStatus는 Boolean 타임 / 요청 데이터에서 "abc"는 Boolean 타입으로 변경 불가능
        * 요청을 DTO로 변환하는 과정에서 오류 발생
        * ProductService가 실행되기도 전에 예외 발생됨
        * GlobalExceptionHandler에 handleInvalidRequest()가 오류를 처리하도록 설계
        */

        // 전달받은 ID로 DB에서 상품 조회
        // 존재하지 않는 상품 또는 삭제된 상품이면 404 예외 발생
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ServiceException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.")
        );

        // 상품 단종 여부 변경
        // Product 엔티티 내부에서 상품 상태도 자동으로 변경
        product.updateDiscontinuedStatus(itemDiscontinuedStatus);

        // 변경된 상품 정보를 응답 DTO에 담기
        UpdatedProductStockStatusResponseDto responseDto = new UpdatedProductStockStatusResponseDto(
                product.getId(),
                product.getItemName(),
                product.getItemQuantity(),
                product.getItemDiscontinuedStatus(),
                product.getItemStockStatus()
        );
        // Controller에게 응답 DTO 반환
        return responseDto;
    }

}
