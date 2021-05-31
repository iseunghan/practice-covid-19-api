package me.iseunghan.practicecovid19.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 전국 코로나19 감염현황 데이터를 담는 Dto 클래스입니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CovidAttributes {
        public Header header;
        public Body body;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Header{
            String resultCode;
            String resultMsg;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Body{
            public List<Item> items = new ArrayList<>();
            public int numOfRows;
            public int pageNo;
            public int totalCount;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Item{
                double accDefRate;
                int accExamCnt;
                int accExamCompCnt;
                int careCnt;
                int clearCnt;
                String createDt;
                int deathCnt;
                int decideCnt;
                int examCnt;
                int resutlNegCnt;
                int seq;
                String stateDt;
                String stateTime;
                String updateDt;
            }
        }
    }
