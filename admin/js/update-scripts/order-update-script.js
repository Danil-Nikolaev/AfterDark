urlApi = "http://localhost:8080/api/afterdark/order";
currentUrl = 'http://127.0.0.1:5500/admin/html/order.html';
var candlesArrayElements = [];
function getURLParameters() {
    var searchParams = new URLSearchParams(window.location.search);
    var params = {};
    searchParams.forEach(function (value, key) {
        params[key] = value;
    });
    return params;
}

function load_selecters() {

}


$(document).ready(function() {

    urlCandleApi = "http://localhost:8080/api/afterdark/candle";
    candles_selector = $("#candles-selector");
    $.get(urlCandleApi, function(data) {
        data.forEach(candle => {
            div = $("<div>").addClass("form-check");
            input = $("<input>").addClass("form-check-input").attr("type", "checkbox").attr("id", `candle_${candle.id}`).attr('name', 'candles[]').addClass('candles-array').val(candle.id);
            label = $("<label>").addClass("form-check-label").attr("for", candle.id).text(candle.name);
            div.append(input, label);
            candles_selector.append(div);
        });
        
        params = getURLParameters();
        id = params['id']
        $.getJSON(urlApi + `/${id}`, function(data){
            $("#communication").val(data.communication);
            $("#address").val(data.address);
            $("#price").val(data.price);
            $("#dateOfPurchase").val(data.dateOfPurchase);
            $("#dateOfDelivery").val(data.dateOfDelivery);
            $("#purchaseService").val(data.purchaseService);
            $("#paymentMethod").val(data.paymentMethod);
            $("#stageOfWork").val(data.stageOfWork);
            $("#paid").val(data.paid.toString());
            candles = data.candles;
            $("#count-candles").text(candles.length);
            sum_price = 0;
            candles.forEach(candle => {
                li = $("<li>").addClass("list-group-item d-flex justify-content-between lh-sm");
                div = $("<div>");
                h6 = $("<h6>").addClass("my-0").text(candle.name);
                small = $("<small>").addClass("text-muted").text("Краткое описание");
                span = $("<span>").addClass("text-muted").text(candle.price);
                div.append(h6, small);
                li.append(div, span);
                $("#candles").append(li);
                sum_price += candle.price;
                $("#candle_" + candle.id).prop('checked', true)
            });
            li = $("<li>").addClass("list-group-item d-flex justify-content-between");
            span = $("<span>").text("Всего (РУБ)");
            strong = $("<strong>").text(sum_price);
            li.append(span, strong);
            $("#candles").append(li);
            $("#user").text(data.user.name);
        });
    
        $("#my-form-update").submit(function(event) {
            event.preventDefault(); // Предотвращаем стандартную отправку формы
    
            params = getURLParameters();
            id = params['id']
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
    });

})

