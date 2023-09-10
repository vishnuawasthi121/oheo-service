package com.ogive.oheo.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ogive.oheo.razorpay.PaymentOrder;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import io.swagger.annotations.ApiOperation;

@Controller
public class RazorpayOperationController {

	private static final Logger LOG = LoggerFactory.getLogger(RazorpayOperationController.class);

	@Autowired
	private RazorpayClient razorpay;
	
	@Value("${razorpay.payment.apiKey}")
	private String apiKey;

	@Value("${razorpay.payment.secretKey}")
	private String secretKey;

	@ApiOperation(value = "PayNow button")
	@PostMapping(path = "/paynow")
	public String addServiceProvider(Model model) {
		LOG.info("addServiceProvider request received@@   {}");
		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", 1); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");
			Order order = razorpay.orders.create(orderRequest);
			Object order_id = order.get("order_id");
			model.addAttribute("order_id", order_id);
		} catch (RazorpayException | JSONException e) {
			// Handle Exception
			System.out.println(e.getMessage());
		}
		return "payNow";
	}

	@ApiOperation(value = "PayNow button", consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public String indexPage(Model model) {
		LOG.info("indexPage - Started");
		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", 1); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");
			Order order = razorpay.orders.create(orderRequest);
			Object order_id = order.get("order_id");
			PaymentOrder  paymentOrder  = new PaymentOrder();
			paymentOrder.setOrder_id((String) order_id);
			model.addAttribute("paymentOrder", paymentOrder);
			
		} catch (RazorpayException | JSONException e) {
			// Handle Exception
			System.out.println(e.getMessage());
		}
		return "welcome";
	}

}
