$(function() {
    
    /*
     * auto hightlight text content on focus 
     */
    $("#view-code-widget input, #view-code-widget textarea").on("focus", function(){
        $(this).select();
    });
    
    /*
     * Toggle the View Code widget
     */
    $(".view-code-button-wrapper").on("click", function(){
        toggleOffCanvas(this);
    });
    
    /*
     * When Next is clicked, switch to next form
     */
    $("button.next-step").on("click", function(){
        var targetObjectId = "#"+$(this).attr("data-target");
        nextStep(targetObjectId);
        return false;
    });
    
    /*
     * When the widget/icon is clicked swith to that specific form
     */
    $(".fund-transfer-steps .step").on("click", function(){
        $(".fund-transfer-steps .step .circle").removeClass("active");
        $(this).find(".circle").addClass("active");
        
        var id = $(this).attr('id');
        
        id = "#"+id.substr(0, id.lastIndexOf("-"));
        nextStep(id);
        
        return false;
    });
    
    /*
     * When update link is clicked, switch to appropriate form
     */
    $("a.update-link").on("click", function(){
       var targetId = "#"+$(this).attr("data-target");
       nextStep(targetId);
       return false;
    });
    
    /*
     * Update Sender or Receiver Visa card number on the last step form.
     * Add space between every 4 digits
     */
    $("#sender_card_number, #receiver_card_number").on("keyup", function(){
        var targetId = "#"+$(this).attr("id")+"-final";
        var currentValue = $(this).val();
        var formatedCardNum = formatCreditCardNum(currentValue);
        $(this).val(formatedCardNum);
        $(targetId).val(formatedCardNum);
    });
    
    $(".fund-transfer-form .form-title").on("click", function(){
        var targetId = "#"+$(this).attr("data-target");
        nextStep(targetId);
    });
    
    /*
     * Verify sender's Visa card number
     */
    $("#verify-sender-card-number").on("click", function(){
        var cardNumber = $("#sender_card_number").val();
        
        // Client side validation
        var formValidator = $("#sender-info-form").validate({
            errorElement: "span",
            errorPlacement: function(error, element) {
                error.insertAfter(element.parent(".input-group"));
            },
            rules: {
                sender_card_number: {
                    required: true,
                    creditcard: true
                }
            },
            messages: {
                sender_card_number: {
                    required: 'Visa card number required.',
                    creditcard: 'Enter valid credit card number.'
                }
            }
        });
        formValidator.form();
        
        if ($("#sender-info-form").valid())  {
            verifyCreditCardNumber(cardNumber, "#receiver-info"); 
        }
        return false;
    });
    
    /*
     * Verify receiver's Visa card number
     */
    $("#verify-receiver-card-number").on("click", function(){
        var cardNumber = $("#receiver_card_number").val();
        
        // Client side validation
        var formValidator = $("#receiver-info-form").validate({
            errorElement: "span",
            errorPlacement: function(error, element) {
                error.insertAfter(element.parent(".input-group"));
            },
            rules: {
                receiver_card_number: {
                    required: true,
                    creditcard: true
                }
            },
            messages: {
                receiver_card_number: {
                    required: 'Visa card number required.',
                    creditcard: 'Enter valid credit card number.'
                }
            }
        });
        formValidator.form();
        
        if ($("#receiver-info-form").valid())  {
            verifyReceiverCardNumber(cardNumber, "#money-info"); 
        }
        return false;
        
    });
    
    /*
     * Reset form value
     */
    $("button.reset-button").on("click", function(){
        $(this).closest("form").find("input[type=text]").val("");
        $('#showMsg').hide();
        $('#showMsg1').hide();
        $('#showMsg2').hide();
        
        $("#sender_card_number + .input-group-addon i, #receiver_card_number + .input-group-addon i").removeClass("fa-check fa-times");
        //$('#apiname').hide();
    });
    
    /*
     * Transfer Fund
     */
    $("#money-transfer-form").on("submit", function() {
        // Client side validation
        var formValidator = $(this).validate({
            errorElement: "span",
            errorPlacement: function(error, element) {
                error.insertAfter(element.parent(".input-group"));
            },
            rules: {
                transfer_amount: {
                    required: true,
                    number: true
                }
            },
            messages: {
                transfer_amount: {
                    required: 'Amount is required.'
                }
            }
        });
        formValidator.form();
        
        if ($(this).valid())  {
            transferFund();
        }
        return false;
    });
    
    /*
     * Update credentails
     */
        
    $("#updateCredential").click(function() {
    	var api_key = $("#api-key").val();
    	var share_secret = $("#share-secret").val();    	
        $.ajax({
            type: "GET",
            url: "AdminConsoleReadServlet",
            cache: false,
            data: "apiKey="+api_key+"&sharedSecret="+share_secret,		
            success: function(responseText) {
                document.getElementById("api-key").value = responseText.apiKey;
                document.getElementById("share-secret").value = responseText.sharedSecret;
            }
        });
    });	 
    
    $("#update-credential-form").on("submit", function(){        
    	var api_key = $("#api-key").val();
    	var share_secret = $("#share-secret").val();
        
        // Client side validation
        var formValidator = $(this).validate({
            errorElement: "span",
            rules: {
                api_key: {
                    required: true
                },
                share_secret: {
                    required: true
                }
            },
            messages: {
                api_key: {
                    required: 'API-Key is required.'
                },
                share_secret: {
                    required: 'Share Secret is required.'
                }
            }
        });
        formValidator.form();
        
        if ($(this).valid())  {
            transferFund();
        }
        return false;
        
        $.ajax({
            type: "GET",
            url: "AdminConsoleServlet",
            data: "apiKey="+api_key+"&sharedSecret="+share_secret,	
            cache: false,
            success: function(responseText) {
                $( "#update-credential-form" ).dialog( "close" );
            }
        });
    });

    $("#clearAdmin").click(function() { 
    	var api_key = $("#api-key").val();
    	var share_secret = $("#share-secret").val();
        
        $.ajax({
            type: "GET",
            url: "AdminResetServlet",
            data: "apiKey="+api_key+"&sharedSecret="+share_secret,		
            cache: false,
            success: function(responseText) {
                document.getElementById("api-key").value = responseText.apiKey;
                document.getElementById("share-secret").value = responseText.sharedSecret;
            }
    	}); 
    });	
    
     
});

