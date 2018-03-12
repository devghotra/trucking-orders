//var orderListJson;
var truckHaulerNumbers = new Array();
var drivers = new Array();
var primeCarriers = new Array();
var customers = new Array();
var authToken;
//var dynatable;

function init(){
	
	$('#logoDiv').show();
	$('#viewEditOrderFormDiv').hide();
	$('#addOrderSuccessfulMsg').hide();
	$('#adminConsoleDiv').hide();
	
	$( "#addOrderDate" ).datepicker({ dateFormat: "yy-mm-dd"});
	$( "#editOrderDate" ).datepicker({ dateFormat: "yy-mm-dd"});
	$( "input[id=toOrderDate]" ).datepicker({ dateFormat: "yy-mm-dd"});
	$( "input[id=fromOrderDate]" ).datepicker({ dateFormat: "yy-mm-dd"});
	
	$("#dialog").dialog({
		modal : true,
		autoOpen : false,
		width : 600,
		resizable : false,
		position : [ 'center', 200 ],
		show : {
			effect : "blind",
			duration : 1000
		},
		hide : {
			effect : "explode",
			duration : 1000
		}
	});
	
	$( "#dialog-confirm" ).dialog({
	      resizable: false,
	      width : 400,
	      modal: true,
	      autoOpen : false,
	      position : [ 'center', 200 ],
			hide : {
				effect : "explode",
				duration : 1000
			}
	    });
	
	$( "#accordion" ).accordion({ heightStyle: "content", active: false, collapsible: true   });
	
	authToken = sessionStorage.authToken;
	
	if(authToken == null){
		$("#addOrderForm :input:not(:checkbox):not(:button)").prop('disabled', true);
		$("#orderLookUpForm :input:not(:checkbox):not(:button)").prop('disabled', true);
		$("#searchOrderForm :input:not(:checkbox):not(:button)").prop('disabled', true);
	} else{
		$(this).successfulLogin();
		loadTruckHaulerNumbers();
		loadDrivers();
		loadOrders();
		loadPrimeCarriers();
		loadCustomers();
	}
	
}

$( ".closeAdminConsole" ).click(function( event ) {
	$( "#dialog" ).dialog( "close" );
});

$( ".closeDeleteOrderDialog" ).click(function( event ) {
	$( "#dialog-confirm" ).dialog( "close" );
});

$( ".tabLink" ).click(function( event ) {
	if(authToken != null){
		$("#errorMsg").hide();
	}
});

