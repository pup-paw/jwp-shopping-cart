package woowacourse.shoppingcart.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import woowacourse.shoppingcart.domain.product.Product;
import woowacourse.shoppingcart.exception.NoSuchProductException;

@Repository
public class ProductDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Product product) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(product);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    private RowMapper<Product> mapToProduct() {
        return (resultSet, rowNum) ->
                new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("price"),
                        resultSet.getString("image_url"),
                        resultSet.getBoolean("selling"),
                        resultSet.getString("description"));
    }

    public Product findProductById(Long productId) {
        try {
            String sql = "SELECT id, name, price, image_url, selling, description FROM product WHERE id = :id";
            SqlParameterSource parameterSource = new MapSqlParameterSource("id", productId);
            return jdbcTemplate.queryForObject(sql, parameterSource, mapToProduct());
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchProductException();
        }
    }

    public List<Product> findSellingProducts() {
        String sql = "SELECT id, name, price, image_url, selling, description FROM product WHERE selling = true";
        return jdbcTemplate.query(sql, mapToProduct());
    }

    public void delete(Long productId) {
        String sql = "UPDATE product SET selling = false WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", productId);
        jdbcTemplate.update(sql, parameterSource);
    }
}