var formatCreditCardNum = function(currentCardNum) {
    var formatedNum = null;
    var length = null;
    
    formatedNum = currentCardNum.trim();
    formatedNum = formatedNum.replace(/ /g, "");
    length = formatedNum.length;
        
    if (length >12) {
        formatedNum = formatedNum.substr(0, 4) + " " +
                    formatedNum.substr(4, 4) + " " +
                    formatedNum.substr(8, 4) + " " +
                    formatedNum.substr(12);
    } else if (length >8) {
        formatedNum = formatedNum.substr(0, 4) + " " +
                    formatedNum.substr(4, 4) + " " + 
                    formatedNum.substr(8);
    } else if (length >4) {
        formatedNum = formatedNum.substr(0, 4) + " " +
                    formatedNum.substr(4);
    }
    
    return formatedNum;
}

var transferFund = function () {
    var transferAmount = $("#transfer-amount").val();
    var parsedData;
    var actionCode;
    var $messageDiv = $('#showMsg2');	
    var $apiNameDiv = $('#apiname');
    var $octDiv = $('#oct-div');
    $apiNameDiv.show().html('Pull Money(AFT)');
    $('#end-point-url').val("https://sandbox.api.visa.com/cva/cce/AccountFundingTransactions/");
    $('#end-point-url-1').val("https://sandbox.api.visa.com/cva/cce/OriginalCreditTransactions/");

    $.ajax({
        type: "GET",
        url: "AFTRequestServlet",
        data: "amount="+transferAmount,	
        cache: false,
        success: function(data) {
            $('#request').html(data);
        }
    });

    $.ajax({
            type: "GET",
            url: "AFTResponseServlet",
            data: "amount="+transferAmount,	
            cache: false,
            success: function(responseText) {
                $('#x-pay-token').val(responseText.token);
                $('#response').html(responseText.response); 	
                $('#api-key-2').val(responseText.apiKey);
                $('#shared-secret-2').val(responseText.sharedSecret);

                parsedData =JSON.parse(responseText.response);						
                actionCode = parsedData.ActionCode;						

                if (actionCode=="00" || actionCode=="0") {	
                    $octDiv.show();
                    $.ajax({
                        type: "GET",
                        url: "OCTRequestServlet",
                        data: "amount="+transferAmount,	
                        cache: false,
                        success: function(data1) {
                            $('#request-1').html(data1);
                        }
                    });

                    $.ajax({
                            type: "GET",
                            url: "OCTResponseServlet",
                            data: "amount="+transferAmount,	
                            cache: false,
                            success: function(responseText1) {											
                                $('#x-pay-token-1').val(responseText1.token);						
                                $('#response-1').html(responseText1.response); 						

                                parsedData =JSON.parse(responseText1.response);						
                                actionCode = parsedData.ActionCode;	

                                resetMessageBox($messageDiv);

                                if (actionCode=="00") {							
                                    $messageDiv.addClass("success").show().html('Money Transfer Successful!');							
                                }else{
                                    $messageDiv.addClass("warning").show().html('Money Transfer Failed.');
                                }
                            }
                    });
                }else{
                    $messageDiv.addClass("warning").show().html('Money Transfer Failed.');
                    document.getElementById("cbxShowHide").disabled=false;
                    $('#request').val('');
                    $('#response').val('');
                    $('#x-pay-token').val('');
                } 
            },
            error: function(xhr, textStatus, errorThrown) {
                $messageDiv.addClass("warning").show().html('Failed to send API request. Network Error.');
            }
    }); 

}

