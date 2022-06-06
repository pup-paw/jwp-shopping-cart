package woowacourse.shoppingcart.domain;

import woowacourse.shoppingcart.domain.product.Product;

public class Cart {

    private final Long id;
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;

    public Cart() {
        this(null, null, null, 0, null);
    }

    public Cart(Long id, Product product) {
        this(id, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Cart(Long id, Long productId, String name, int price, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
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
}
