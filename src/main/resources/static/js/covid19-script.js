
$(function () {
    var $domesticStatus = $('#domestic-cumulative-status');
    var $7daysGraph = $('#7days-graph');
    var $daysProperties = $('#7days-properties');

    var graph_Template = "<div class=\"progress\" style=\"left: {{num}}%;\">\n" +
        "                <div class=\"progress-bar bg-danger\" role=\"progressbar\" style=\"top: {{top}}%; width: 100%; height: {{height}}%\" aria-valuenow=\"{{count}}\" aria-valuemin=\"0\"\n" +
        "                     aria-valuemax=\"100\">\n" +
        "                </div>\n" +
        "                <span class=\"text\" count-value=\"{{count}}\"></span>\n" +
        "            </div>";

    var date_Template = "<span class=\"day-property\" id=\"date-{{dataId}}\" style=\"left: {{num}}%\">{{date}}</span>";

    /**
     * 현재 날짜 기준으로 확진자 감염현황을 렌더링 해주는 함수
     */
    function renderDomesticStatus() {

    }

    /**
     * 7일간 확진자 감염현황 그래프 렌더링 함수
     */
    function renderGraph(num, count) {
        var per = count / 10;
        var data = {
            num : num * 12,
            top : (100-per),
            height : per,
            count : count
        }
        $7daysGraph.append(Mustache.render(graph_Template, data));
    }

    /**
     * 7일간의 날짜를 렌더링 해주는 함수
     */
    function renderDate(dataId, num, date){
        var data = {
            dataId : dataId,
            num : (num * 12),
            date : date
        };

        $daysProperties.append(Mustache.render(date_Template, data));
    }

    /**
     * 화면 로드 시, 제일 상단 부분 (국내 누적확진자 , 전일대비 증가) 데이터 삽입
     */
    $.ajax({
        url: "http://localhost:8080/covid19-api/domestic-status",
        type: "GET",
        contentType: "application/json",

        success: function (result) {
            // <-- 맨 위 상단 누적확진자, 전일대비 확진자 표시 -->
            const list = result.body.items;
            var now_decideCnt = list[0].decideCnt;
            var prev_decideCnt = list[1].decideCnt;
            var diff_decideCnt = increaseOrDecrease(now_decideCnt - prev_decideCnt);
            $('.decide-patient-count').text(now_decideCnt.toLocaleString()); // toLocalString() -> Number 형식의 숫자를 소숫점 3자리마다 끊어주는 내장함수!
            $('.before-decide-patient-count').text(diff_decideCnt.toLocaleString());

            // <-- 총 검사중 -->
            var now_examCnt = list[0].examCnt;
            var prev_examCnt = list[1].examCnt;
            var diff_examCnt = increaseOrDecrease(now_examCnt - prev_examCnt);
            $('#total-exam-count').text(now_examCnt.toLocaleString());
            $('#before-exam-count').text(diff_examCnt.toLocaleString());

            // <-- 총 사망자 -->
            var now_deathCnt = list[0].deathCnt;
            var prev_deathCnt = list[1].deathCnt;
            var diff_deathCnt = increaseOrDecrease(now_deathCnt - prev_deathCnt);
            $('#total-death-count').text(now_deathCnt.toLocaleString());
            $('#before-death-count').text(diff_deathCnt.toLocaleString());

            // <-- 총 격리해제 -->
            var now_clearCnt = list[0].clearCnt;
            var prev_clearCnt = list[1].clearCnt;
            var diff_clearCnt = increaseOrDecrease(now_clearCnt - prev_clearCnt);
            $('#total-clear-count').text(now_clearCnt.toLocaleString());
            $('#before-clear-count').text(diff_clearCnt.toLocaleString());
        }
    })

    /**
     * 화면 로드 시, 7일간 확진자 감염현황 데이터 삽입
     */
    $.ajax({
        url: "http://localhost:8080/covid19-api/domestic-status-7",
        type: "GET",
        contentType: "application/json",

        success: function (result) {
            var items = result.body.items;
            console.log(result);

            for (var i = items.length-1; i > 0; i--) {
                var date = items[i-1].stateDt.substring(4,6) + '.' + items[i-1].stateDt.substring(6);
                var diff = items[i-1].decideCnt - items[i].decideCnt;
                renderDate(8-i, 8-i, date);
                renderGraph(8 - i, diff);
            }
        }
    });

});
/*
    전일 대비 카운트의 값이 증가인지 감소인지 체크해주는 함수
 */
function increaseOrDecrease(count) {
    var resultTxt = '';
    var format_num = count.toLocaleString();
    if(count > 0){
        resultTxt = format_num + '↑';
    }else{
        resultTxt = (count * (-1)).toLocaleString() + '↓';
    }
    return resultTxt;
}