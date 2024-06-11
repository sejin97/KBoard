$(function (){
    //[추가] 버튼 누르면 첨부 파일 추가 가능
    let i = 0;
    $('#btnAdd').click(function () {
        $("#files").append(`
        <div class="input-group mb-2">
       <input class="form-control col-xs-3" type="file" name="upfile${i}"/>    <!--서로 다른 name 값을 줘야한다 그래서 i 변수 선언(중복 금지)-->
       <button type="button" class="btn btn-outline-danger" onclick="$(this).parent().remove()">삭제</button>
        </div>`);
        i++;
    });

    //Summernote추가
    $("#content").summernote({
        height: 300,
    });
})