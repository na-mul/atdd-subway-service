package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.fare.dto.Fare;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class Path {
	private final Sections sections;
	private final List<Station> stations;
	private final long distance;
	private final Fare fare;

	public Path(Sections sections, List<Station> stations, long distance) {
		this.sections = sections;
		this.stations = stations;
		this.distance = distance;
		this.fare = new Fare(sections, stations, distance);
	}

	public Path discountFareByAge(int age) {
		fare.discountByAge(age);
		return this;
	}

	public Sections getSections() {
		return sections;
	}

	public List<Station> getStations() {
		return stations;
	}

	public long getDistance() {
		return distance;
	}

	public long getFare() {
		return fare.getFare();
	}
}
