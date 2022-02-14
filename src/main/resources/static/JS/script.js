//search function
const search=()=>{
	
	let query = $("#search-input").val();
	
	if(query==''){
		$(".search-result").hide();
	}
	else{
		console.log(query);
		//sending request to server
		
		let url = `http://localhost:8083/search/${query}`;
		fetch(url).then((response)=>{
			return response.json();
		}).then((data)=>{
			//data
			let text =`<div class='list-group'>`
			
			data.forEach((contact)=>{
				text+=`<a style='background:black; color:white;' href='/user/contact/Cont${contact.cid}' class='list-group-item list-group-action'>${contact.name}</a>`
			});
			
			text+=`</div>`
			
			$(".search-result").html(text);
			$(".search-result").show();
		});
		
	}
}

/******************************************************************************************************************** */

//password toggle
function password_show_hide() {
	var x = document.getElementById("password");
	var show_eye = document.getElementById("show_eye");
	var hide_eye = document.getElementById("hide_eye");
	hide_eye.classList.remove("d-none");
	   if (x.type === "password") {
		   x.type = "text";
		   show_eye.style.display = "none";
		   hide_eye.style.display = "block";
	  } else {
		   x.type = "password";
		   show_eye.style.display = "block";
		   hide_eye.style.display = "none";
	  }
}

function password_show_hide1() {
	var x = document.getElementById("password1");
	var show_eye = document.getElementById("show_eye1");
	var hide_eye = document.getElementById("hide_eye1");
	hide_eye.classList.remove("d-none");
	   if (x.type === "password") {
		   x.type = "text";
		   show_eye.style.display = "none";
		   hide_eye.style.display = "block";
	  } else {
		   x.type = "password";
		   show_eye.style.display = "block";
		   hide_eye.style.display = "none";
	  }
}

/************************************************************************************************************** */

//first request to server to create order

