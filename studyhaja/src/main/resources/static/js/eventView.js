$(function () {
   $('[data-toggle="tooltip"]').tooltip();

   moment.locale('ko');

    $(".date-time").text(function(index, dateTime) {
        return moment(dateTime, "YYYY-MM-DD`T`hh:mm").format('LLL');
    });
    $(".date").text(function(index, dateTime) {
        return moment(dateTime, "YYYY-MM-DD`T`hh:mm").format('LL');
    });
    $(".weekday").text(function(index, dateTime) {
        return moment(dateTime, "YYYY-MM-DD`T`hh:mm").format('dddd');
    });
    $(".time").text(function(index, dateTime) {
        return moment(dateTime, "YYYY-MM-DD`T`hh:mm").format('LT');
    });
});