$( "#addOrderForm #entryTotal" ).focus(function() {
	  //var bridgeToll = $( "#addOrderForm #bridgeToll" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #bridgeToll" ).val());
	  var loads = $( "#addOrderForm #loads" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #loads" ).val());
	  var loadRate = $( "#addOrderForm #ratePerLoad" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #ratePerLoad" ).val());
	  var hrs = $( "#addOrderForm #hours" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #hours" ).val());
	  var hrRate = $( "#addOrderForm #ratePerHour" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #ratePerHour" ).val());
	  var tons = $( "#addOrderForm #tons" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #tons" ).val());
	  var tonRate = $( "#addOrderForm #ratePerTon" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #ratePerTon" ).val());
	  //var brokage = $( "#addOrderForm #brokagePercentage" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #brokagePercentage" ).val());
	  
	  var total = loads*loadRate + hrs*hrRate + tons*tonRate;
	  //total = total - total*(brokage/100);
	  
	  $( "#addOrderForm #entryTotal" ).val(total);
});

$( "#addOrderForm #netEntry" ).focus(function() {
      var stbyHrs = $( "#addOrderForm #standbyHours" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #standbyHours" ).val());
	  var stbyHrRate = $( "#addOrderForm #ratePerStandbyHr" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #ratePerStandbyHr" ).val());
      var numOfTolls = $( "#addOrderForm #numOfTolls" ).val().trim() == "" ? 1 : parseFloat($( "#addOrderForm #numOfTolls" ).val());
	  var bridgeToll = $( "#addOrderForm #bridgeToll" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #bridgeToll" ).val());
	  var totalEntry = $( "#addOrderForm #entryTotal" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #entryTotal" ).val());
	  var otherCharges = $( "#addOrderForm #otherCharges" ).val().trim() == "" ? 0 : parseFloat($( "#addOrderForm #otherCharges" ).val());
	  
	  $( "#addOrderForm #netEntry" ).val(bridgeToll*numOfTolls + totalEntry + otherCharges + stbyHrs*stbyHrRate);
});

$( "#viewEditOrderForm #entryTotal" ).focus(function() {
	  //var bridgeToll = $( "#viewEditOrderForm #bridgeToll" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #bridgeToll" ).val());
	  var loads = $( "#viewEditOrderForm #loads" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #loads" ).val());
	  var loadRate = $( "#viewEditOrderForm #ratePerLoad" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #ratePerLoad" ).val());
	  var hrs = $( "#viewEditOrderForm #hours" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #hours" ).val());
	  var hrRate = $( "#viewEditOrderForm #ratePerHour" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #ratePerHour" ).val());
	  var tons = $( "#viewEditOrderForm #tons" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #tons" ).val());
	  var tonRate = $( "#viewEditOrderForm #ratePerTon" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #ratePerTon" ).val());
	  //var brokage = $( "#viewEditOrderForm #brokagePercentage" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #brokagePercentage" ).val());
	  
	  var total = loads*loadRate + hrs*hrRate + tons*tonRate;
	  //total = total - total*(brokage/100);

	  $( "#viewEditOrderForm #entryTotal" ).val(total);
});

$( "#viewEditOrderForm #netEntry" ).focus(function() {
    var stbyHrs = $( "#viewEditOrderForm #standbyHours" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #standbyHours" ).val());
    var stbyHrRate = $( "#viewEditOrderForm #ratePerStandbyHr" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #ratePerStandbyHr" ).val());
    var numOfTolls = $( "#viewEditOrderForm #numOfTolls" ).val().trim() == "" ? 1 : parseFloat($( "#viewEditOrderForm #numOfTolls" ).val());
    var bridgeToll = $( "#viewEditOrderForm #bridgeToll" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #bridgeToll" ).val());
	var totalEntry = $( "#viewEditOrderForm #entryTotal" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #entryTotal" ).val());
	var otherCharges = $( "#viewEditOrderForm #otherCharges" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #otherCharges" ).val());
	  
	  $( "#viewEditOrderForm #netEntry" ).val(bridgeToll*numOfTolls + totalEntry + otherCharges + stbyHrs*stbyHrRate);
});

$( "#viewEditOrderForm #totalPayment" ).focus(function() {
	  var bridgeTollPayment = $( "#viewEditOrderForm #bridgeTollPayment" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #bridgeTollPayment" ).val());
	  var paymentHours = $( "#viewEditOrderForm #paymentHours" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #paymentHours" ).val());
	  var paymentPerHour = $( "#viewEditOrderForm #paymentPerHour" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #paymentPerHour" ).val());
	  var paymentTons = $( "#viewEditOrderForm #paymentTons" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #paymentTons" ).val());
	  var paymentPerTon = $( "#viewEditOrderForm #paymentPerTon" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #paymentPerTon" ).val());
	  var paymentLoads = $( "#viewEditOrderForm #paymentLoads" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #paymentLoads" ).val());
	  var paymentPerLoad = $( "#viewEditOrderForm #paymentPerLoad" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #paymentPerLoad" ).val());
	  var otherPayment = $( "#viewEditOrderForm #otherPayment" ).val().trim() == "" ? 0 : parseFloat($( "#viewEditOrderForm #otherPayment" ).val());
	  
	  var total = bridgeTollPayment + paymentHours*paymentPerHour + paymentTons*paymentPerTon + paymentLoads*paymentPerLoad + otherPayment;

	  $( "#viewEditOrderForm #totalPayment" ).val(total);
});


$( "#editOrderBtn" ).click(function( event ) {
	$("#viewEditOrderForm :input:not(:checkbox):not(:button)").prop('disabled', false);
	$(this).hide();
	$('#deleteOrderBtn').hide();
	$('#saveOrderBtn').show();
	$('#cancelEditBtn').show();
});

$( "#cancelEditBtn" ).click(function( event ) {
	$("#viewEditOrderForm :input:not(:checkbox):not(:button)").prop('disabled', true);
	$(this).hide();
	$('#saveOrderBtn').hide();
	$('#editOrderBtn').show();
	$('#deleteOrderBtn').show();
});

$( "#deleteOrderBtn" ).click(function( event ) {
	$( "#dialog-confirm" ).dialog( "open" );
});

$( "#confirmDeleteOrderBtn" ).click(function( event ) {
	deleteOrder();
});


$( "#loginForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action'),
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
        		sessionStorage.authToken = data.authToken;
        		sessionStorage.adminUserFlag = data.adminUserFlag;
            	location.reload();
        	} else{
        		$(this).showErrorMessage(data.responseDescription);
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#addOrderForm" ).submit(function( event ) {
	$("#errorMsg").hide();
    setDefaultNumOfTolls("addOrderForm");
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$(this).showErrorMessage(data.responseDescription);
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#addUserForm" ).submit(function( event ) {
	var pwd = $('#addUserForm #password').val();
	var verifyPwd = $('#addUserForm #verifyPwd').val();
	var adminUserFlag = $('#addUserForm #adminUserFlag').val();
	
	if(adminUserFlag == 'on')
		$('#addUserForm #adminUserFlag').val(true);

	
	if(pwd != verifyPwd){
		$("#dialogErrorMsg").text("Password and verify password don't match");
		$("#dialogErrorMsg").show();
		return false;
	}
	
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#deactivateUserForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});


$( "#addTruckHaulerForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#removeTruckHaulerForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});


$( "#addDriverForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#removeDriverForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#addCustomerForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#removeCustomerForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#addPrimeCarrierForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#removePrimeCarrierForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$("#dialogErrorMsg").text(data.responseDescription);
        		$("#dialogErrorMsg").show();
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#orderLookUpForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	var orderId = $('#lookUpOrderId').val();
	var tagNumber = $('#lookUpTag').val();
	
	if((orderId == null || orderId.trim().length == 0 )
			&& (tagNumber == null || tagNumber.trim().length == 0)){
		$(this).showErrorMessage("Enter either order number or tag number");
		return false;
	} 
	
	if(orderId != null && orderId.trim().length > 0 && tagNumber != null && tagNumber.trim().length > 0){
		$(this).showErrorMessage("Enter either order number or tag number, not both");
		return false;
	}
	
	loadOrder(orderId, tagNumber);
	return false;
});

$( "#viewEditOrderForm" ).submit(function( event ) {
	$("#errorMsg").hide();
    setDefaultNumOfTolls("viewEditOrderForm");
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
            	location.reload();
        	} else{
        		$(this).showErrorMessage(data.responseDescription);
        	}
        },
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        }
    }); 
	return false;
});

$( "#searchOrderForm" ).submit(function( event ) {
	$("#errorMsg").hide();
	$.ajax({
        url     : $(this).attr('action') + "?at="+authToken,
        type    : $(this).attr('method'),
        contentType : "application/json; charset=utf-8",
        dataType: 'json',
        data    : JSON.stringify($(this).serializeObject()),
        success : function( data ) {
        	if(data.responseCode == 200){
        		$("#ordersTableHeading").text("Search Results");
            	//orderListJson = data.orderList;
            	//dynatable.settings.dataset.originalRecords = orderListJson;
            	//dynatable.process();
            	loadOrdersDataTable(data.orderList, true);
            	loadPaymentDataTable(data.orderList, true);
        	} else{
        		$(this).showErrorMessage(data.responseDescription);
        		}
        	},
        error   : function( xhr, err ) {
        	alert('Error in form submission '+err+xhr);     
        	}
    });
	return false;
});


function loadOrders(){
	$.get( "services/orders?at="+authToken, function( data ) {
		//orderListJson = data.orderList;
		
		/*$('#ordersTable').dynatable({
	  		dataset: {
	    		records: orderListJson
	  		}
		});
		
		dynatable = $('#ordersTable').dynatable({
			  dataset: {
			    records: orderListJson
			  }
			}).data('dynatable');*/
		
		if(data.responseCode == 200){
			loadOrdersDataTable(data.orderList, false);
			loadPaymentDataTable(data.orderList, false);
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
		
	});
}

function loadOrdersDataTable(orderListArray, refresh){
	var aaData="[";
	for (var key in orderListArray){
		var shift = orderListArray[key].shift == null ? "" : orderListArray[key].shift;
		var tollsNTimes = orderListArray[key].numOfTolls + "/<br/>" + orderListArray[key].bridgeToll;
		
		aaData+=(key>0?",":"")+"[";
	 
		aaData+="\""+orderListArray[key].orderDate+"\","+
				"\""+shift+"\","+
            	"\""+orderListArray[key].primeCarrier+"\","+
				"\""+orderListArray[key].customer+"\","+
            	"\""+orderListArray[key].jobNumber+"\","+
            	"\""+orderListArray[key].tagNumber+"\","+
            	"\""+orderListArray[key].truckHaulerNumber+"\","+
            	"\""+orderListArray[key].driverName+"\","+
            	"\""+orderListArray[key].hoursNRate+"\","+
            	"\""+orderListArray[key].loadsNRate+"\","+
            	"\""+orderListArray[key].tonsNRate+"\","+
            	"\""+orderListArray[key].entryTotal+"\","+
            	"\""+orderListArray[key].standbyHrsNRate+"\","+
				"\""+tollsNTimes+"\","+
				"\""+orderListArray[key].otherCharges+"\","+
				"\""+orderListArray[key].netEntry+"\","+
				"\""+orderListArray[key].paymentStatus+"\","+
		        "\"<a href='#' onclick='loadOrderFromLink("+orderListArray[key].orderId+"); return false;'><span class='glyphicon glyphicon-edit'></span></a>\"";
	     
	  aaData+="]";
	}
	aaData+="]";
	
	if(refresh){
		var oTable = $('#ordersTable').dataTable();
		oTable.fnClearTable();
		console.debug("Search Results Datatable: "+aaData);
		oTable.fnAddData(JSON.parse(aaData));
		oTable.fnDraw();
	} else{
		$('#ordersTable').dataTable( {
	        "aaData": JSON.parse(aaData),
	        //"sDom": 'Tfrtlip ',
	        "sDom": 'T<"clear">lfrtip',
	        //"sPaginationType": "full_numbers",
	        "bAutoWidth": false,
	        "aaSorting": [[ 0, "desc" ]],
	        "oLanguage": {
	            "sLengthMenu": "Display _MENU_ orders per page"
	        },
			"oTableTools": {
				"aButtons": [
								"copy",
								"print",
								{ "sExtends":"xls", "sFileName" : "jassboys-orders.xls"},
								{ "sExtends":"pdf", "sFileName" : "jassboys-orders.pdf", "sPdfOrientation": "landscape"},
							],
							"sSwfPath": "media/copy_csv_xls_pdf.swf"
			}
	    } );
		$(".dataTables_filter input").addClass("searchInputBox");
		//$(".DTTT_button").removeClass("DTTT_button");
		
	}
}

function loadPaymentDataTable(orderListArray, refresh){
	var aaData="[";
	for (var key in orderListArray){
		aaData+=(key>0?",":"")+"[";
		
		var paymentHr = orderListArray[key].paymentHours+"/"+orderListArray[key].paymentPerHour;
		var paymentTon = orderListArray[key].paymentTons+"/"+orderListArray[key].paymentPerTon;
		var paymentLoad = orderListArray[key].paymentLoads+"/"+orderListArray[key].paymentPerLoad;
		
		var otherPaymentData = "";
		var notes = "";
		
		if(orderListArray[key].otherPaymentDesc != null && orderListArray[key].otherPaymentDesc.trim().length > 0){
			otherPaymentData = orderListArray[key].otherPayment + "<br/>(" + orderListArray[key].otherPaymentDesc + ")";
		} else{
			otherPaymentData = orderListArray[key].otherPayment;
		}
		
		if(orderListArray[key].notes != null){
			notes = orderListArray[key].notes;
			notes = notes.replace(/(\r\n|\n|\r)/gm," ");
		}
		
		var shift = orderListArray[key].shift == null ? "" : orderListArray[key].shift;
		 
		aaData+="\""+orderListArray[key].orderDate+"\","+
				"\""+shift+"\","+
				"\""+orderListArray[key].customer+"\","+
				"\""+orderListArray[key].tagNumber+"\","+
				"\""+orderListArray[key].primeCarrier+"\","+
				"\""+orderListArray[key].truckHaulerNumber+"\","+
				"\""+orderListArray[key].netEntry+"\","+
				"\""+paymentHr+"\","+
				"\""+paymentTon+"\","+
				"\""+paymentLoad+"\","+
				"\""+orderListArray[key].bridgeTollPayment+"\","+
				"\""+otherPaymentData+"\","+
				"\""+orderListArray[key].totalPayment+"\","+
				"\""+orderListArray[key].paymentStatus+"\","+
				"\""+notes+"\","+
        		"\"<a href='#' onclick='loadOrderFromLink("+orderListArray[key].orderId+"); return false;'><span class='glyphicon glyphicon-edit'></span></a>\"";
	     
	  aaData+="]";
	}
	aaData+="]";
	
	if(refresh){
		var oTable = $('#paymentTable').dataTable();
		oTable.fnClearTable();
		console.debug("Payment Results Datatable: "+aaData);
		oTable.fnAddData(JSON.parse(aaData));
		oTable.fnDraw();
	} else{
		$('#paymentTable').dataTable( {
	        "aaData": JSON.parse(aaData),
	        //"sDom": 'Tfrtlip ',
	        "sDom": 'T<"clear">lfrtip',
	        //"sPaginationType": "full_numbers",
	        "bAutoWidth": false,
	        "aaSorting": [[ 0, "desc" ]],
	        "oLanguage": {
	            "sLengthMenu": "Display _MENU_ payments per page"
	        },
			"oTableTools": {
				"aButtons": [
								"copy",
								"print",
								{ "sExtends":"xls", "sFileName" : "jassboys-orders-payment.xls"},
								{ "sExtends":"pdf", "sFileName" : "jassboys-orders-payment.pdf", "sPdfOrientation": "landscape"},
							],
							"sSwfPath": "media/copy_csv_xls_pdf.swf"
			}
	    } );
		$(".dataTables_filter input").addClass("searchInputBox");
		//$(".DTTT_button").removeClass("DTTT_button");
		
	}
}

function loadTruckHaulerNumbers(){
	$.get( "services/orders/truck-hauler-numbers?at="+authToken, function( data ) {
		if(data.responseCode == 200){
			truckHaulerNumbers = data.truckHaulerNumbers;
			var option = '';
			for (var i=0;i<truckHaulerNumbers.length;i++){
			   option += '<option value="'+ truckHaulerNumbers[i] + '">' + truckHaulerNumbers[i] + '</option>';
			}
			$( "select[id=truckHaulerNumber]" ).append(option);
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
	});
}

function loadDrivers(){
	$.get( "services/orders/drivers?at="+authToken, function( data ) {
		if(data.responseCode == 200){
			drivers = data.drivers;
			var option = '';
			for (var i=0;i<drivers.length;i++){
			   option += '<option value="'+ drivers[i] + '">' + drivers[i] + '</option>';
			}
			$( "select[id=driverName]" ).append(option);
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
	});
}

function loadCustomers(){
	$.get( "services/orders/customers?at="+authToken, function( data ) {
		if(data.responseCode == 200){
			customers = data.customers;
			var option = '';
			for (var i=0;i<customers.length;i++){
			   option += '<option value="'+ customers[i] + '">' + customers[i] + '</option>';
			}
			$( "select[id=customer]" ).append(option);
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
	});
}

function loadPrimeCarriers(){
	$.get( "services/orders/prime-carrier?at="+authToken, function( data ) {
		if(data.responseCode == 200){
			primeCarriers = data.primeCarriers;
			var option = '';
			for (var i=0;i<primeCarriers.length;i++){
			   option += '<option value="'+ primeCarriers[i] + '">' + primeCarriers[i] + '</option>';
			}
			$( "select[id=primeCarrier]" ).append(option);
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
	});
}

function loadOrderFromLink(orderId){
	$( "#viewEditOrderLink" ).trigger( "click" );
	$( "#lookUpOrderId" ).val(orderId);
	loadOrder(orderId, null);
	$('html, body').animate({ scrollTop: 0 }, 'slow');
}

function loadOrder(orderId, tagNumber){
	var url = "";
	if(tagNumber != null && tagNumber.trim().length > 0)
		url = "services/orders?tag="+tagNumber.trim()+"&at="+authToken;
	else
		url = "services/orders?order-id="+orderId+"&at="+authToken;
	$.get( url, function( data ) {
		if(data.responseCode == 200){
			console.debug("loads: "+data.order.loads);
			$('#viewEditOrderFormDiv').show();
			$('#viewEditOrderForm #orderId').val(data.order.orderId);
			$('#viewEditOrderForm #editOrderDate').val(data.order.orderDate);
			$('#viewEditOrderForm #shift').val(data.order.shift);
			$('#viewEditOrderForm #tagNumber').val(data.order.tagNumber);
			$('#viewEditOrderForm #primeCarrier').val(data.order.primeCarrier);
			$('#viewEditOrderForm #customer').val(data.order.customer);
			$('#viewEditOrderForm #jobNumber').val(data.order.jobNumber);
			$('#viewEditOrderForm #loads').val(data.order.loads);
			$('#viewEditOrderForm #ratePerLoad').val(data.order.ratePerLoad);
			$('#viewEditOrderForm #hours').val(data.order.hours);
			$('#viewEditOrderForm #ratePerHour').val(data.order.ratePerHour);
			$('#viewEditOrderForm #standbyHours').val(data.order.standbyHours);
			$('#viewEditOrderForm #ratePerStandbyHr').val(data.order.ratePerStandbyHr);
			$('#viewEditOrderForm #tons').val(data.order.tons);
			$('#viewEditOrderForm #ratePerTon').val(data.order.ratePerTon);
			$('#viewEditOrderForm #entryTotal').val(data.order.entryTotal);
            $('#viewEditOrderForm #numOfTolls').val(data.order.numOfTolls);
			$('#viewEditOrderForm #bridgeToll').val(data.order.bridgeToll);
			$('#viewEditOrderForm #paymentStatus').val(data.order.paymentStatus);
			$('#viewEditOrderForm #paidToSub').val(data.order.paidToSub);
			$('#viewEditOrderForm #driverName').val(data.order.driverName);
			$('#viewEditOrderForm #notes').val(data.order.notes);
			$('#viewEditOrderForm #truckHaulerNumber').val(data.order.truckHaulerNumber);
			$('#viewEditOrderForm #brokagePercentage').val(data.order.brokagePercentage);
			$('#viewEditOrderForm #netEntry').val(data.order.netEntry);
			
			$('#viewEditOrderForm #paymentHours').val(data.order.paymentHours);
			$('#viewEditOrderForm #paymentPerHour').val(data.order.paymentPerHour);
			$('#viewEditOrderForm #paymentTons').val(data.order.paymentTons);
			$('#viewEditOrderForm #paymentPerTon').val(data.order.paymentPerTon);
			$('#viewEditOrderForm #paymentLoads').val(data.order.paymentLoads);
			$('#viewEditOrderForm #paymentPerLoad').val(data.order.paymentPerLoad);
			$('#viewEditOrderForm #otherPayment').val(data.order.otherPayment);
			$('#viewEditOrderForm #totalPayment').val(data.order.totalPayment);
			
			$('#viewEditOrderForm #bridgeTollPayment').val(data.order.bridgeTollPayment);
			$('#viewEditOrderForm #otherPaymentDesc').val(data.order.otherPaymentDesc);
			
			$('#viewEditOrderForm #otherCharges').val(data.order.otherCharges);
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
	});
}

function deleteOrder(){
	var orderId = $("#viewEditOrderForm #orderId").val();
	var url = "services/orders/delete-order?at="+authToken+"&order-id="+orderId;
	$.get( url, function( data ) {
		if(data.responseCode == 200){
			location.reload();
    	} else{
    		$(this).showErrorMessage(data.responseDescription);
    	}
	});
}

$.fn.successfulLogin = function()
{
	$("#adminConsoleItem").show();
	$("#signOutItem").show();
	$("#signInItem").hide();
	$("#errorMsg").hide();
};

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
} else {
    o[this.name] = this.value || '';
        }
    });
    return o;
};

$.fn.showErrorMessage = function(errorMessage)
{
	$("#errorMsg").text(errorMessage);
	$("#errorMsg").show();
	
	if(errorMessage.trim() == "Session Expired" || errorMessage.trim() == "Unauthorized User"){
		$("#adminConsoleItem").hide();
		$("#signInItem").show();
	}
};

function adminConsole()
{
	$("#dialogErrorMsg").hide();
	
	if(sessionStorage.adminUserFlag == 'false'){
		$(".adminUserClass").hide();
	}
		
	
	$( "#dialog" ).dialog( "open" );
};

function signOut()
{
	sessionStorage.removeItem('authToken');
	location.reload();
};

/* Set num of tolls to 1 if bridge toll is set*/
function setDefaultNumOfTolls(formId){
    var bridgeToll = $( "#"+formId+" #bridgeToll" ).val().trim();
    var numOfTolls = $( "#"+formId+" #numOfTolls" ).val().trim();

    if(numOfTolls == "" && bridgeToll != ""){
        $( "#"+formId+" #numOfTolls" ).val(1);
	}

}


//$("#dialog").dialog({
//modal: true,
//minHeight:600,
//width: 800,
//height: 500, 
//autoOpen : false,
//show : {
//	effect : "blind",
//	duration : 1000
//},
//hide : {
//	effect : "explode",
//	duration : 1000
//}
//});

//buttons : {
//	Close : function() {
//		$(this).dialog("close");
//	}
//},