urlApi = "http://localhost:8080/api/afterdark/colorshape";
currentUrl = 'http://127.0.0.1:5500/admin/html/color-shape.html';

function getURLParameters() {
    var searchParams = new URLSearchParams(window.location.search);
    var params = {};
    searchParams.forEach(function (value, key) {
        params[key] = value;
    });
    return params;
}

$(document).ready(function() {

    params = getURLParameters();
    id = params['id'];
    $.getJSON(urlApi + `/${id}`, function(data){
        $("#name").val(data.name);
        $("#description").val(data.description);
        $("#price").val(data.price);
    });

    $("#my-form-update").submit(function(event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы

        params = getURLParameters();
        id = params['id'];
        // Отправляем AJAX-запрос
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