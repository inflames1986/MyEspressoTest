package com.inflames1986.uiautomator.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.inflames1986.uiautomator.TestConstants
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(
            Until.hasObject(By.pkg(packageName).depth(TestConstants.EXACT_DEPT)),
            TestConstants.TIMEOUT
        )
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, TestConstants.SEARCH_EDIT_TEXT_ID))
        //Проверяем на null
        Assert.assertNotNull(editText)
    }

    //Убеждаемся, что поиск работает как ожидается
    @Test
    fun test_SearchIsPositive() {
        val editText = uiDevice.findObject(By.res(packageName, TestConstants.SEARCH_EDIT_TEXT_ID))
        editText.text = TestConstants.QUERY_SECOND
        uiDevice.findObject(By.res(packageName, TestConstants.FIND_BUTTON)).click()
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, TestConstants.TOTAL_COUNT)),
                TestConstants.TIMEOUT
            )
        Assert.assertEquals(changedText.text.toString(), TestConstants.FIND_RESULT_STRING)
    }

    //Убеждаемся, что DetailsScreen открывается
    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                TestConstants.TO_DETAILS_BUTTON_ID
            )
        )
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, TestConstants.TOTAL_COUNT)),
                TestConstants.TIMEOUT
            )
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(changedText.text, "Number of results: 0")
    }

    //Убеждаемся, что DetailsScreen открывается и после успешного выполнения запроса отображает
    //именно полученное количество найденных репозиториев
    @Test
    fun test_OpenDetailsScreenAndCheckResult() {
        //Находим searchEditText и ставим ему текст AppDelivery
        uiDevice.findObject(By.res(packageName, TestConstants.SEARCH_EDIT_TEXT_ID)).text =
            TestConstants.QUERY
        //Находим кнопку FIND (find_button) и вызываем клик
        uiDevice.findObject(By.res(packageName, TestConstants.FIND_BUTTON)).click()
        //Находим totalCountTextView и в resultValue заносим его текстовое значение
        val resultValue =
            uiDevice.wait(
                Until.findObject(By.res(packageName, TestConstants.TOTAL_COUNT)),
                TestConstants.TIMEOUT
            ).text.toString()
        //Находим кнопку TO DETAILS (toDetailsActivityButton) и вызываем клик
        uiDevice.findObject(
            By.res(
                packageName,
                TestConstants.TO_DETAILS_BUTTON_ID
            )
        ).click()
        //На экране детализации также находим totalCountTextView и заносим его текстовое значение
        //в detailValue.
        val detailValue = uiDevice.wait(
            Until.findObject(By.res(packageName, TestConstants.TOTAL_COUNT)),
            TestConstants.TIMEOUT
        ).text.toString()
        //Сравниваем значение, которое получилось в результате поиска на MainActivity со значением
        //которое выводится в DetailsActivity
        Assert.assertEquals(resultValue, detailValue)
    }

    //Убеждаемся, что при поиске появляется ProgressBar, если запрос не пустой
    @Test
    fun test_ShowProgressWhenSearch() {
        val editText = uiDevice.findObject(By.res(packageName, TestConstants.SEARCH_EDIT_TEXT_ID))
        editText.text = TestConstants.QUERY_SECOND
        uiDevice.findObject(By.res(packageName, TestConstants.FIND_BUTTON)).click()
        val progress = uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.PROGRESS_BAR_ID
                )
            ),
            TestConstants.TIMEOUT
        )
        Assert.assertNotNull(progress)
    }

    //Убеждаемся, что при поиске не появляется ProgressBar, если запрос пустой
    @Test
    fun test_NotShowProgressWhenSearchWithEmptyQuery() {
        val editText = uiDevice.findObject(By.res(packageName, TestConstants.SEARCH_EDIT_TEXT_ID))
        editText.text = ""
        uiDevice.findObject(By.res(packageName, TestConstants.FIND_BUTTON)).click()
        val progress = uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.PROGRESS_BAR_ID
                )
            ),
            TestConstants.TIMEOUT
        )
        Assert.assertNull(progress)
    }

    //Убеждаемся, что при клике по инкременту на экране детализации отобразится "Number of results: 1"
    @Test
    fun test_ClickToIncrementAndCheckValue() {
        uiDevice.findObject(By.res(packageName, TestConstants.TO_DETAILS_BUTTON_ID)).click()
        uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.INCREMENT_BUTTON_ID
                )
            ),
            TestConstants.TIMEOUT
        )?.click()
        val detailValue =
            uiDevice.findObject(
                By.res(packageName, TestConstants.TOTAL_COUNT)
            ).text.toString()
        Assert.assertEquals(detailValue, TestConstants.FIND_RESULT_INCREMENT)
    }

    //Убеждаемся, что при клике по инкременту на экране детализации отобразится "Number of results: -1"
    @Test
    fun test_ClickToDecrimentAndCheckValue() {
        uiDevice.findObject(By.res(packageName, TestConstants.TO_DETAILS_BUTTON_ID)).click()
        uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.DECREMENT_BUTTON_ID
                )
            ),
            TestConstants.TIMEOUT
        )?.click()

        val detailValue =
            uiDevice.findObject(
                By.res(packageName, TestConstants.TOTAL_COUNT)
            ).text.toString()
        Assert.assertEquals(detailValue, TestConstants.FIND_RESULT_DECREMENT)
    }

    //Убеждаемся, что при клике на BACK откроется MainActivity, если мы находимся на DetailActivity
    @Test
    fun test_ClickBackFromDetailActivity() {
        //Перейдем на экран детализации
        uiDevice.findObject(By.res(packageName, TestConstants.TO_DETAILS_BUTTON_ID)).click()
        //Убедимся, что экран открыт. Для этого проверим наличие нокпи декремента, например.
        uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.DECREMENT_BUTTON_ID
                )
            ),
            TestConstants.TIMEOUT
        )
        //Жмакаем кнопку Back
        uiDevice.pressBack()
        //Ищем кнопку FIND, чтобы убедиться, что мы вернулись на экран MainActivity
        val findButton = uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.FIND_BUTTON
                )
            ),
            TestConstants.TIMEOUT
        )
        Assert.assertNotNull(findButton)
    }

    //Убеждаемся, что при клике на BACK приложение закроется, если мы находимся на MainActivity
    @Test
    fun test_ClickBackFromMainActivity() {
        uiDevice.pressBack()
        //Ищем кнопку FIND, чтобы убедиться, что мы ее не найдем )
        val findButton = uiDevice.wait(
            Until.findObject(
                By.res(
                    packageName,
                    TestConstants.FIND_BUTTON
                )
            ),
            TestConstants.TIMEOUT
        )
        Assert.assertNull(findButton)
    }
}