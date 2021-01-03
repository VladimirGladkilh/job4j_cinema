<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Halls" %>
<%@ page import="store.PsqlStore" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
<head>
    <%
        Collection<Halls> hallsCollection = PsqlStore.instOf().findAllHalls();
    %>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>Работа мечты</title>
    <link rel="icon" type="image/png" href="favicon.ico"/>
    <script>function takeData() {
        var allInputs = $("input[type=checkbox]").filter(':checked:enabled');
        var selected = '';
        var names = '';
        for (var i = 0, max = allInputs.length; i < max; i++){
                if (selected != '') {
                    selected += ';';
                    names += ';';
                }
                selected += allInputs[i].id;
                names += allInputs[i].value;
        }
        console.log(selected);
        var kol = selected.split(';').length;
        var texts = "Выбрано мест: "+ kol + "<br>На сумму: " + (500 * kol) + "<br> Выбраны места: " + names;
        document.getElementById("calc").innerHTML = texts;
        document.getElementById("placesIds").setAttribute("value", selected);
    }

    var mainHallId=1;


        function starter(hallId) {
            mainHallId = hallId;
            openHall();
        };
        function openHall() {
            console.log("run " + mainHallId);
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/cinema/places.do",
                data: "hallId=" + mainHallId,
                dataType: 'text',
                origin: "http://localhost:8081"
            })
                .done(function (data) {
                    document.getElementById("cardbody").innerHTML= data;
                })
                .fail(function (err) {
                    alert("err" + err.message);
                })
            setTimeout(function(){
                openHall();
            }, 60000);
        };
    </script>
</head>
<style>
    body {
        font-family: "Lato", sans-serif;
    }

    .sidenav {
        width: 130px;
        position: fixed;
        z-index: 1;
        top: 20px;
        left: 10px;
        background: #eee;
        overflow-x: hidden;
        padding: 8px 0;
    }

    .sidenav a {
        padding: 6px 8px 6px 16px;
        text-decoration: none;
        font-size: 25px;
        color: #2196F3;
        display: block;
    }

    .sidenav a:hover {
        color: #064579;
    }

    .main {
        margin-left: 140px; /* Same width as the sidebar + left position in px */
        font-size: 28px; /* Increased text to enable scrolling */
        padding: 0px 10px;
    }

    @media screen and (max-height: 450px) {
        .sidenav {padding-top: 15px;}
        .sidenav a {font-size: 18px;}
    }
</style>
<body onload="openHall()">


<script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

<div class="sidenav">
    <label>Залы</label>
    <% for (Halls hall : hallsCollection) { %>
    <a onclick="starter('<%=hall.getId()%>')"><%=hall.getName()%></a>
    <% } %>
</div>
<div class="main" id="cardbody">

</div>
<dic class="main" is="btn_place">
<button type="button" class="btn btn-info btn-lg" data-toggle="modal" onclick="takeData()" data-target="#myModal">Оплатить</button>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <form action="<%=request.getContextPath()%>/payment.do" method="post">
                <div class="form-group">
                    <label id="calc" >Тут будет инфа о покупке</label><br>
                    <input type="text" class="form-control" name="places" id="placesIds" value="">
                    <label>Имя</label>
                    <input type="text" class="form-control" name="name" value="">
                    <label>Телефон</label>
                    <input type="text" class="form-control" name="phone" value="">
                </div>
                <button type="submit" class="btn btn-primary">Оплатить</button>
            </form>
        </div>
    </div>
</div>
</div>
</dic>
</body>
</html>
