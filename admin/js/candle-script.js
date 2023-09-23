urlApi = "http://localhost:8080/api/afterdark/candle";
currentUrl = 'http://127.0.0.1:5500/admin/html/candle.html';

$(document).ready(function() {
    console.log("Work");
    load_table();
});

function load_table() {
    $.get(urlApi, function(data){
        data.sort(function(a, b) {
            return a.id - b.id;
        });
        
        data.forEach(item => {
            newRow = $('<tr>');
            cell1 = $('<td>').text(item.id);
            cell2 = $('<td>').text(item.name);
            cell3 = $('<td>').text(item.description);
            cell4 = $('<td>').text(item.quanity);
            cell5 = $('<td>').text(item.price);
            cell6 = $('<td>').text(item.custom);
            if (item.colorShape === null) {
                cell7 = $('<td>').text("Пусто");
            } else {
                cell7 = $('<td>').text(item.colorShape.name);
            }

            if (item.smell === null) {
                cell8 = $('<td>').text("Пусто");
            } else {
                cell8 = $('<td>').text(item.smell.name);
            }

            if (item.wax === null) {
                cell9 = $('<td>').text("Пусто");
            } else {
                cell9 = $('<td>').text(item.wax.name);
            }

            if (item.wick === null) {
                cell10 = $('<td>').text("Пусто");
            } else {
                cell10 = $('<td>').text(item.wick.name);
            }
            
            if (item.shape === null) {
                cell11 = $('<td>').text("Пусто");
            } else {
                cell11 = $('<td>').text(item.shape.name);
            }

            cell12 = $('<td>').append($('<a>').addClass("btn btn-dark btn-sm").attr('href',urlApi + `/${item.id}`).text('Редактировать'));
            a = $('<a>').addClass("btn btn-dark btn-sm").attr('href',urlApi + `/${item.id}`).text('Удалить')
            cell13 = $('<td>').append(a);
            a.on('click', function(event) {
                event.preventDefault(); // Предотвращаем переход по ссылке
                var clickedLink = event.target.href; 
                $.ajax({
                    url: clickedLink, // Замените на URL вашего API-ресурса
                    type: 'delete',
                    success: function(result) {
                        // Обработка успешного выполнения запроса DELETE
                        window.location.href = currentUrl;
                        console.log('DELETE-запрос выполнен успешно');
                    },
                    error: function(xhr, status, error) {
                        // Обработка ошибок при выполнении запроса DELETE
                        console.error('Произошла ошибка при выполнении DELETE-запроса:', error);
                    }
                })
            });

            newRow.append(cell1, 
                cell2, cell3, cell4, cell5, cell6, 
                cell7, cell8, cell9, cell10, cell11, cell12, cell13
                );
            $('#table-candle').append(newRow);
        });
    })
}