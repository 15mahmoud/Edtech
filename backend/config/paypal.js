const paypal = require("@paypal/checkout-server-sdk");
require("dotenv").config();

// Set up PayPal environment with your client ID and secret
const environment = new paypal.core.SandboxEnvironment(
  process.env.PAYPAL_CLIENT_ID,
  process.env.PAYPAL_SECRET
);
const client = new paypal.core.PayPalHttpClient(environment);

// Create a function to handle payment
exports.instance = async (amount) => {
  try {
    const request = new paypal.orders.OrdersCreateRequest();
    request.prefer("return=representation");
    request.requestBody({
      intent: "CAPTURE",
      purchase_units: [
        {
          amount: {
            currency_code: "USD",
            value: amount,
          },
        },
      ],
    });

    const order = await client.execute(request);
    return order.result;
  } catch (error) {
    console.error("Error creating PayPal order:", error);
    throw error;
  }
};
