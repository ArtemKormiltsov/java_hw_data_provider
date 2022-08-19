package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ParameterizedWebTest {

        @ValueSource(strings = {"Kotlin", "Java"})
        @ParameterizedTest(name = "По результатам поиска на странице отображаются {0}")
        void gitGubSearchTest(String testData) {
            open("https://github.com/");
            $("[placeholder='Search GitHub']").setValue(testData).pressEnter();
            $$(".flex-auto").shouldBe(CollectionCondition.sizeGreaterThan(0));
        }

    @CsvSource(value = {
            "Kotlin | Kotlin is a statically typed programming language for multiplatform applications.",
            "Java | Java is an object-oriented programming language used mainly for web, desktop, embedded devices and mobile applications."},
    delimiter = '|')
    @ParameterizedTest(name = "Результаты перехода на страницу содержат {1} для запроса {0}")
    void checkSearchDataTest(String testData, String expectedResult) {
        open("https://github.com/");
        $("[placeholder='Search GitHub']").setValue(testData).pressEnter();
        $$(".d-md-flex").first().shouldBe(text(expectedResult));
    }

    static Stream<Arguments> tlNavigationTest() {
        return Stream.of(
                Arguments.of("Русский", List.of("RU","Twitter","Главная", "FAQ", "Приложения", "API", "Протокол")),
                Arguments.of("English", List.of("EN","Twitter","Home", "FAQ", "Apps", "API", "Protocol"))
        );
    }
    @MethodSource("tlNavigationTest")
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки {1}")
    void tlNavigationTest(String lang, List<String> expectedButtons) {
        open("https://desktop.telegram.org/");
        $(".dropdown-toggle").click();
        $$(".dropdown-menu a").find(text(lang)).click();
        $$(".nav.navbar-nav a").filter(visible).shouldHave(texts(expectedButtons));
    }
}

