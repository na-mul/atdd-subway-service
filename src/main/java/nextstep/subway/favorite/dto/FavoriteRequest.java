package nextstep.subway.favorite.dto;

import javax.validation.constraints.NotNull;

public class FavoriteRequest {
	@NotNull
	private Long source;
	@NotNull
	private Long target;

	private FavoriteRequest() {
	}

	public FavoriteRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}
}
