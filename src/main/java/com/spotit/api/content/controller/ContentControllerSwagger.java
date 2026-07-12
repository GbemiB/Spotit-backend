package com.spotit.api.content.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link ContentController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class ContentControllerSwagger {

    private ContentControllerSwagger() {
    }

    static final String FEED_SUMMARY = "Get the content feed";
    static final String FEED_DESCRIPTION = "Returns the current content feed items, ordered by sort order, up to an optional limit (default 10).";
    static final String FEED_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "items": [
                  {
                    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                    "tag": "wellness",
                    "title": "5 ways to ease cramps",
                    "imageUrl": "https://cdn.spotit.app/content/cramps.jpg",
                    "sponsored": false,
                    "advertiser": null
                  },
                  {
                    "id": "9d1e2b0a-1234-4c56-9abc-1234567890ab",
                    "tag": "promo",
                    "title": "20% off at HealthCo",
                    "imageUrl": "https://cdn.spotit.app/content/healthco.jpg",
                    "sponsored": true,
                    "advertiser": "HealthCo"
                  }
                ]
              }
            }
            """;
}
