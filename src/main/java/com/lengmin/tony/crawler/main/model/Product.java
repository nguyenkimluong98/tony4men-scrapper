package com.lengmin.tony.crawler.main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: luongnk
 * @since: 13/04/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Integer id;
    private String code;
    private String title;
    private Integer quantity;
    private Integer price;
    private List<String> images;
    private List<String> sizes;
    private List<ProductColor> colors;
    private Integer categoryId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductColor {
        private String color;
        private String image;
    }
}
