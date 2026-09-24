package com.spotit.api.shop.controller;

final class ProductConfigControllerSwagger {
    private ProductConfigControllerSwagger() {
    }

    static final String LIST_SUMMARY = "List products";
    static final String LIST_DESCRIPTION = "Returns every product in the catalog, including inactive ones, for admin management.";
    static final String LIST_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                { "id": "theme_dark_rose", "name": "Dark Rose Theme", "cost": 300, "minLevel": "bronze", "premiumOnly": false, "icon": "🎨", "active": true },
                { "id": "profile_frame_gold", "name": "Gold Profile Frame", "cost": 1000, "minLevel": "gold", "premiumOnly": true, "icon": "🖼️", "active": true }
              ]
            }
            """;

    static final String GET_SUMMARY = "Get a product";
    static final String GET_DESCRIPTION = "Returns a single product by id, for admin management.";
    static final String GET_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": { "id": "theme_dark_rose", "name": "Dark Rose Theme", "cost": 300, "minLevel": "bronze", "premiumOnly": false, "icon": "🎨", "active": true }
            }
            """;

    static final String NOT_FOUND_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Product not found.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String CREATE_SUMMARY = "Create a product";
    static final String CREATE_DESCRIPTION = "Adds a new product to the rewards-shop catalog.";
    static final String CREATE_REQUEST_EXAMPLE = """
            {
              "id": "theme_dark_rose",
              "name": "Dark Rose Theme",
              "cost": 300,
              "minLevel": "bronze",
              "premiumOnly": false,
              "icon": "🎨"
            }
            """;
    static final String CREATE_201_EXAMPLE = """
            {
              "code": 201,
              "message": "Created",
              "data": { "id": "theme_dark_rose", "name": "Dark Rose Theme", "cost": 300, "minLevel": "bronze", "premiumOnly": false, "icon": "🎨", "active": true }
            }
            """;
    static final String CREATE_409_EXAMPLE = """
            {
              "code": 409,
              "message": "A product with id 'theme_dark_rose' already exists.",
              "data": { "errorCode": "resource_already_exists" }
            }
            """;

    static final String UPDATE_SUMMARY = "Update a product";
    static final String UPDATE_DESCRIPTION = "Partially updates a product; only non-null fields are applied.";
    static final String UPDATE_REQUEST_EXAMPLE = """
            {
              "name": "Dark Rose Theme",
              "cost": 350,
              "minLevel": "bronze",
              "premiumOnly": false,
              "icon": "🎨",
              "active": true
            }
            """;
    static final String UPDATE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": { "id": "theme_dark_rose", "name": "Dark Rose Theme", "cost": 350, "minLevel": "bronze", "premiumOnly": false, "icon": "🎨", "active": true }
            }
            """;

    static final String DELETE_SUMMARY = "Delete a product";
    static final String DELETE_DESCRIPTION = "Removes a product from the catalog.";
    static final String DELETE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Product deleted.",
              "data": null
            }
            """;
}
