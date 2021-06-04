
$(function (){
    /**
     * 화면 로드 시, 제일 상단 부분 (국내 누적확진자 , 전일대비 증가) 데이터 삽입
     */
    $.ajax({
        url : "http://localhost:8080/covid19-api/domestic-status",
        type : "GET",
        contentType: "application/json",

        success : function (result){
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