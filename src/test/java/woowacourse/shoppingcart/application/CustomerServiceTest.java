package woowacourse.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static Fixture.CustomerFixtures.MAT_ADDRESS;
import static Fixture.CustomerFixtures.MAT_EMAIL;
import static Fixture.CustomerFixtures.MAT_PHONE_NUMBER;
import static Fixture.CustomerFixtures.MAT_SAVE_REQUEST;
import static Fixture.CustomerFixtures.MAT_USERNAME;
import static Fixture.CustomerFixtures.UPDATE_ADDRESS;
import static Fixture.CustomerFixtures.UPDATE_PHONE_NUMBER;
import static Fixture.CustomerFixtures.UPDATE_REQUEST;
import static Fixture.CustomerFixtures.YAHO_ADDRESS;
import static Fixture.CustomerFixtures.YAHO_EMAIL;
import static Fixture.CustomerFixtures.YAHO_PHONE_NUMBER;
import static Fixture.CustomerFixtures.YAHO_SAVE_REQUEST;
import static Fixture.CustomerFixtures.YAHO_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.customer.LoginCustomer;
import woowacourse.shoppingcart.exception.InvalidCustomerException;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Sql("/truncate.sql")
class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest(CustomerService customerService) {
        this.customerService = customerService;
    }

    @DisplayName("customer를 저장한다.")
    @Test
    void save() {
        CustomerSaveRequest request = MAT_SAVE_REQUEST;

        CustomerResponse response = customerService.save(request);

        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(response.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(response.getAddress()).isEqualTo(MAT_ADDRESS);
            assertThat(response.getPhoneNumber()).isEqualTo(MAT_PHONE_NUMBER);
        });
    }

    @DisplayName("customer의 username을 활용하여 조회한다.")
    @Test
    void find() {
        CustomerSaveRequest request = YAHO_SAVE_REQUEST;
        customerService.save(request);

        CustomerResponse response = customerService.find(new LoginCustomer(request.getUsername()));

        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getUsername()).isEqualTo(YAHO_USERNAME);
            assertThat(response.getEmail()).isEqualTo(YAHO_EMAIL);
            assertThat(response.getAddress()).isEqualTo(YAHO_ADDRESS);
            assertThat(response.getPhoneNumber()).isEqualTo(YAHO_PHONE_NUMBER);
        });
    }

    @DisplayName("존재하지 않는 username인 경우 예외를 던진다.")
    @Test
    void find_error_notExist_username() {
        assertThatThrownBy(() -> customerService.find(new LoginCustomer("merong")))
                .isInstanceOf(InvalidCustomerException.class);
    }

    @DisplayName("customer를 수정한다.")
    @Test
    void update() {
        CustomerSaveRequest request = MAT_SAVE_REQUEST;
        customerService.save(request);

        customerService.update(new LoginCustomer(MAT_USERNAME), UPDATE_REQUEST);

        CustomerResponse response = customerService.find(new LoginCustomer(MAT_USERNAME));
        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(response.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(response.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(response.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
        });
    }

    @DisplayName("존재하지 않는 username을 수정하는 경우 예외를 던진다.")
    @Test
    void update_error_notExist_username() {
        assertThatThrownBy(() -> customerService.update(new LoginCustomer("merong"), UPDATE_REQUEST))
                .isInstanceOf(InvalidCustomerException.class);
    }

    @DisplayName("customer를 삭제한다.")
    @Test
    void delete() {
        customerService.save(YAHO_SAVE_REQUEST);

        customerService.delete(new LoginCustomer(YAHO_USERNAME));

        assertThatThrownBy(() -> customerService.find(new LoginCustomer(YAHO_USERNAME)))
                .isInstanceOf(InvalidCustomerException.class);
    }

    @DisplayName("존재하지 않는 username을 삭제하는 경우 예외를 던진다.")
    @Test
    void delete_error_notExist_username() {
        assertThatThrownBy(() -> customerService.delete(new LoginCustomer(YAHO_USERNAME)))
                .isInstanceOf(InvalidCustomerException.class);
    }
}
