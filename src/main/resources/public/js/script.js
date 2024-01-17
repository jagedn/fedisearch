function load_data(search) {
    // Show loader
    $('#loader').removeClass('d-none')
        // Selecting the table Element
    var table = $('#table-list')
        // Emptying the Table items
    table.find('tbody').html('')
    $.ajax({
        url:"api/search",
        type:"POST",
        data: JSON.stringify({search: search}),
        contentType:"application/json; charset=utf-8",
        dataType:"json",
        success: function(resp){
            if (resp.length > 0) {
                // If returned json data is not empty
                var i = 1;
                // looping the returned data
                Object.keys(resp).map(k => {
                    // creating new table row element
                    var tr = $('<tr>')
                    // first column data
                    tr.append('<td class="py-1 px-2 text-center">' + (i++) + '</td>')
                    // second column data
                    var url = `https://${resp[k].instance}/@${resp[k].username}`
                    var anchor = `<a target="_blank" href="${url}">${resp[k].username}</a>`
                    tr.append('<td class="py-1 px-2">' + anchor + '</td>')
                    // third column data
                    tr.append('<td class="py-1 px-2">' + resp[k].instance + '</td>')
                    // fourth column data
                    tr.append('<td class="py-1 px-2">' + resp[k].note + '</td>')
                    // Append table row item to table body
                    table.find('tbody').append(tr)
                })
            } else {
                // If returned json data is empty
                var tr = $('<tr>')
                tr.append('<th class="py-1 px-2 text-center">No data to display</th>')
                table.find('tbody').append(tr)
            }
            $('#loader').addClass('d-none')
        }
    });
}

function updateStats(){
    $.get("/api/stats",function(resp){
        var current = resp[0];
        console.log(current)
        $('#instances').text(current.instances)
        $('#users').text(current.users)
    });
}

$(function() {
    // Hide loader on document ready
    $('#loader').addClass('d-none')
    var table = $('#table-list')
    table.find('tbody').html('')

    $("#target").on("submit", function (event) {
        event.preventDefault();
        if ($("#search").val().length > 3) {
            load_data($("#search").val())
        }
    });

    updateStats()
})