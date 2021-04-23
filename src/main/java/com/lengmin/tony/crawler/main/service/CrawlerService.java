package com.lengmin.tony.crawler.main.service;

import com.lengmin.tony.crawler.main.model.Category;
import com.lengmin.tony.crawler.main.model.Product;

import java.util.List;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */

public interface CrawlerService {
    List<Category> getCategoryList(String url);

    List<Product> getProductList(List<Category> categories);
}
