package com.lengmin.tony.crawler.main.repository;

import com.lengmin.tony.crawler.main.model.Category;
import com.lengmin.tony.crawler.main.model.Product;

import java.util.List;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */

public interface CrawlerRepository {
    void saveCategories(List<Category> categories);

    void saveProducts(List<Product> products);
}
