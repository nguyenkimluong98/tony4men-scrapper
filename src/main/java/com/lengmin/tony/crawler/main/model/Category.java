package com.lengmin.tony.crawler.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: luongnk
 * @since: 12/04/2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer id;
    private Integer parentId;
    private String name;
    private String url;
}
