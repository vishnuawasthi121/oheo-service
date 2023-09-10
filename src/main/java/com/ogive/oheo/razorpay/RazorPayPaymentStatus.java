package com.ogive.oheo.razorpay;

public enum RazorPayPaymentStatus {
	/**
	 * A payment is created when the customer submits payment details to Razorpay.
	 * The payment has not been processed at this stage.
	 */
	Created,
	/**
	 * ' When the customer's payment details are successfully authenticated by the
	 * bank, the Payment state changes to Authorized. The amount deducted from the
	 * customer’s account by Razorpay is not settled to your account until the
	 * payment is captured, either manually or automatically. There can be scenarios
	 * where payment is interrupted due to external factors, such as network issues
	 * or technical errors at the customer's or bank's end. In this case, the amount
	 * may get debited from the customer's bank account but the payment status is
	 * not received by Razorpay from the bank. This is termed as Late Authorization.
	 */
	Authorized,
	/**
	 * The payment status changes to captured when the authorized payment is
	 * verified to be complete by Razorpay. The amount is settled to your account as
	 * per the settlement schedule of the bank. The captured amount must be the same
	 * as the authorized amount. Any authorization not followed by a capture within
	 * 5 days is automatically voided and the amount is refunded to the customer.
	 */
	Captured,

	/**
	 * The amount is refunded to the customer's account.
	 */
	Refunded,
	/**
	 * Any unsuccessful transaction is marked as failed. The customer will have to
	 * retry the payment.
	 */
	Failed
}
