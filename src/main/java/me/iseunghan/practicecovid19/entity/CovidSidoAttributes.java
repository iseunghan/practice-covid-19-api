package me.iseunghan.practicecovid19.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 전국 시,도의 코로나19 감염현황의 데이터를 담는 Dto 클래스입니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CovidSidoAttributes {
    public Header header;
    public Body body;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        public String resultCode;
        public String resultMsg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Body {
        public List<Item> items = new ArrayList<>();
        public int numOfRows;
        public int pageNo;
        public int totalCount;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Item {
            public String createDt;
            public int deathCnt;
            public int defCnt;
            public String gubun;
            public String gubunCn;
            public String gubunEn;
            public int incDec;
            public int isolClearCnt;
            public int isolIngCnt;
            public int localOccCnt;
            public int overFlowCnt;
            public String qurRate;
            public int seq;
            public String stdDay;
            public String updateDt;
        }
    }
}
