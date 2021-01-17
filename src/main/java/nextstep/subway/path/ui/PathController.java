package nextstep.subway.path.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;

@RestController
public class PathController {

	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping(value = "/paths", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PathResponse> findPath(@RequestParam Long source, @RequestParam Long target) {
		return ResponseEntity.ok().body(pathService.findPath(source, target));
	}
}
