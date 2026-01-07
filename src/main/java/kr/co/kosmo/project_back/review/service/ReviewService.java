package kr.co.kosmo.project_back.review.service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.kosmo.project_back.review.dto.request.ReviewRequestDto;
import kr.co.kosmo.project_back.review.dto.response.ReviewResponseDto;
import kr.co.kosmo.project_back.review.mapper.ReviewImageMapper;
import kr.co.kosmo.project_back.review.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewImageMapper reviewImageMapper;

    @Value("${file.upload.review-path}")
    private String reviewUploadPath;

    // 상품별 리뷰 목록 조회
    public List<ReviewResponseDto> getReviewsByProduct(Integer productId, String sort) {
        // 리뷰 목록 조회
        List<ReviewResponseDto> reviews = 
            reviewMapper.findReviewsByProductId(productId, sort);
        // 각 리뷰에 이미지 목록 붙이기
        for (ReviewResponseDto review : reviews) {
            review.setReviewImages(
                reviewImageMapper.findByReviewId(review.getReviewId())
            );
        }
        return reviews;
    }

    // 상품 리뷰 작성
    public Long insertReview(ReviewRequestDto dto) {
    
        reviewMapper.insertReview(dto);
        Long reviewId = dto.getReviewId();

        // 이미지 파일 저장 + DB 저장
        if(dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (MultipartFile file : dto.getImages()) {
                if (file.isEmpty()) continue;

                String imageUrl = saveReviewImage(file);
                reviewImageMapper.insertReviewImage(reviewId, imageUrl);
            }
        }
        return reviewId;
    }

    // 실제 파일 저장
    private String saveReviewImage(MultipartFile file) {
        try {
            String original = file.getOriginalFilename();
            String ext = "";
            String fileName = UUID.randomUUID() + ext;

            // 프로젝트 루트 기준 절대경로 만들기
            String projectRoot = new File("").getAbsolutePath(); // 실행 기준(프로젝트 폴더)
            String dirPath = projectRoot + File.separator + reviewUploadPath
                + File.separator + LocalDate.now().getYear()
                + File.separator + String.format("%02d", LocalDate.now().getMonthValue());
            
            File dir = new File(dirPath);
            if(!dir.exists()) dir.mkdirs();

            //실제 저장 파일 경로
            File saveFile = new File(dir, fileName);
            file.transferTo(saveFile);

            // DB 저장 URL
            return "/uploads/reviews/"
                    + LocalDate.now().getYear() + "/"
                    + String.format("%02d", LocalDate.now().getMonthValue())
                    + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("리뷰 이미지 저장 실패", e);
        }
    }
    // 리뷰 수정
    public void updateReview(Long reviewId, ReviewRequestDto dto) {
        dto.setReviewId(reviewId);
        reviewMapper.updateReview(dto);
    }
    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewMapper.deleteReview(reviewId);
    }
}