
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Rutam Khakov
  Date: 01.07.2017
  Time: 17:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="<c:url value="../resource/js/jquery.min.js" />"></script>
    <title>Title</title>
</head>
<body>
<input type="hidden" id = "id_tweet" value="1"/>
<input type="hidden" id = "id_author" value="0"/>
<input type="hidden" id = "id_date" value="0"/>
<input type="hidden" id = "id_text" value="0"/>
<input type="hidden" id = "id_favorite" value="0"/>
<input type="hidden" id = "id_retweet" value="0"/>
<table>
    <thead>
    <tr>
        <th><a href="" id="tweet_id" onclick="OrderById();return false;">Id</a></th>
        <th><a href="" id = "author" onclick="OrderByAuthor();return false;">Автор</a></th>
        <th><a href="" id = "tweet_date" onclick="OrderByDate();return false;">Дата</a></th>
        <th><a href="" id = "tweet_text" onclick="OrderByText();return false;">Текст</th>
        <th><a href="" id = "favorite" onclick="OrderByFavorite();return false;">Фаорит</a></th>
        <th><a href="" id = "retweet" onclick="OrderByRetweet();return false;">Ретвит</a></th>
    </tr>
    </thead>
<form onsubmit="return false;">
    <input type="text" onchange="Searching();return false;" id = "search_text">
</form>
<tbody id="res">
<c:forEach var="tweet" items="${tweets}">
    <tr>
        <th>${tweet.getId()}</th>
        <th>${tweet.getAuthor()}</th>
        <th>${tweet.getDate()}</th>
        <th>${tweet.getText()}</th>
        <th>${tweet.getFavoriteCount()}</th>
        <th>${tweet.getRetweetCount()}</th>
    </tr>
</c:forEach>
</tbody>
</table>
<script type="application/javascript">
    Order = function (orderId, orderName) {
        val_1 = $("#"+orderId).val();
        if(val_1 == 1){
            $("#"+orderId).val(0);
        }else
        {
            $("#"+orderId).val(1);
        }

        Search(orderName + val_1);
    };
    OrderById = function(){
        Order("id_tweet","id");
    };
    OrderByAuthor = function(){
        Order("id_author","au");
    };
    OrderByDate = function(){
        Order("id_date","da");
    };
    OrderByText = function(){
        Order("id_text","te");
    };
    OrderByFavorite = function(){
        Order("id_favorite","fa");
    };
    OrderByRetweet = function(){
        Order("id_retweet","re");
    };
    Searching = function(){
        val_1 = $("#search_text").val();
        Search("2" + val_1);
    };

    Search = function (value1) {
        $.ajax({
            url: "/search",
            data: {"q": value1},
            dataType: "json",
            success: function (response_data) {
                if (response_data.length > 0) {
                    $("#res").html("");
                    for (var i = 0; i < response_data.length; i++) {
                        $("#res").append("<tr><td>"+response_data[i].id +"</td><td>"+
                            response_data[i].author +"</td><td>"+ new Date(response_data[i].date) +
                            "</td><td>"+response_data[i].text + "</td><td>"
                            + response_data[i].favoriteCount  +"</td><td>"
                            + response_data[i].retweetCount + "</td></tr>");
                    }
                } else {
                    $("#res").html("");
                }
            }
        });
    }
</script>
</body>
</html>
