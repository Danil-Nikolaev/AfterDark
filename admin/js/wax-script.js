urlApi = "http://localhost:8080/api/afterdark/wax";
currentUrl = 'http://127.0.0.1:5500/admin/html/wax.html'

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
            updateCol = $('<a>').addClass("btn btn-dark btn-sm").attr('value', `${item.id}`).text('Редактировать')
            cell6 = $('<td>').append(updateCol);
            a = $('<a>').addClass("btn btn-dark btn-sm").attr('href',urlApi + `/${item.id}`).text('Удалить')
            cell7 = $('<td>').append(a);

            updateCol.on('click', function(event){
                event.preventDefault();
                id = $(this).attr('value')
                window.location.href = 'http://127.0.0.1:5500/admin/html/update-pages/wax-update.html' + `?id=${id}`;
            });

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

            newRow.append(cell1, cell2, cell3, cell4, cell5, cell6, cell7);
            $('#table-wax').append(newRow);
        });
    })
}