const paymentStart=()=>{
	console.log("payment started");
	let amount = $("#paymentField").val();
	console.log(amount);
	if(amount == "" || amount == 0){
		swal("Failed!!!", "Please, enter amount!!!", "error");
		return;
		}
	//now, we will use ajax to send request to server to create order.

$.ajax(
	{
		url: '/user/createOrder',
		data: JSON.stringify({amount:amount, info:'order-request'}),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response){
			//invoked when success
			console.log(response)
			if(response.status=='created'){
				//now, we can open payment form
				let options = {
					key : 'rzp_test_ckUbwQFcrzCweq',
					amount : response.amount,
					curreny : 'INR',
					name  : 'ePHONEBOOK',
					description : 'donation',
					image : 'data:image/jpg;base64,/9j/4AAQSkZJRgABAQEAlgCWAAD/2wCEAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDIBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMv/CABEIAMgA5gMBIgACEQEDEQH/xAA0AAEAAgMBAQEAAAAAAAAAAAAABgcDBAUBAggBAQADAQEBAAAAAAAAAAAAAAACAwQFAQb/2gAMAwEAAhADEAAAAPz+AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABJ4x9wlM8fB/QHL2UyvfXxX0j7YXuqqvPqw9vz2u47dVOaa48OliAAAAAAAAMkuhLj2PWPMpsun2lWaz06HSzc/wB2tMtytunHcl2Ma6AAAAAAABPa5xzt2fBObp60ehOvuz9jk/OW+GLo2LHabMUS+fbI+MvzOHw98AAAAAAHU5curnOuTh1sN8E17A+d2aA/W7addkPuXW6Xz3R/P3AtbL2cVTbVu9ycaa0/05QUJxfW7HM20YBOAAAAAGWVw/r1z3et9wmqefXk27bXlsmjrHwaZfFpRl43Rrzfqb773LuSXfm7pVT/AFjQvervNdi4u3zutjxC6oAAAAB1eU897OT37qs4fmNdVOJzR1oc/XMNzj7XK30F3e5HfouNyNjWT8knL0Vc82DxOIe+AAAAAAOnzHns35cckWe2PTHoxnyVmb1HTLLphHnvnU5wAm8fYQ98l4AAAAAAAAB7dtN3xy9tXQ79Dfny2HnU5OXdmx+WdlyaK2tfY41F1Xjp4AAAAAAAAEgj6PtjYa/UW2BBMSyAWwl/ZrdmvsWL8JLwL6QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP/8QANxAAAQQBAwIDBgQDCQAAAAAAAwECBAUGAAcREhMgMDEQFBUXIUAWIiRBJTJhIyY0NUJDRVJU/9oACAEBAAEMAPvE+9T71PvU8yqwW3uK8c2M6Igl2uyTjlo4rkdtjkif7AF0u2+Sov8AhBa+XGR/+YOvlvkP7hjpr5b3/wD0iaTba9/f3JNfLa7/AHLXpqPtlLVUWXbQxJlFMKgunQBSXSGeJPMaR7U/K5U1W5Da1R2lhzTMXG7RL/H4lijUa5w9GRohvI9URv4+xly/5hxr8a40/wBLUaaTKseJ6XEVNRpsGevEObGkK8a8KnC85sXvZnau/bxJ5tfjFtZR/eBRlHFxXMKLD8bStmzElyCbwUvVw2vmqhdzqCfBkAc2THIvrrldcrprla5HIqou3l9IuIhoM0jinvy97IbIvPPjTywieYrRja572MqsRGiywisry2vrK7P3J8ohfZwvsa5WuRUXhQFhyf7OW1QOnVx4Lm9zpePWF5DHxy1NKkjIQZyqY5Cu/m8SeXVSXV7FLDapLKq2yyW5chHR2RBrtLj9JHSRkuWDjtsZW29fyOtrbW0fJt4hFVI1LBjte/rcrulrdcaqqSxupHZrohZD1gw6OKaFa2oZGn8dS9PPHkJ5PGsc21m2LWSrZ7oMWPDx7DYCmRgIQ8g3WmyFeCkYsUUuZImyHHknIYvPsAAsk7AhG8habawNTV/Hc3lfD4WSZ0+cFauijNqqZV50jVXTRKrHLpRrpWqniTyKmqPYyBiEJ7345gsbGmkubtoxyLXP7O2lHhYpCO5ZcuTNO48s5Dl9jWK9yNaiqtBtzc3DmEkDWDFxvHaXDa80mBGaablGX3GW2HvVpJUnsa3ldDCq8fTUOtNKAZAheRzo309NFDxpycL4U8jb9iScoisIq9rc/KDTLNalCL29rjGguyOyjEUcv8WY1k30yykUE123QbUbj4rfwrVlnUWFPK93sYhoxdu/h0PFRzDpFEcWR0xSdtltCUjnNkR3tR6Kz5PDVfpdO0PZgb14+O8aBsWxyp/eFE1B2HiI5O/fPe12EVuH4bb/AAwZCSZUFAJ0InKyRcc/TRW8L4U8cdWtkDc9EVuJlZAzckd3DG50J48vmkci9O18sTcgk1xlRGJVnW0JXflSTyeHJ+ikCem3AWTHSpywCWlZT1kShqQRK6YsiLLhQrIajmxASWzsNk1vXMxayPBLW7tnFGRljXocrd4Yif8AEH1G3niNVP4VJ4od2aewOwJUNEfkN+yJikyQrWPS3tHGK5/bBxNkq5V/K3Uh/Kr6eFPIno8gYtqJyos5zc4oWygInxuNJLDkikAeozZESPkrPxBDYg5dfIq8zjjr7gzYV5d49Z4/L7FhGcPW3Nk+XixohXK5Wk01etFZzpjWueiPd0tkxiRTuEVOHCdwuo5+n0XSXJZO0HUR6ucWYqt45+hzcquiu5Xwp5FPOCBxIs1rnQZMafjVoE4Dua6wlQ7tVldtkOwDILGMhAvcwhS9wiv6WtWgz8seKlZeB9/r6QNKyFJNj4gqBhP66hPR0sSL6c6sA97FKqe9OCIvC6GXp0LIpjcdfTdTEiPkf10QvOlXlfCnk094IUZa20CsmstcYLFjpYV5UnVnHtxGFBJi8I5I/TJa6a3+WdHltjSpATMKtSQiutduwJ1hx23lPv7816cKe7x4cP2I9eONK5dc+JPKqL2fSSe9CMrNNbjOVLwqtpbO3wq6qEUhI3fBxrGsujVsAdfNjk7UKZHsAqaDJHJHEJ+pYvOl8xPMpMyt6PpYE/djEnYhlifqxrT2N7i86jVCERp4gJBopmmAV4i1G4BgvalqD3lF9fDUYxCk7eWlzKY9Za+q+JPNT11hcUTsGhR5AmkFm2ILRHSZDRzq/THI13KtRyAqks2Kta5XyFbx7IECTZTRQ4YXGkXYhU+3U2vA9Hs/fxJ5sCBJspg4sMLzHgx2V8CNCG5HNnQxW1ceAZOWFG4RXDenDtBMQBmFE9zCRKSqzGvDcyGGDLZg9CBeVjyzahgjVonBgQwxB5vJ7eIqLn6+JPMxjIx48aQQlZGmou6aoxWDpRsYu6Bv9NPFTTd05rHo5tTC5kGWRIIZ3CO9mMZmLH6wkMkB8jS7lQXetSdNO3DrnIv8Kk6yPJH35ANaBI8bxJ96n3qfep96mv/EAEYQAAIAAwQFBgkJBgcAAAAAAAECAAMRBCExQRITUWFxEEBSgZGhBSAiIzJCscHRFDBDYnJzdNLhFTNggpKiJFNjk+Lw8f/aAAgBAQANPwD+P5ldHWTwrXbso2rPEbpwj75Y+/WN9oWPxKxvtKx+JHwjMSleYfYB3wJcuZpsgUjSUNQgE7dvM9xhT6OmSp4iJq0dRgGFx5FBYk5ARvlP8I3o490fWYj2iDlKmqx7K15FnmWOCgL7uaZ2iewlyx/M2PVCzWb/AAil1ocqmgjbVREyUyrrEqKkXXjxBgRlFmKskxzVmlk0oTnQ0v3w9qmt/ceZMQqqoqSTgBFK6hr5FlP1um+7AQPRStEUbAouHiCDhPlCoH2lz4ih4w4rLmyzVHG0H/p5HklAsula1BGOV0OxY9ZrzKdVJJVamSM2A6RwGy87Ia8van0T2YxSuhKQAnhW89kD6W0WjVIeoCsZV05h7S3ug5KKDkGJUXLxOAhxVrHZRrTLfJg+CsIrdXmbAMJZHnnHA+iN57IUXzphrMfrN5O4Rhr5gBmNwGC95hsXmMWJ6zyuaKiCpJ3CFGktkDedmbjsrsF8JcsiTc0wbXIvPDlHMHYBQqk1MSaui2geRZVB9Nhm59UX0uuJIoiNMe0tL1k91F7MFvCDtbeIbF5jlies8pwAvrBv1k4eURuXHtpCSmb5TP8AKckCtNw4QP3cpbkljYB7/EVdIhFJoBieYSdKfMoaVVBpU6zQdcWekycK+nOYVJP2QQB17Ys/guZqZgxVjfUf0w2PhTwVSW7Ha8v0WO8RTS+TudTPUb1P6RksxaVG0HAjeIafMBnOFD0FKCuMH1TPUHvMTFK6SmovFI/D/wDKN9n/AFj8P+sZhJAHtMTbOytOmGrtUUpuEDE/PBwSDsrE7XWZdgLV0R2gDrieVnITmGUH4jqjwhZWkjecadmlCTGlaLGmk4NNGu0kXRKYjNWRh3giHu1swVnSfrA4tT+rflExntEqZtR6Fbxc12fcDUQf82WGPbiIUFjZmmEy2pfQV9hqN8DCZJcJpcQQb+EffL8I+9WGNAZyjRrxGEBQKFQQammBuMNgZcsKOwRw+eeiTGU3rNUD2gBu2LAhM6SBfOl4llGd9TTKpGyJTB0dcVIvBgoP2jZl9VxdrVGaNn0TjiDCAJZvCRHk2kC4JO2tkHxOBrmSQkwXo9Oi2B4YjMRYLSBLrkkwFqcAysf5jyMpHcYzNK0hewjEEbiL+Q4wrrJqTiA93dB+ftICzQuKHJxvHsrA85ZrVJNzrkyn2iDfNVBSVOPSHQY5jA5UwhTcymhEG8hRQdkEBauod0GQIa5wNhoRkRE9kecZE120dHSoCjXp6Rx5CT7DyLOnWSp9dECMvZpkdnKZuu9Hyq7K7OYua6IPlyW6SHLhnDXifLFSm5xiD4gmzqWqRMMqctCKUdfYaiOjb5JlzP8AdlY8SkLfo2a3yZga6+5tBsIGEudbhLQ8StTSLMhl2Wx2ZSJclSamlbySbyxJJPNTc6G9HGwjOG2XyJh93d1wL9dIOmtN+Y5EdmWfJNSNKlaqcRwIgXtqz5S/aU3jspF/sPOR9BOqy9WY6ob6ZaaDHecD104w583aZJ0kb4GENVdGKkdYgVpPlUSZgcRg3cd/jJpmzsHICqhQG7Opci/ZzWcjs6OKhgXOUTWoK3mU3RO7YeTYYUFmsrfvCBiU6fDHceWa1ERc/gNpyiz2WVZ9YuEx3nKzsNxOlTcBzSYaKiCv/g3xZ5SSgwwagoT1mpiehTgTgeo0MKSpG8ciMGVlNCpGBB2w9UtPyZlVXmLixBBvIIN22P8AUtNB/aohxR9Up0nGxmJLEbq0i0WxF6kVmPey8znKF87TSSh9UkGlc7tkHFRaSoPEKojfOmH3wDUeXM/NExyxAwqTXlecZuks3RpcBSlDsjdaF/LH4hfyxIB1coNpGppVicyaDLAD+PP/xAAzEQABAwICBgYLAQAAAAAAAAABAgMRAAQSIQUTMDFBURAUIjJhoSBDUFJxgZGx0eHwwf/aAAgBAgEBPwD2GsLI7Bg1eXN1agKMEfD90nTDx4D++ddcvPc8jRvrkd5MfI1ZXLjzhCjlGyWvDlvNPW/WE4Xd3h+aGibYGQD9aJoGaZY1b61AZGP3sXXUNJxLMCk3bz6j1dEDmaQyv1iyfKgAkVris4WhPjw/dJSRvM1OweUUoKhTbeJOueEngOArWBPeEfarm41LeMCauL64cV2xCeVJ0jaAQFeRrrzJ7ppu5xKNJVPpuJC0lJ40gaxnVnIjKsZCZI+NX7QLBUg5eVNPONmAcuXClaNtVGSn70vRrIHYEfOrZpSicecGKbTHpkSINYoUMe/nz/uX06LxpARIkTy/FBnMQQf7xigSojKKIpLaRMCgI2BAUINFtxGbZkcj/hrEh4atwQeX4NO2ChmjPpWsJEnZ6UX2EpG+ZqwuS8iFbxRnhRvW0nCqZovl91KQIE7JSQoYTXU2PdpthtqcAiehdmytRUoZnxNN2zTZlIz9h//EACwRAAIBAgQEBQQDAAAAAAAAAAECAAMRBBIhMRQwMkEQEyAiUCNRYaFCUoH/2gAIAQMBAT8A+DUqD7hKNOjV01BnBU/zOHof2/cGGonY/uYmglNRblKt9YlUUzdJxlXvALwgjQypUzU1B3HJRGc2WNRp0x9Q6/YQuP4iEkmBABdozZu0tyKYuwEdspyJLE7SlTztlJtKeHpqNNTDhq25E8ll6p5YK6Rlt60NjmHaN7XzDaZdbCYViKgDSpTVhcjWDFVQN4uIZuqMQg07yob+sGxvLXF128MM7FrWvaNU0IIIm0BhcmE35AJBuIGRusf6IA1I50N4mMDCzi3iqltuXgl9xMxVII1xsYPzBhajC67Th/Kpszb8oEg3E4ip949Rn6j4LiKijKDHr1HFmPwf/9k=',
					order_id : response.id,
					handler : function(response){
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						console.log('payment successful!!!');
						/*alert('congrats!!!');*/
						
						updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,"paid");
						
						
					},
					prefill: {
						name: "",
						email : "",
						contact : ""
					},
					notes : {
						address : "Connecting people"
						},
					theme: {
						color: "#010c12"
						}
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
			       swal("Failed!!!", "Oops! payment failed, Try again!!!", "error");
});
				rzp.open();
				
			}
		},
		error: function(error){
			//invoked when error
			console.log(error);
			alert("something went wrong")
		} 
		
	}
)
};

//updating payment successful on server
function updatePaymentOnServer(payment_id,order_id,status)
{
	$.ajax({
		url: '/user/updateOrder',
		data: JSON.stringify({payment_id : payment_id,order_id : order_id,status:status}),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response){
			swal("Congrats! Payment Successful,", "Thanks for supporting!", "success");
		},
		error: function(error){
			swal("Failed!!!", "Your payment is successful, but we didn't get it on server, we will contact you ASAP.", "error");
		}
	})
}
