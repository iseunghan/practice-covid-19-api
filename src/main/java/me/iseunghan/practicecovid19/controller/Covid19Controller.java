package me.iseunghan.practicecovid19.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import me.iseunghan.practicecovid19.entity.Response;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 21.05.24 월 : 코로나 api 호출 후 응답 결과까지 얻기 완료.
 * 21.05.25 화 : XmlMapper 의존성 추가 후 데이터 매핑 완료.
 */
@Controller
@RequestMapping(value = "/covid19-api")
public class Covid19Controller {

    @GetMapping
    @ResponseBody
    public String covid_api_Test() throws UnsupportedEncodingException, URISyntaxException {
        RestTemplate rt = new RestTemplate();
        // RestTemplate의 MessageConverter가 기본 Charset이 UTF-8이 아니여서 설정해줘야 함!
        rt.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        // 혹시 모르니, 헤더에도 charset=UTF-8로 설정해준다.
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.valueOf("application/xml;charset=UTF-8");
        headers.setContentType(mediaType);

        StringBuilder urlBuilder = new StringBuilder("http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson");
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=8dg1oO8E7c1WbwllHQfIkNvUulM0K%2Fbg7dzt25DwEgO3R9Cv6w10zlsQ269Tq5pQA3XtPziQLkXCzW4ftvTGzw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("startCreateDt","UTF-8") + "=" + URLEncoder.encode("20200410", "UTF-8")); /*검색할 생성일 범위의 시작*/
        urlBuilder.append("&" + URLEncoder.encode("endCreateDt","UTF-8") + "=" + URLEncoder.encode("20200410", "UTF-8")); /*검색할 생성일 범위의 종료*/
        URI uri = new URI(urlBuilder.toString());

        HttpEntity http = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                uri,
                HttpMethod.GET,
                http,
                String.class
        );

        return "응답 결과 : " + response.getBody();
    }
}