//Sender Card Number Verification
var verifyCreditCardNumber = function (cardNumber, targetId) {
    var responseRegExp = new RegExp("TransactionIdentifier");
    var $messageDiv = $('#showMsg');
    var $apiNameDiv = $('#apiname');
    cardNumber = cardNumber.replace(/ /g, "");
    $apiNameDiv.show().html('Account Verification');
    $('#oct-div').hide();
    $('#end-point-url').val("https://sandbox.api.visa.com/cva/cce/AccountVerification/");

    $.ajax({
        type: "GET",
        url: "AccountVerificationRequestServlet",
        data: "accNo="+cardNumber,	
        cache: false,
        success: function(data) {
                $('#request').html(data);
        }
    });

    $.ajax({
        type: "GET",
        url: "AccountVerificationResponseServlet",
        data: "accNo="+cardNumber,		
        cache: false,
        success: function(responseText) {
            $('#x-pay-token').val(responseText.token);
            $('#response').html(responseText.response); 	
            $('#api-key-2').val(responseText.apiKey);
            $('#shared-secret-2').val(responseText.sharedSecret);

            resetMessageBox($messageDiv);

            if (responseRegExp.test(responseText.response)) {					
                $messageDiv.addClass("success").show().html('Sender Account Verified Successfully!');
                $("#sender_card_number + .input-group-addon i").removeClass("fa-check fa-times").addClass("fa-check");
                //nextStep(targetId);
            }else{
                $messageDiv.addClass("warning").show().html('Failed to verify Sender Account.');
                $("#sender_card_number + .input-group-addon i").removeClass("fa-check fa-times").addClass("fa-times");
            }

        },
        error: function(xhr, textStatus, errorThrown) {
            $messageDiv.addClass("warning").show().html('Failed to send API request. Network Error.');
        }
    });
}

