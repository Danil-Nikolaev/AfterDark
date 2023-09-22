urlApi = "http://localhost:8080/api/afterdark/candle";

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
            cell6 = $('<td>').text(item.custom);
            cell7 = $('<td>').text(item.colorShape.name);
            cell8 = $('<td>').text(item.smell.name);
            cell9 = $('<td>').text(item.wax.name);
            cell10 = $('<td>').text(item.wick.name);
            cell11 = $('<td>').text(item.shape.name);
            newRow.append(cell1, 
                cell2, cell3, cell4, cell5, cell6, 
                cell7, cell8, cell9, cell10, cell11
                );
            $('#table-candle').append(newRow);
        });
    })
}