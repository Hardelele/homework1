# Домашняя работа #1

## Задание
Реализовать программу, которая получает ключевое слово для поиска на [Wikipedia](https://wikipedia.org),
достает из результатов текст и на основе динамически загружаемых плагинов на выходе формирует отчет и записывает результат в файл.

## Требования
1. Консольное приложение на вход получает слово или фразу для поиска статьи на Wikipedia как аргументы запуска или через stdin.
1. Необходимо реализовать класс, выполняющий запрос к Wikipeadia, результат получать в формате JSON и с помощью Json Parser'а или JsonPath достать текст статьи.
1. Необходимо реализовать динамическую загрузку и исполнение кода из jar файлов, реализующих дополнительный функционал.
Для простоты будем называть это плагинами. Плагины должны реализовывать интерфейс [PluginInterface](/plugin-interface/src/main/java/ru/digitalhabbits/homework1/plugin/PluginInterface.java),
а после загрузки приложение должно через Reflection API вызвать метод `String apply(String text)`.  
В проекте созданы подпроекты для двух плагинов:
    - [Counter Plugin](/counter-plugin) – подсчет количество строк, слов, букв. На вход плагин получает текст,
на выходе формирует _только_ строку в формате `<lines>;<words>;<letters>`.
    - [Frequency Dictionary Plugin](/frequency-dictionary-plugin) – частотный словарь, подсчитывает сколько раз встречается каждое слово.
Словоформы не учитывать, т.е. слова _аргумент_ и _аргументы_ считаются двумя _разными_ словами. Знаки препинания не учитываются, союзы,
предлоги и прочие части речи длиной в одну букву _так же_ учитываются. На выходе формировать список строк в формате `<слово> <количество вхождений>`.
1. С помощью `gradle` плагины собираются в jar и подкладываются в папку `$rootDir/plugins`.
1. Результат обработки текста плагином записывать в папку [results](/results) в формате results-<plugin-name>.txt.

## Сборка приложения 
```shell script
# загружает gradle wrapper 6.6
./gradlew wrapper

# сборка проекта, прогон тестов и копирование плагинов в /plugins
./gradlew clean build 

# запуск приложения, важно что это нужно делать из $rootDir, т.к. пути папок plugins/ и results/ строятся от корня
java -jar main-module/build/libs/main-module.jar  
```

##  Комментарии
1. Все классы, в которых необходимо реализовать требуемое поведение, содержат `TODO: NotImplemented` и на них описаны тесты,
чтобы гарантировать корректность поведения.
1. Пример запроса к Wikipedia https://en.wikipedia.org/w/api.php?action=query&format=json&titles=HTML&prop=extracts&explaintext.
1. Для выполнения запроса к Wikipedia использовать [Apache HTTP Client](https://hc.apache.org/httpcomponents-client-ga/tutorial/html/index.html).
[Пример](https://mkyong.com/java/apache-httpclient-examples/).
1. Для парсинга ответа использовать Json (реализацию от Google [Gson](https://github.com/google/gson)) или [JsonPath](https://github.com/json-path/JsonPath).
1. Для загрузки плагинов использовать URLCLassLoader.
1. Если требуется добавить какие-то дополнительные библиотеки, то делать это через Gradle. [Пример](https://docs.gradle.org/current/userguide/dependency_management_for_java_projects.html).

##  Как сдавать?
* Fork этого репозитория
* Merge request вашей реализации в этот репозиторий

## Дедлайн
11 октября 23:59
