urlApi = "http://localhost:8080/api/afterdark/order";
currentUrl = 'http://127.0.0.1:5500/admin/html/order.html';

$(document).ready(function() {
    load_selecters();
    $("#my-form").submit(function(event) {
        event.preventDefault(); // Предотвращаем стандартную отправку формы

        // Отправляем AJAX-запрос
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
    urlUserApi = "http://localhost:8080/api/afterdark/user";
    urlCandleApi = "http://localhost:8080/api/afterdark/candle";

    user_selector = $("#users");
    candles_selector = $("#candles-selector");
    $.get(urlUserApi, function(data) {
        data.forEach(user => {
            option = $("<option>").attr("value", user.id).text(user.name);
            user_selector.append(option);
        });
    });

    $.get(urlCandleApi, function(data) {
        data.forEach(candle => {
            div = $("<div>").addClass("form-check");
            input = $("<input>").addClass("form-check-input").attr("type", "checkbox").attr("id", "candle_" + candle.id).attr('name', 'candles[]').val(candle.id);
            label = $("<label>").addClass("form-check-label").attr("for", candle.id).text(candle.name);
            div.append(input, label);
            candles_selector.append(div);
        });
    });
}