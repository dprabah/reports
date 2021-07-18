$('#contacts-csv-form').submit(function(event) {
    const formElement = this;
    $.fn.fileSubmit(formElement);
    event.preventDefault();
});

$('#listings-csv-form').submit(function(event) {
    const formElement = this;
    $.fn.fileSubmit(formElement);
    event.preventDefault();
});

$.fn.fileSubmit = function (formElement) {
    const formData = new FormData(formElement);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/upload-csv",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            console.log(response);

        },
        error: function (error) {
            console.log(error);

        }
    });
};
