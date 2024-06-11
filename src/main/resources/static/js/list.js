$(function (){
    //페이징 헤더
    $("[name='pageRows']").change(function (){
        // alert($(this).val())//내가 누른 숫자 확인용

        $("[name='frmPageRows']").attr({
            "method": "POST",
            "action": "/board/pageRows",
        }).submit();


    });
});