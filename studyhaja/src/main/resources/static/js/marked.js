$(function () {
   let mark = function () {
       let keyword = $('#keyword').text();
       let options = {
           "each": function (element) {
               setTimeout(function () {
                   $(element).addClass('animate');
               }, 150);
           }
       };

       $('.context').unmark({
           done: function () {
               $('.context').mark(keyword, options);
           }
       });
   };

   mark();
});