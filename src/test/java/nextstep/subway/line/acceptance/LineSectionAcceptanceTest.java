package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    /*
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음 & 지하철 노선에 지하철역 등록되어 있음
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 0, 강남역.getId(), 정자역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    /**
     *   Scenario: 지하철 구간을 관리
     *     When 지하철 구간 등록 요청
     *     Then 지하철 구간 등록됨
     *     When 구간 사이에 지하철 구간 등록 요청
     *     Then 지하철 구간 등록됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 등록한 지하철 구간이 반영된 역 목록이 순서대로 조회됨
     *     When 종점을 포함하지 않은 지하철 구간 삭제 요청
     *     Then 지하철 구간 삭제됨
     *     When 종점을 포함한 지하철 구간 삭제 요청
     *     Then 지하철 구간 삭제됨
     *     When 지하철 노선에 등록된 역 목록 조회 요청
     *     Then 삭제한 지하철 구간이 반영된 역 목록이 순서대로 조회됨
     */
    @DisplayName("지하철 구간 관리 통합 인수 테스트")
    @Test
    void testIntegrationAcceptance () {
        // When 지하철 구간 등록 요청
        ExtractableResponse 지하철역_등록_요청_응답1 = 지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 3);

        // Then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(지하철역_등록_요청_응답1);

        // When 구간 사이에 지하철 구간 등록 요청
        ExtractableResponse 지하철역_등록_요청_응답2 =  지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // Then 지하철 구간 등록됨
        지하철_노선에_지하철역_등록됨(지하철역_등록_요청_응답2);

        // When 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철역_조회_요청_응답1 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철역_조회_요청_응답1, Arrays.asList(강남역, 양재역, 정자역, 광교역));

        // When 종점을 포함하지 않은 지하철 구간 삭제 요청
        ExtractableResponse<Response> 지하철역_제외_요청_응답1 = 지하철_노선에_지하철역_제외_요청(신분당선, 양재역);

        // Then 지하철 구간 삭제됨
        지하철_노선에_지하철역_제외됨(지하철역_제외_요청_응답1);

        // When 종점을 포함한 지하철 구간 삭제 요청
        ExtractableResponse<Response> 지하철역_제외_요청_응답2 = 지하철_노선에_지하철역_제외_요청(신분당선, 광교역);

        // Then 지하철 구간 삭제됨
        지하철_노선에_지하철역_제외됨(지하철역_제외_요청_응답2);

        // When 지하철 노선에 등록된 역 목록 조회 요청
        ExtractableResponse<Response> 지하철역_조회_요청_응답2 = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        // Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
        지하철_노선에_지하철역_순서_정렬됨(지하철역_조회_요청_응답2, Arrays.asList(강남역, 정자역));
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> response, List<StationResponse> expectedStations) {
        LineResponse line = response.as(LineResponse.class);
        List<Long> stationIds = line.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
