package kr.co.kosmo.project_back.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.ReviewResponseDto;
import kr.co.kosmo.project_back.user.dto.ReviewRequestDto;
import kr.co.kosmo.project_back.user.mapper.ReviewMapper;
import kr.co.kosmo.project_back.user.mapper.ReviewImageMapper;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;

    // AdminProductService와 동일하게 기본 업로드 경로 사용
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public List<ReviewResponseDto> getReviewsByProduct(Integer productId) {
        log.debug("리뷰 조회 시작 - productId: {}", productId);
        List<ReviewResponseDto> reviews = reviewMapper.findReviewsByProductId(productId);
        log.debug("조회된 리뷰 개수: {}", reviews != null ? reviews.size() : 0);
        if (reviews != null) {
            for (ReviewResponseDto review : reviews) {
                log.debug("리뷰 ID: {}, 사용자 ID: {}, 내용: {}", review.getReviewId(), review.getUserId(), review.getContent());
                review.setReviewImages(reviewImageMapper.findByReviewId(review.getReviewId()));
            }
        }
        return reviews;
    }

    public List<ReviewResponseDto> getAllReviews() {
        log.debug("전체 리뷰 조회 시작");
        List<ReviewResponseDto> reviews = reviewMapper.findAllReviews();
        log.debug("조회된 전체 리뷰 개수: {}", reviews != null ? reviews.size() : 0);
        if (reviews != null) {
            for (ReviewResponseDto review : reviews) {
                review.setReviewImages(reviewImageMapper.findByReviewId(review.getReviewId()));
            }
        }
        return reviews;
    }

    public List<ReviewResponseDto> getReviewsByUserId(Integer userId) {
        log.debug("사용자별 리뷰 조회 시작 - userId: {}", userId);
        List<ReviewResponseDto> reviews = reviewMapper.findReviewsByUserId(userId);
        log.debug("조회된 리뷰 개수: {}", reviews != null ? reviews.size() : 0);
        if (reviews != null) {
            for (ReviewResponseDto review : reviews) {
                log.debug("리뷰 ID: {}, 상품 ID: {}, 내용: {}", review.getReviewId(), review.getProductId(), review.getContent());
                review.setReviewImages(reviewImageMapper.findByReviewId(review.getReviewId()));
            }
        }
        return reviews;
    }

    public ReviewResponseDto getReview(Integer reviewId) {
        log.debug("리뷰 상세 조회 시작 - reviewId: {}", reviewId);
        ReviewResponseDto review = reviewMapper.findByReviewId(reviewId);
        if (review != null) {
            log.debug("리뷰 조회 성공 - reviewId: {}, content: {}", review.getReviewId(), review.getContent());
            review.setReviewImages(reviewImageMapper.findByReviewId(reviewId));
        } else {
            log.debug("리뷰를 찾을 수 없음 - reviewId: {}", reviewId);
        }
        return review;
    }

    public Integer insertReview(ReviewRequestDto dto) {
        log.debug("리뷰 작성 시작 - userId: {}, productId: {}, content: {}", dto.getUserId(), dto.getProductId(), dto.getContent());
        
        // 배송완료한 주문이 있는지 확인
        Boolean hasDeliveredOrder = reviewMapper.hasDeliveredOrder(dto.getUserId(), dto.getProductId());
        if (hasDeliveredOrder == null || !hasDeliveredOrder) {
            log.warn("리뷰 작성 실패: 배송완료한 주문이 없음 - userId: {}, productId: {}", dto.getUserId(), dto.getProductId());
            throw new IllegalStateException("배송완료한 상품에 대해서만 리뷰를 작성할 수 있습니다.");
        }
        
        // 이미 해당 상품에 대한 리뷰를 작성했는지 확인 (상품당 하나만)
        Boolean hasExistingReview = reviewMapper.hasExistingReview(dto.getUserId(), dto.getProductId());
        if (hasExistingReview != null && hasExistingReview) {
            log.warn("리뷰 작성 실패: 이미 리뷰가 존재함 - userId: {}, productId: {}", dto.getUserId(), dto.getProductId());
            throw new IllegalStateException("이미 해당 상품에 대한 리뷰를 작성하셨습니다.");
        }
        
        Integer result = reviewMapper.insertReview(dto);
        log.debug("리뷰 작성 결과 - result: {}, reviewId: {}", result, dto.getReviewId());

        // 이미지가 있으면 업로드 및 DB 저장
        if (result > 0 && dto.getImages() != null && !dto.getImages().isEmpty()) {
            Integer reviewId = dto.getReviewId();
            for (MultipartFile image : dto.getImages()) {
                if (image != null && !image.isEmpty()) {
                    try {
                        String imageUrl = saveReviewImageFile(image);
                        reviewImageMapper.insertReviewImage(reviewId, imageUrl);
                        log.debug("리뷰 이미지 저장 성공 - reviewId: {}, imageUrl: {}", reviewId, imageUrl);
                    } catch (RuntimeException e) {
                        // 이미지 저장 실패 시 로그만 남기고 리뷰 자체는 유지
                        log.error("리뷰 이미지 저장 실패 - reviewId: {}, message: {}", reviewId, e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    /**
     * 리뷰 이미지를 파일로 저장하고, 프론트에서 접근 가능한 URL을 반환
     * (예: /uploads/review/파일명)
     */
    private String saveReviewImageFile(MultipartFile file) {
        try {
            // 리뷰 이미지는 uploads/review 하위에 저장
            Path uploadPath = Paths.get(uploadDir, "review");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String filename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // WebConfig 에서 /uploads/** -> file:uploads/ 로 매핑되어 있으므로,
            // 하위 경로를 포함한 URL을 반환
            return "/uploads/review/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("리뷰 이미지 파일 저장 실패: " + e.getMessage(), e);
        }
    }

    public Boolean canWriteReview(Integer userId, Integer productId) {
        log.debug("리뷰 작성 가능 여부 확인 - userId: {}, productId: {}", userId, productId);
        
        // 배송완료한 주문이 있는지 확인
        Boolean hasDeliveredOrder = reviewMapper.hasDeliveredOrder(userId, productId);
        if (hasDeliveredOrder == null || !hasDeliveredOrder) {
            return false;
        }
        
        // 이미 해당 상품에 대한 리뷰를 작성했는지 확인 (상품당 하나만)
        Boolean hasExistingReview = reviewMapper.hasExistingReview(userId, productId);
        if (hasExistingReview != null && hasExistingReview) {
            return false;
        }
        
        return true;
    }
}



