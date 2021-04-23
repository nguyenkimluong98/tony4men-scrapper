package com.lengmin.tony.crawler.main.service.impl;

import com.lengmin.tony.crawler.main.configuration.ApplicationConfiguration;
import com.lengmin.tony.crawler.main.model.Category;
import com.lengmin.tony.crawler.main.model.Product;
import com.lengmin.tony.crawler.main.repository.CrawlerRepository;
import com.lengmin.tony.crawler.main.service.CrawlerService;
import com.lengmin.tony.crawler.main.utils.JSoupUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */

@Service
@RequiredArgsConstructor
public class CrawlerServiceImpl implements CrawlerService {

    private final CrawlerRepository crawlerRepository;
    private final ApplicationConfiguration configuration;

    @Override
    public List<Category> getCategoryList(String domain) {
        Document document = JSoupUtils.get(domain);

        assert document != null;
        Elements liMenus = document.select("#nav > nav > ul > li");

        List<Category> categories = new ArrayList<>();

        for (Element liMenu : liMenus) {
            Elements menus = liMenu.select("li > a");
            int currentParentId = categories.size();

            for (int j = 0; j < menus.size(); j++) {
                String name = menus.get(j).text();
                String url = menus.get(j).attr("href");

                Category category = new Category(categories.size(), null, name, url);

                if (j > 0) {
                    category.setParentId(currentParentId);
                }

                categories.add(category);
            }
        }

//        crawlerRepository.saveCategories(categories);

        return categories;
    }

    @Override
    public List<Product> getProductList(List<Category> categories) {
        List<Product> productList = new ArrayList<>();

        for (Category category : categories) {
            if (category.getParentId() != null) {
                List<Product> products = getCategoryPage(category);
                productList.addAll(products);
            }
        }

        crawlerRepository.saveProducts(productList);

        return productList;
    }

    private List<Product> getCategoryPage(Category category) {
        final String url = configuration.getWebsite() + category.getUrl();
        Document document = JSoupUtils.get(url);

        assert document != null;
        int currentPage = 1;
        final int maxPageNumber = getMaxPagination(document);

        List<Product> totalProducts = new ArrayList<>();

        do {
            if (currentPage > 1) {
                document = JSoupUtils.get(url + "?page=" + currentPage);
            }

            assert document != null;
            List<String> productUrls = getProductUrlList(document);
            List<Product> products = getProductDetailList(productUrls, category.getId());

            totalProducts.addAll(products);
            System.out.println("Crawled page: " + currentPage + " - category: " + category.getId() + " - numberProducts: " + products.size());
            currentPage++;
        } while (currentPage <= maxPageNumber);

        return totalProducts;
    }

    private List<Product> getProductDetailList(List<String> productUrls, Integer categoryId) {
        List<Product> products = new ArrayList<>();

        for (String productUrl : productUrls) {
            Product product = getProductDetail(productUrl);
            product.setCategoryId(categoryId);

            products.add(product);
        }

        return products;
    }

    private Product getProductDetail(String productUrl) {
        Document document = JSoupUtils.get(configuration.getWebsite() + productUrl);
        assert document != null;

        String code = document.select("#detail-product > div.product-title.tp_product_detail_name > div > span").text();
        String title = document.select("#detail-product > div.product-title.tp_product_detail_name > h1").text();
        Integer price = getProductPrice(document);
        List<String> sizes = document.select("#variant-swatch-1 > div.select-swap.attr-size.req > div > label > span").eachText();
        List<String> images = document.select("#product > div:nth-child(2) > div > div > div.clearfix.product-detail-main.pr_style_01 > div > div.col-md-8.col-sm-7.col-xs-12 > div.clearfix.hidden-xs.col-sm-1.thumbnails.small-img > div > div > div.pview-thumb-slide > div > img").eachAttr("src");
        List<Product.ProductColor> colors = getProductColors(document);

        return Product.builder()
                .code(code)
                .title(title)
                .price(price)
                .sizes(sizes)
                .images(images)
                .colors(colors)
                .build();
    }

    private List<Product.ProductColor> getProductColors(Document document) {
        Elements colorElements = document.select("#variant-swatch-0 > div.select-swap.attr-color.req > div > label > img");

        return colorElements.stream()
                .map(element -> new Product.ProductColor(element.attr("alt"), element.attr("src")))
                .collect(Collectors.toList());
    }

    private Integer getProductPrice(Document document) {
        String price = document.select("#price-preview > span").text();

        // 295,000d -> 295 * 1000 = 295000
        return Integer.parseInt(price.split(",")[0]) * 1000;
    }

    private List<String> getProductUrlList(Document document) {
        Elements elements = document.select("#collection-body > div.clearfix.filter-here > div.content-product-list.product-list.filter.clearfix.fixBox > div > div > div.product-img.image-resize > a");
        return elements.eachAttr("href");
    }

    private int getMaxPagination(Document document) {
        Elements lastElement = document.select("#pagination > div > div > a.paging-last");
        String lastUrl = lastElement.attr("href");

        String[] split = lastUrl.split("=");
        System.out.println("split: " + Arrays.toString(split));
        return split.length > 1 ? Integer.parseInt(split[1]) : 1;
    }
}
