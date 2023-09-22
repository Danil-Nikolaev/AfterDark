urlApi = "http://localhost:8080/api/afterdark/gypsum";

$(document).ready(function() {
    console.log("Work");
    load_table();
});

function load_table() {
    $.get(urlApi, function(data){
        data.forEach(item => {
            newRow = $('<tr>');
            cell1 = $('<td>').text(item.id);
            cell2 = $('<td>').text(item.name);
            cell3 = $('<td>').text(item.description);
            cell4 = $('<td>').text(item.quanity);
            cell5 = $('<td>').text(item.price);

            newRow.append(cell1, cell2, cell3, cell4, cell5);
            $('#table-gypsum').append(newRow);
        });
    })
}