package nextstep.subway.path.dto;

import java.util.List;

public class PathResponse {
	private List<PathStationResponse> stations;
	private long distance;

	private PathResponse() {
	}

	public PathResponse(List<PathStationResponse> stations, long distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static PathResponse of(Path path) {
		return new PathResponse(PathStationResponse.newList(path.getStations()), path.getDistance());
	}

	public List<PathStationResponse> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}
}
