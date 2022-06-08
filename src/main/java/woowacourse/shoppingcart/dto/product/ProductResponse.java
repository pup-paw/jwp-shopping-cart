package woowacourse.shoppingcart.dto.product;

import woowacourse.shoppingcart.domain.product.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final Integer price;
    private final String imageUrl;
    private final String description;

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getDescription());
    }

    public ProductResponse(Long id, String name, int price, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
