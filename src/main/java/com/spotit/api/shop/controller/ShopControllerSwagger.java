package com.spotit.api.shop.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link ShopController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class ShopControllerSwagger {

    private ShopControllerSwagger() {
    }

    static final String PRODUCTS_SUMMARY = "List shop products";
    static final String PRODUCTS_DESCRIPTION = "Returns every active product with lock status computed for the current user's level and Premium status.";
    static final String PRODUCTS_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                { "id": "theme_dark_rose", "name": "Dark Rose Theme", "cost": 300, "minLevel": "bronze", "premiumOnly": false, "locked": false, "lockReason": null },
                { "id": "profile_frame_gold", "name": "Gold Profile Frame", "cost": 1000, "minLevel": "gold", "premiumOnly": true, "locked": true, "lockReason": "premium_required" }
              ]
            }
            """;

    static final String REDEEM_SUMMARY = "Redeem a product";
    static final String REDEEM_DESCRIPTION = "Spends SpotPoints to redeem a product, creating an order. Fails if the product doesn't exist/is inactive, the user's level is too low, Premium is required, or the balance is insufficient.";
    static final String REDEEM_REQUEST_EXAMPLE = """
            {
              "productId": "theme_dark_rose"
            }
            """;
    static final String REDEEM_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "orderId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "productId": "theme_dark_rose",
                "pointsSpent": 300,
                "newBalance": 180,
                "status": "processing"
              }
            }
            """;
    static final String REDEEM_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Product not found.",
              "data": { "errorCode": "not_found" }
            }
            """;
    static final String REDEEM_403_EXAMPLE = """
            {
              "code": 403,
              "message": "This item requires Premium.",
              "data": { "errorCode": "premium_required" }
            }
            """;
    static final String REDEEM_402_EXAMPLE = """
            {
              "code": 402,
              "message": "Not enough SpotPoints yet.",
              "data": { "errorCode": "insufficient_points" }
            }
            """;

    static final String ORDERS_SUMMARY = "List orders";
    static final String ORDERS_DESCRIPTION = "Returns the current user's redemption order history, most recent first.";
    static final String ORDERS_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                {
                  "orderId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                  "productId": "theme_dark_rose",
                  "status": "processing",
                  "createdAt": "2026-07-10T09:00:00Z"
                }
              ]
            }
            """;
}
