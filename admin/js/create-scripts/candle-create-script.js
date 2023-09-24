
$(document).ready(function() {
    load_selecters();

    $("#my-form").submit(function(event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы
        // Отправляем AJAX-запрос
        urlApi = "http://localhost:8080/api/afterdark/candle";
        currentUrl = 'http://127.0.0.1:5500/admin/html/candle.html';
        
        $.ajax({
            url: urlApi,
            method: 'POST',
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
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(4)").append(wick);
        });
    });

    $.get(waxApi, function(data) {
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(3)").append(wick);
        });
    });

    $.get(smellApi, function(data) {
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(2)").append(wick);
        });
    });

    $.get(shapeApi, function(data) {
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(5)").append(wick);
        });
    });

    $.get(colorShapeApi, function(data) {
        data.forEach(element => {
            wick = $("<option>").attr('value', element.id).text(element.name);
            $(".my-select:eq(1)").append(wick);
        });
    });
}