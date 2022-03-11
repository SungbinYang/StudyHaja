$(function () {
    function tagRequest(url, tagTitle) {
        $.ajax({
            dataType: 'json',
            autocomplete: {
                enabled: true,
                rightKey: true,
            },
            contentType: 'application/json; charset=utf-8',
            method: 'POST',
            url: '/settings/zones' + url,
            data: JSON.stringify({'zoneName': tagTitle})
        }).done(function (data, status) {
            console.log('${data} and status is ${status}');
        });
    }

    function onAdd(e) {
        tagRequest("/add", e.detail.data.value);
    }

    function onRemove(e) {
        tagRequest("/remove", e.detail.data.value);
    }

    let tagInput = document.querySelector('#tags');

    let tagify = new Tagify(tagInput, {
        enforceWhitelist: true,
        whitelist: JSON.parse(document.querySelector('#whitelist').textContent),
        dropdown: {
            enabled: 1,
        }
    });

    tagify.on('add', onAdd);
    tagify.on('remove', onRemove);

    tagify.DOM.input.classList.add('form-control');
    tagify.DOM.scope.parentNode.insertBefore(tagify.DOM.input, tagify.DOM.scope);
});