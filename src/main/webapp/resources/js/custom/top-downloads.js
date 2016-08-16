'use strict';

var TopDownloads = function () {
};

TopDownloads.prototype = function () {

    var topDownloadsDiv = $('#top-downloads'),
        topDownloadsLoader = $('#top-downloads-loader'),
        programsUrl = 'http://jquery-ui-project.esy.es/software_store/programs/',
        programDetailsUrl = '/details',
        restApiUrl = '/rest/top/get',
        errorMsg = 'Top downloads unavailable',
        timeout = 10000,

        init = function () {
            topDownloadsLoader.show();
            getJson()
                .done(function (data) {
                    topDownloadsDiv.html(generateHtml(data));
                    topDownloadsDiv.slick({
                        autoplay: true,
                        slidesToShow: 4,
                        slidesToScroll: 2,
                        dots: true
                    });
                })
                .fail(function () {
                    topDownloadsDiv.html(errorMsg);
                })
                .always(function () {
                    topDownloadsLoader.hide();
                });
        },

        generateHtml = function (data) {
            var html = '';
            $.each(data, function (index, program) {
                html += '<div>'
                    + '<a href="' + programDetailsUrl + '/' + program.id + '">'
                    + '<img src="' + program.img512Url + '">' + '<br/>'
                    + '</a>'
                    + program.name + '<br/>'
                    + 'Downloads: ' + program.downloads
                    + '</div>';
            });
            return html;
        },

        getJson = function () {
            return $.ajax({
                url: restApiUrl,
                dataType: "json",
                timeout: timeout
            });
        };

    return {
        init: init
    };

}();

