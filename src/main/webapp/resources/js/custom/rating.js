'use strict';

var Rating = function () {
};

Rating.prototype = function () {

    var ratingDiv = $('#rating'),
        ratingMsgSpan = $('#rating-msg'),
        jspProgramIdAttribute = 'data-program-id',
        programId = ratingDiv.attr(jspProgramIdAttribute),
        submitRatingUrl = '/rating/add',
        submitSuccessMsg = 'Thank you for your vote. Your rating is: ',
        submitErrorMsg = 'Unable to submit rating. Please try again later.',
        timeout = 10000,

        init = function () {
            ratingDiv.rateYo({
                halfStar: true,
                onSet: function (rating, rateYoInstance) {
                    ratingDiv.rateYo("option", "readOnly", true);
                    sendRating(programId, rating)
                        .done(function () {
                            showMessage(submitSuccessMsg + rating);
                        })
                        .fail(function () {
                            showMessage(submitErrorMsg);
                        });
                }
            });
        },

        sendRating = function (programId, rating) {
            return $.ajax({
                method: "POST",
                url: submitRatingUrl,
                data: {programId: programId, rating: rating},
                timeout: timeout
            });
        },

        showMessage = function (message) {
            ratingMsgSpan.hide().text(message).fadeIn();
        };

    return {
        init: init
    };

}();

