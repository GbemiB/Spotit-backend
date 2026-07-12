package com.spotit.api.content.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link ContentConfigController}, kept out of the controller so endpoint
 * methods stay focused on request handling. Mirrors the companion-class
 * convention used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class ContentConfigControllerSwagger {

    private ContentConfigControllerSwagger() {
    }

    static final String LIST_SUMMARY = "List content items";
    static final String LIST_DESCRIPTION = "Returns every content item in the catalog, including unsponsored and inactive ones, for admin management.";
    static final String LIST_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                  "tag": "wellness",
                  "title": "5 ways to ease cramps",
                  "imageUrl": "https://cdn.spotit.app/content/cramps.jpg",
                  "sponsored": false,
                  "advertiser": null,
                  "sortOrder": 1
                },
                {
                  "id": "9d1e2b0a-1234-4c56-9abc-1234567890ab",
                  "tag": "promo",
                  "title": "20% off at HealthCo",
                  "imageUrl": "https://cdn.spotit.app/content/healthco.jpg",
                  "sponsored": true,
                  "advertiser": "HealthCo",
                  "sortOrder": 2
                }
              ]
            }
            """;

    static final String GET_SUMMARY = "Get a content item";
    static final String GET_DESCRIPTION = "Returns a single content item by id, for admin management.";
    static final String GET_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "tag": "wellness",
                "title": "5 ways to ease cramps",
                "imageUrl": "https://cdn.spotit.app/content/cramps.jpg",
                "sponsored": false,
                "advertiser": null,
                "sortOrder": 1
              }
            }
            """;

    static final String NOT_FOUND_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Content item not found.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String CREATE_SUMMARY = "Create a content item";
    static final String CREATE_DESCRIPTION = "Adds a new item to the content-feed catalog.";
    static final String CREATE_REQUEST_EXAMPLE = """
            {
              "tag": "wellness",
              "title": "5 ways to ease cramps",
              "imageUrl": "https://cdn.spotit.app/content/cramps.jpg",
              "sponsored": false,
              "advertiser": null,
              "sortOrder": 1
            }
            """;
    static final String CREATE_201_EXAMPLE = """
            {
              "code": 201,
              "message": "Created",
              "data": {
                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "tag": "wellness",
                "title": "5 ways to ease cramps",
                "imageUrl": "https://cdn.spotit.app/content/cramps.jpg",
                "sponsored": false,
                "advertiser": null,
                "sortOrder": 1
              }
            }
            """;

    static final String UPDATE_SUMMARY = "Update a content item";
    static final String UPDATE_DESCRIPTION = "Partially updates a content item; only non-null fields are applied.";
    static final String UPDATE_REQUEST_EXAMPLE = """
            {
              "tag": "wellness",
              "title": "5 ways to ease cramps (updated)",
              "imageUrl": null,
              "sponsored": null,
              "advertiser": null,
              "sortOrder": 2
            }
            """;
    static final String UPDATE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "tag": "wellness",
                "title": "5 ways to ease cramps (updated)",
                "imageUrl": "https://cdn.spotit.app/content/cramps.jpg",
                "sponsored": false,
                "advertiser": null,
                "sortOrder": 2
              }
            }
            """;

    static final String DELETE_SUMMARY = "Delete a content item";
    static final String DELETE_DESCRIPTION = "Removes a content item from the catalog.";
    static final String DELETE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Content item deleted.",
              "data": null
            }
            """;
}
