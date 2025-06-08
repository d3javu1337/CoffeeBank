namespace backend.http;

public static class PaymentAccountEndpoints
{
    /*
     * 1. open payment account
     * 2. get payment account
     */
    public static void MapPaymentAccountEndpoints(this WebApplication app)
    {
        app.MapPost("/account", () => { });
        app.MapGet("/account", () => { });
    }
}