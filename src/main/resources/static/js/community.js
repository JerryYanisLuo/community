function post()
{
    var questionId = $("#question_id").val();
    var content = $("#comment_id").val();

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parent_id":questionId,
            "reply": null,
            "content":content,
            "type": 1
        }),
        success: function (response){
            if(response.code == 200)
            {
                //请求成功
                window.location.reload();
            }
            else if(response.code == 2003)
            {
                //没有登录
                if(confirm(response.message))
                {
                    window.close();
                    window.open("https://github.com/login/oauth/authorize?client_id=332a3737971e29bdf5f1&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                    window.localStorage.setItem("question_id", questionId);
                    window.localStorage.setItem("close", "true");
                }
            }
            else if(response.code == 2007)
            {
                //评论为空
                alert(response.message);
            }
        },
        dataType: "json"
    });

}

var par = 0;
var re = 0;
function subReply(e)
{

    var main_comm_id = e.getAttribute("data-id");
    var comment = $("#comment_section"+main_comm_id);
    comment.addClass("in");

    var reply = e.getAttribute("reply");
    console.log("main comment "+main_comm_id);
    console.log("sub comment "+reply);
    par = main_comm_id;
    re = reply;
}

function postSub()
{
    var questionId = $("#question_id").val();
    var content = $("#sub_comment_content"+par).val();

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parent_id":par,
            "reply": re,
            "content":content,
            "type": 2
        }),
        success: function (response){
            if(response.code == 200)
            {
                //请求成功
                window.location.reload();
            }
            else if(response.code == 2003)
            {
                //没有登录
                if(confirm(response.message))
                {
                    window.close();
                    window.open("https://github.com/login/oauth/authorize?client_id=332a3737971e29bdf5f1&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                    window.localStorage.setItem("question_id", questionId);
                    window.localStorage.setItem("close", "true");
                }
            }
            else if(response.code == 2007)
            {
                //评论为空
                console.log(re);
                alert(response.message);
            }
        },
        dataType: "json"
    });
}


function jump(e)
{
    var id =  e.getAttribute("commid");
    location.href = $("#{id}");
    console.log("jump");
}


