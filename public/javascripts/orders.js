(function ($) {
    // Don't allow submitting the form to the server till we get
    // the payment token from Stripe.
    var allowSubmit = false,
        stripeRequestOutstanding = false;

    var stripeResponseHandler = function (status, response) {
        stripeRequestOutstanding = false;
        console.log("Response from Stripe", response);
        if (response.error) {
            alert("An error occurred communicating with the payment manager:\n\n" +
                    response.error.message);
        } else {
            var paymentForm = $("#orderForm");
            paymentForm.find("#stripePaymentToken").val(response['id']);
            allowSubmit = true;
            paymentForm.submit();
        }
    };

    // Add event handlers in onDomReady:
    $(function () {
        $("#orderForm").on("submit", function (submitEvent) {
            console.log("submit event fired");
            if (stripeRequestOutstanding || allowSubmit === false) {
                submitEvent.preventDefault();
            }
            if (!stripeRequestOutstanding && !allowSubmit) {
                // Make a request to stripe.
                Stripe.createToken({
                    number: $("#cardNumberInput").val(),
                    cvc: $("#cvcNumberInput").val(),
                    exp_month: $("#cardExpirationMonthInput").val(),
                    exp_year: $("#cardExpirationYearInput").val(),
                    name: $("#name").val(),
                    address_zip: $("#addressZipCode").val(),
                    address_country: "USA"
                }, stripeResponseHandler);
                stripeRequestOutstanding = true;
            }
        });
    });
}(jQuery));