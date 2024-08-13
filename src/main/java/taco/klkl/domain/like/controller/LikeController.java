package taco.klkl.domain.like.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.dto.response.LikeResponseDto;
import taco.klkl.domain.like.service.LikeService;

@Slf4j
@RestController
@RequestMapping("/v1/products/{productId}/likes")
@RequiredArgsConstructor
@Tag(name = "3. 좋아요", description = "좋아요 관련 API")
public class LikeController {

	private final LikeService likeService;

	@PostMapping
	public LikeResponseDto addLike(@PathVariable final Long productId) {
		return likeService.createLike(productId);
	}

	@DeleteMapping
	public LikeResponseDto removeLike(@PathVariable final Long productId) {
		return likeService.deleteLike(productId);
	}
}