//Receiver Card Number Verification
var verifyReceiverCardNumber = function (cardNumber, targetId) {	
    var responseRegExp = new RegExp("CardProductTypeCode");
    var $messageDiv = $('#showMsg1');
    var $apiNameDiv = $('#apiname');	
    cardNumber = cardNumber.replace(/ /g, "");
    $apiNameDiv.show().html('Account Lookup');
    $('#oct-div').hide();
    $('#end-point-url').val("https://sandbox.api.visa.com/cva/cce/AccountLookup/");
    $.ajax({
        type: "GET",
        url: "AccountLookupRequestServlet",
        cache: false,
        data: "recipientCardNumber="+cardNumber,		
        success: function(data) {
            $('#request').html(data);
        }
    });

    $.ajax({
        type: "GET",
        url: "AccountLookupResponseServlet",
        cache: false,
        data: "recipientCardNumber="+cardNumber,		
        success: function(responseText) {
            $('#x-pay-token').val(responseText.token);
            $('#response').html(responseText.response); 	
            $('#api-key-2').val(responseText.apiKey);
            $('#shared-secret-2').val(responseText.sharedSecret);

            resetMessageBox($messageDiv);

            if (responseRegExp.test(responseText.response)) {
                $messageDiv.addClass("success").show().html('Receiver Account Verified Successfully!');
                $("#receiver_card_number + .input-group-addon i").removeClass("fa-check fa-times").addClass("fa-check");
                //nextStep(targetId);
            }else{
                $messageDiv.addClass("warning").show().html('Failed to verify Receiver Account.');
                $("#receiver_card_number + .input-group-addon i").removeClass("fa-check fa-times").addClass("fa-times");
            }      
        },
        error: function(xhr, textStatus, errorThrown) {
            $messageDiv.addClass("warning").show().html('Failed to send API request. Network Error.');
        }
    });    
}

var resetMessageBox = function ($this) {
    $this.removeClass("success warning");
}

var toggleOffCanvas = function (target) {
    var $this = $(target);
    var icon = $this.find(".arrow i");
    
    if ($this.hasClass("canvas-slid")) {
        icon.removeClass("fa-angle-left");
        icon.addClass("fa-angle-right");
    } else {
        icon.removeClass("fa-angle-right");
        icon.addClass("fa-angle-left");
    }
}

var nextStep = function (targetObjectId) {
    var nextStepClass = null;
    
    //To fix mobile to desktop view problem
    $(".fund-transfer-form .step .form-content").show();
    
    $(".fund-transfer-form .step").hide();
    $(targetObjectId).fadeIn(500);
    
    
    
    var targetWidgetId = targetObjectId+"-widget";
    if (targetObjectId == "#receiver-info") {
        nextStepClass = "step2";
        $(".progress-arrow").removeClass("active");
        $(targetWidgetId).next(".progress-arrow").addClass("active");
    } else if (targetObjectId == "#money-info") {
        nextStepClass = "step3";
        $(".progress-arrow").removeClass("active");
    } else {
        nextStepClass = "step1";
        $(".progress-arrow").removeClass("active");
        $(targetWidgetId).next(".progress-arrow").addClass("active");
        //arrowPosition = "left";
    }
    
    //$(".progress-arrow").removeClass("left").removeClass("right").removeClass("hide").addClass(arrowPosition);
    
    // Remove all carrot then add carrot to the selected item
    $(".fund-transfer-form .form-title").removeClass("active");
    $(targetObjectId).prev("div.form-title").addClass("active");

    //Change icon background color
    $(".fund-transfer-form .form-title .circle.small").removeClass("active");
    $(targetObjectId).prev("div.form-title").find(".circle.small").addClass("active");
    
    
    
    
    $('.carrot').removeClass("step1").removeClass("step2").removeClass("step3");
    $('.carrot').addClass(nextStepClass);
    
    $(".fund-transfer-steps .step .circle").removeClass("active");
    $(targetWidgetId).find(".circle").addClass("active");
    
    $(".step").removeClass("active");
    $(targetWidgetId).addClass("active");

    return false;
}