// initialization of element of the website
$(function () {
    'use strict';

    var topDownloads = new TopDownloads(applicationPath);
    topDownloads.init();

    var rating = new Rating(applicationPath);
    rating.init();

});