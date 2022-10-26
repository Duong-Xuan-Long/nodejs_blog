$('document').ready(function (){
    $('#editModal').modal({backdrop: 'static', keyboard: false})
    $('table #btnEdit').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (category, status){
            $('#idModal').val(category.id);
            $('#nameModal').val(category.name);
        });
        $('#editModal').modal('show');
        $('#editModal').on('hidden.bs.modal', function(){
            $('.modal-backdrop').remove();
        })
    });

});
