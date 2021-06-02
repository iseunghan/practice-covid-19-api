package me.iseunghan.practicecovid19.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import me.iseunghan.practicecovid19.entity.CovidAttributes;
import me.iseunghan.practicecovid19.entity.CovidSidoAttributes;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDate;

/**
 * 21.05.24 월 : 코로나 api 호출 후 응답 결과까지 얻기 완료.
 * 21.05.25 화 : XmlMapper 의존성 추가 후 데이터 매핑 완료.
 */
@RestController
@RequestMapping(value = "/covid19-api")
public class Covid19Controller {

    static String covid19Inf = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson";
    static String covid19SidoInf = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";

    /**
     * 전국 시,도의 코로나19 감염현황을 제공해줍니다.
     */
    @GetMapping("/local")
    public ResponseEntity get_covid19_sido_info() throws UnsupportedEncodingException {
        RestTemplate rt = new RestTemplate();
        // RestTemplate의 MessageConverter가 기본 Charset이 UTF-8이 아니여서 설정해줘야 함!
        rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        // 혹시 모르니, 헤더에도 charset=UTF-8로 설정해준다.
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.valueOf("application/xml;charset=UTF-8");
        headers.setContentType(mediaType);

        URI covidRequestURI = getCovidRequestURI(covid19SidoInf, 1);

        HttpEntity http = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                covidRequestURI,
                HttpMethod.GET,
                http,
                String.class
        );

        // ObjectMapper를 상속받는 XmlMapper를 선언
        ObjectMapper xmlMapper = new XmlMapper();
        CovidSidoAttributes covidSidoAttributes = null;
        try {
            covidSidoAttributes = xmlMapper.readValue(response.getBody(), CovidSidoAttributes.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(covidSidoAttributes);
    }

    /**
     * 현재 날짜 기준으로 국내 코로나19 감염현황을 조회힙니다.
     * - 총 2개의 조회 데이터
     * - item[0] : 현재일 감염현황
     * - item[1] : 이전일 감염현황
     */
    @GetMapping("/domestic-status")
    public ResponseEntity get_covid19_info() throws UnsupportedEncodingException {
        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.valueOf("application/xml;charset=UTF-8");
        headers.setContentType(mediaType);

        URI covidRequestURI = getCovidRequestURI(covid19Inf, 1);

        HttpEntity http = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                covidRequestURI,
                HttpMethod.GET,
                http,
                String.class
        );

        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        CovidAttributes covidAttributes = null;
        try {
            covidAttributes = xmlMapper.readValue(response.getBody(), CovidAttributes.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(covidAttributes);
    }

    /**
     * [전국 감염현황] 현재 일자를 기준으로 7일 전까지 감염현황을 조회합니다.
     * - items[0] 부터 최신 날짜 데이터임.
     */
    @GetMapping("/domestic-status-7")
    public ResponseEntity get_covid19_info_7days() throws UnsupportedEncodingException {
        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.valueOf("application/xml;charset=UTF-8");
        headers.setContentType(mediaType);

        URI covidRequestURI = getCovidRequestURI(covid19Inf, 6);

        HttpEntity http = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                covidRequestURI,
                HttpMethod.GET,
                http,
                String.class
        );

        // ObjectMapper를 상속받는 XmlMapper를 선언
        ObjectMapper xmlMapper = new XmlMapper();
        CovidAttributes covidAttributes = null;
        try {
            covidAttributes = xmlMapper.readValue(response.getBody(), CovidAttributes.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(covidAttributes);
    }

    /**
     * 조회 시작일, 종료일에 대해서 요청 URI 생성해주는 메소드
     *
     * @param days 현재로부터 {days}일전까지 조회
     * @return
     */
    private URI getCovidRequestURI(String url, int days) throws UnsupportedEncodingException {
        // days
        LocalDate now = LocalDate.now();
        LocalDate yst = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() - days);
        String startCreateDt = yst.toString().replaceAll("-", "");
        String endCreateDt = now.toString().replaceAll("-", "");

        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=8dg1oO8E7c1WbwllHQfIkNvUulM0K%2Fbg7dzt25DwEgO3R9Cv6w10zlsQ269Tq5pQA3XtPziQLkXCzW4ftvTGzw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt", "UTF-8") + "=" + URLEncoder.encode(startCreateDt, "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt", "UTF-8") + "=" + URLEncoder.encode(endCreateDt, "UTF-8")); /*검색할 생성일 범위의 종료*/
        URI uri = URI.create(urlBuilder.toString());
        return uri;
    }
}
