package com.lengmin.tony.crawler.main.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lengmin.tony.crawler.main.model.Category;
import com.lengmin.tony.crawler.main.model.Product;
import com.lengmin.tony.crawler.main.repository.CrawlerRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */

@Repository
public class CrawlerRepositoryImpl implements CrawlerRepository {

    private final ObjectMapper mapper;

    public CrawlerRepositoryImpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public void saveCategories(List<Category> categories) {
        saveFile("categories.json", categories);
    }

    @Override
    public void saveProducts(List<Product> products) {
        saveFile("products.json", products);
    }

    public <T> void saveFile(String filename, List<T> elements) {
        String FOLDER_PREFIX = "data/";
        File file = new File(FOLDER_PREFIX + filename);
        try {
            boolean newFile = file.createNewFile();

            if (newFile) {
                FileOutputStream outputStream = new FileOutputStream(file, false);
                mapper.writeValue(outputStream, elements);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
