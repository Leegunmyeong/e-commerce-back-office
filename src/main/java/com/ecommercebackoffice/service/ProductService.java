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
    public ProductCreateResponseDto productCreated(ProductCreateRequestDto requestDto) {
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

    @Transactional(readOnly = true)
    public ProductListGetResponse getProductList(String itemName, int page, int size, String filter, String sort, String category, ProductStatus status) {
        // 사용자가 상품명을 빈 문자열로 입력했는지 검사
        // 상품명 자체를 입력하지 않는 null은 전체 조회를 위해 허용
        // 상품명은 null이 아닌데 (true) 공백이면(true) 예외를 발생시킨다
        if (itemName != null && itemName.equals("")) {
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


        // 전체 상품 조회 및 정렬
        List<Product> products;
        // 정렬 기준과 정렬 순서를 모두 입력하지 않은 경우
        if (filter == null && sort == null) {
            // 정렬 조건 없이 전체 상품 조회
            products = productRepository.findAll();
        } else {
            // 정렬 기준을 입력하지 않은 경우
            if (filter == null) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "정렬 기준을 입력해주세요.");
            }
            // 정렬기준이 price가 아닌 경우
            if (!filter.equals("price")) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, " 정렬 기준은 price만 가능합니다.");
            }
            // 정렬순서를 입력하지 않는 경우
            if (sort == null) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "정렬 순서를 입력해주세요.");
            }

            // 정렬 순서가 asc가 아닌 경우
            if (!sort.equals("asc")) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "정렬은 asc(오름차순)만 가능합니다.");
            }

            // 가격 기준 오름차순으로 전체 상품 조회
            products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "itemPrice"));
        }


        // 조건에 맞는 상품 DTO를 저장할 리스트
        List<ProductDto> productDtoList = new ArrayList<>();
        // 조회한 상품 반복문으로 하나씩 검사
        for (Product product : products) {

            // 상품명을 입력한 경우에만 상품명을 비교
            if (itemName != null) {
                if (!product.getItemName().equals(itemName)) {
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


}
