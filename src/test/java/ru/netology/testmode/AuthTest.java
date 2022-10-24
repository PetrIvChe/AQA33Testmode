package ru.netology.testmode;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;


public class AuthTest {

    @BeforeEach
    public void shouldOpenForm() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//input[@type='text']").val("vasya");
        $x("//input[@type='password']").val("password");
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(), 'Личный кабинет')]").should(Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        DataGenerator.RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(getRandomLogin()); //Введен рандомный логин
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        DataGenerator.RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']").should(Condition.visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        DataGenerator.RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        $x("//input[@type='password']").val(getRandomPassword());  //Введен рандомный паспорт
        $x("//span[@class='button__text']").click();
        $x("//div[@class='notification__content']")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));


    }

    @Test
    @DisplayName("Should get error message if  password field is empty ")
    void shouldGetErrorIfPassportFieldIsEmpty() {
        DataGenerator.RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//input[@type='text']").val(user.getLogin());
        // воод паспорта отсутствует
        $x("//span[@class='button__text']").click();
        $("[data-test-id='password'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(10));


    }

    @Test
    @DisplayName("Should get error message if login field is empty")
    void shouldGetErrorMessageIfLoginFieldIsEmpty() {
        DataGenerator.RegistrationInfo user = DataGenerator.Registration.getRegisteredUser("blocked");
        //Ввод логина отсутствует
        $x("//input[@type='password']").val(user.getPassword());
        $x("//span[@class='button__text']").click();
        $("[data-test-id='login'].input_invalid .input__sub")
                .shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(10));
    }
}
