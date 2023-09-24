urlApi = "http://localhost:8080/api/afterdark/colorshape";
currentUrl = 'http://127.0.0.1:5500/admin/html/color-shape.html';

$(document).ready(function() {
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