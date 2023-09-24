
function getURLParameters() {
    var searchParams = new URLSearchParams(window.location.search);
    var params = {};
    searchParams.forEach(function (value, key) {
        params[key] = value;
    });
    return params;
}

$(document).ready(function() {
    load_selecters();

    urlApi = "http://localhost:8080/api/afterdark/candle";
    params = getURLParameters();
    id = params['id']
    $.getJSON(urlApi + `/${id}`, function(data){
        $("#name").val(data.name);
        $("#description").val(data.description);
        $("#quanity").val(data.quanity);
        $("#price").val(data.price);
        $("#custom").val(data.custom.toString());

        if (data.colorShape !== null) {
            $("#color-shape").val(data.colorShape.id.toString());
        }

        if (data.smell !== null) {
            $("#smell").val(data.smell.id.toString());
        }

        if (data.wax !== null) {
            $("#wax").val(data.wax.id.toString());
        }
        
        if (data.wick !== null) {
            $("#wick").val(data.wick.id.toString());
        }
        
        if (data.shape !== null) {
            $("#shape").val(data.shape.id.toString());
        }
        
    });

    $("#my-form-update").submit(function(event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы
        // Отправляем AJAX-запрос
        params = getURLParameters();
        id = params['id']
        urlApi = "http://localhost:8080/api/afterdark/candle";
        currentUrl = 'http://127.0.0.1:5500/admin/html/candle.html';
        
        $.ajax({
            url: urlApi + `/${id}`,
            method: 'PUT',
            data: $(this).serialize(),
            success: function(response) {
                // Перенаправляем пользователя на другую страницу
                window.location.href = currentUrl;
            },
            error: function(xhr, status, error) {
                // Обработка ошибки
                console.error('Произошла ошибка:', error);
            }
        });
    });
})

function load_selecters() {
    const wickApi = "http://localhost:8080/api/afterdark/wick";
    const waxApi = "http://localhost:8080/api/afterdark/wax";
    const smellApi = "http://localhost:8080/api/afterdark/smell";
    const shapeApi = "http://localhost:8080/api/afterdark/shape";
    const colorShapeApi = "http://localhost:8080/api/afterdark/colorshape";


    $.get(wickApi, function(data){
        data.sort(function(a, b) {
            return a.id - b.id;
        });
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(4)").append(wick);
        });
    });

    $.get(waxApi, function(data) {
        data.sort(function(a, b) {
            return a.id - b.id;
        });
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(3)").append(wick);
        });
    });

    $.get(smellApi, function(data) {
        data.sort(function(a, b) {
            return a.id - b.id;
        });
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(2)").append(wick);
        });
    });

    $.get(shapeApi, function(data) {
        data.sort(function(a, b) {
            return a.id - b.id;
        });
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(5)").append(wick);
        });
    });

    $.get(colorShapeApi, function(data) {
        data.sort(function(a, b) {
            return a.id - b.id;
        });
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(1)").append(wick);
        });
    });
}