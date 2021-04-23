package com.lengmin.tony.crawler.main;

import com.lengmin.tony.crawler.main.configuration.ApplicationConfiguration;
import com.lengmin.tony.crawler.main.model.Category;
import com.lengmin.tony.crawler.main.model.Product;
import com.lengmin.tony.crawler.main.service.CrawlerService;
import com.lengmin.tony.crawler.main.utils.JSoupUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@SpringBootApplication
public class Tony4menCrawlerApplication {

	@Autowired
	private CrawlerService crawlerService;

	@Autowired
	private ApplicationConfiguration configuration;

	public static void main(String[] args) {
		SpringApplication.run(Tony4menCrawlerApplication.class, args);
	}

	@PostConstruct
	void initialize() {
		JSoupUtils.init(configuration);
		List<Category> categoryList = crawlerService.getCategoryList(configuration.getWebsite());
		List<Product> productList = crawlerService.getProductList(categoryList);
		log.info("done");
	}
}
