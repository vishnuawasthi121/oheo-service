
package com.ogive.oheo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class RazorPayConfig {

	@Value("${razorpay.payment.apiKey}")
	private String apiKey;

	@Value("${razorpay.payment.secretKey}")
	private String secretKey;

	@Bean
	public RazorpayClient razorpayClient() throws RazorpayException {
		RazorpayClient client = new RazorpayClient(this.apiKey, this.secretKey);
		return client;
	}
}
