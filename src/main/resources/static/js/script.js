console.log("js loaded");

// const toggleSidebar = () => {
//     let sidebar = $(".sidebar");
//     let contentArea = $(".content");
//   if ($(sidebar).is(":visible")) {
//     sidebar.css("display", "none");
//     contentArea.css("margin-left", "0%");
//   }else{
//     sidebar.css("display", "block");
//     contentArea.css("margin-left", "20%");
//   }
// };

// const toggleSidebar = () => {
//     let sidebar = $(".sidebar");
//     let contentArea = $(".content");

//     // Check if the sidebar is currently visible
//     if ($(sidebar).is(":visible")) {
//         // If visible, hide the sidebar with a fade-out animation
//         sidebar.hide("fade", { duration: 200 });
//         contentArea.animate({ "margin-left": "1%" }, 200);
//     } else {
//         // If not visible, show the sidebar with a fade-in animation
//         sidebar.show("fade", { duration: 200 });
//         contentArea.animate({ "margin-left": "20%" }, 200);
//     }
// };

const toggleSidebar = () => {
  let sidebar = $(".sidebar");
  let contentArea = $(".content");

  // Check if the sidebar is currently visible
  if ($(sidebar).is(":visible")) {
    // If visible, hide the sidebar with a slide-out animation
    sidebar.hide("slide", { direction: "left" }, 200);
    contentArea.animate({ "margin-left": "5px" }, 200);
  } else {
    // If not visible, show the sidebar with a slide-in animation
    sidebar.show("slide", { direction: "left" }, 200);
    contentArea.animate({ "margin-left": "19%" }, 200);
  }
};

const confirmDeletion = (cid) => {
  swal({
    title: "Are you sure?",
    text: "Once deleted, you will not be able to access this contact",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((willDelete) => {
    if (willDelete) {
      window.location = "/user/contact/delete/" + cid;
    } else {
    }
  });
};

const search = () => {
  let query = $("#search-input").val();
  let searcResult = $(".search-result");
  if (query.length > 0) {
    let url = `http://localhost:8080/search/${query}`;
    fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        console.log(data);
        if (data.length == 0) {
          searcResult.html(
            "<p class='list-group-item m-0'>No result found</p>"
          );
          return;
        }
        let text = `<div class='list-group'>`;
        data.forEach((contact) => {
          text += `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-item-action'>${contact.name}</a>`;
        });
        text += `</div>`;
        searcResult.html(text);
        searcResult.show();
      });
  } else {
    searcResult.hide();
  }
};

// first request to create order
const paymentStart = () => {
  console.log("Payment initiated");
  let paymentValue = $("#payment_field").val();

  if (paymentValue != "") {
    console.log(paymentValue);
    $.ajax({
      url: "/user/create_order",
      type: "POST",
      data: JSON.stringify({
        amount: paymentValue,
        info: "order_request",
      }),
      contentType: "application/json",
      dataType: "json",
      success: function (response) {
        console.log(response);
        if (response.status == "created") {
          // open payment form
          let options = {
            key: "rzp_test_u8dWKOGCtW7j9P",
            amount: response.amount,
            currency: "INR",
            name: "Smart Contact Manager",
            description: "Support us",
            order_id: response.id,
            handler: function (response) {
              console.log(response.razorpay_payment_id);
              console.log(response.razorpay_order_id);
              console.log(response.razorpay_signature);
              console.log("Payment successful");
              updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,'paid');
              
            },
            prefill: {
              //We recommend using the prefill parameter to auto-fill customer's contact information especially their phone number
              name: "", //your customer's name
              email: "",
              contact: "", //Provide the customer's phone number for better conversion rates
            },
            notes: {
              address: "Smart Contact Manager",
            },
            theme: {
              color: "#3399cc",
            },
          };
          let rzp = new Razorpay(options);
          

          rzp.on('payment.failed', function (response){
            console.log(response.error.code);
            console.log(response.error.description);
            console.log(response.error.source);
            console.log(response.error.step);
            console.log(response.error.reason);
            console.log(response.error.metadata.order_id);
            console.log(response.error.metadata.payment_id);
            swal("Sorry","Oops payment failed","error");
        });

          rzp.open();

          
        }
      },
      error: function (error) {
        console.log(error);
      },
    });
  } else {
    swal("Invalid amount!!!","Please enter amount in figure","error");
  }
};


const updatePaymentOnServer = (payment_id,order_id,status) => {
  $.ajax({
    url: "/user/update_order",
    type: "POST",
    data: JSON.stringify({
      payment_id:payment_id,
      order_id: order_id,
      status : status,
    }),
    contentType: "application/json",
    dataType: "json",
    success: function (response) {
      console.log(response);
      swal("Thankyou","Payment successful","success");
    },
    error: function (error) {
      console.log(error);
      swal("Failed!!","Your payment was successful, but there was some issue on server. We'll contact  you as soon as possible");
    }
  });
}