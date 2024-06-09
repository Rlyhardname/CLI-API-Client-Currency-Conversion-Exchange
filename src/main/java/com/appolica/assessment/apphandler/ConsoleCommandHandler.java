package com.appolica.assessment.apphandler;

import com.appolica.assessment.exceptions.ResponseCachedException;
import com.appolica.assessment.io.ApiKeyReader;
import com.appolica.assessment.io.JsonFileWriter;
import com.appolica.assessment.io.Reader;
import com.appolica.assessment.io.Writer;
import com.appolica.assessment.models.dto.HistoricalConversionContainerDTO;
import com.appolica.assessment.models.dto.UnrecognizedFieldsDTO;
import com.appolica.assessment.network.CustomHttpClient;
import com.appolica.assessment.objectmapper.JsonMapper;
import com.appolica.assessment.objectmapper.JsonObjectMapper;
import com.appolica.assessment.utils.AmountParser;
import com.appolica.assessment.parser.LocalDateParser;
import com.appolica.assessment.parser.Parser;
import com.appolica.assessment.utils.UrlBuilder;
import com.appolica.assessment.config.Paths;
import com.appolica.assessment.models.ConversionContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

import static com.appolica.assessment.config.CustomURL.API_KEY;
import static com.appolica.assessment.messages.ValidationMessages.*;

public class ConsoleCommandHandler {
    public static ConsoleCommandHandler consoleCommandHandler;
    private Map<String, Map<String, Double>> cacheMap;
    private Scanner sc;
    private ConversionContainer conversionContainer;
    private Set<Currency> currencySet;
    private CustomHttpClient httpClient;
    private JsonMapper jsonObjectMapper;
    private Writer jsonFileWriter;


    public static void start(String... args) {
        if (consoleCommandHandler == null) {
            consoleCommandHandler = new ConsoleCommandHandler(args);
        }

    }

    private void initConfigurations() {
        setConfigurationApiKey();
    }

    private void setConfigurationApiKey() {
        Reader<String> apiKeyReader = new ApiKeyReader();
        API_KEY = apiKeyReader.read(Paths.API_KEY_PATH);
    }

    private void initFields(String... args) {
        sc = new Scanner(System.in);
        conversionContainer = new ConversionContainer(parseArgsToLocalDate(args));
        currencySet = Currency.getAvailableCurrencies();
        httpClient = new CustomHttpClient();
        jsonObjectMapper = new JsonObjectMapper(new ObjectMapper());
        jsonFileWriter = new JsonFileWriter();
        cacheMap = new HashMap<>();
    }

    private LocalDate parseArgsToLocalDate(String... args) {
        Parser<LocalDate> parser = new LocalDateParser();
        if (args.length == 1) {
            return parser.parse(args[0]);
        } else if (args.length == 0) {
            return LocalDate.of(2024, 6, 6);
        }

        throw new IllegalArgumentException(TOO_MANY_ARGUMENTS);
    }

    private ConsoleCommandHandler(String... args) {
        initConfigurations();
        initFields(args);
        while (true) {
            inputAmount();

            inputBaseCurrency();

            inputTargetCurrency();

            String url = buildUrl();

            try {
                HttpResponse<String> response = sendRequest(url);
                handleResponse(response);
            } catch (ResponseCachedException e) {
                calculateCachedDataConversion();
            }

            printResult();
        }
    }


    private void inputAmount() {
        while (true) {
            try {
                String commandLineString = sc.nextLine();
                appState(commandLineString);
                conversionContainer.setAmount(AmountParser.parseToDouble(commandLineString));
                break;
            } catch (NumberFormatException e) {
                System.out.println(INVALID_AMOUNT);
            }

        }

        System.out.printf("%s",System.lineSeparator());
    }

    private void inputBaseCurrency() {
        while (true) {
            try {
                String commandLineString = sc.nextLine();
                appState(commandLineString);
                if (currencySet.contains(Currency.getInstance(commandLineString.toUpperCase()))) {
                    conversionContainer.setBaseCurrency(commandLineString);
                    break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(INVALID_CURRENCY);
            }

        }

        System.out.printf("%s",System.lineSeparator());
    }

    private void inputTargetCurrency() {
        while (true) {
            try {
                String commandLineString = sc.nextLine();
                appState(commandLineString);
                if (currencySet.contains(Currency.getInstance(commandLineString.toUpperCase()))) {
                    conversionContainer.setTargetCurrency(commandLineString);
                    break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(INVALID_CURRENCY);
            }

        }

        System.out.printf("%s",System.lineSeparator());
    }

    private String buildUrl() {
        return UrlBuilder.buildHistoricalDayConversion(conversionContainer);
    }

    private HttpResponse<String> sendRequest(String url) {
        if (!cacheMap.containsKey(conversionContainer.getBaseCurrency())) {
            return httpClient.sendRequest(url);
        }

        if (!cacheMap.get(conversionContainer.getBaseCurrency()).containsKey(conversionContainer.getTargetCurrency())) {
            return httpClient.sendRequest(url);
        }

        throw new ResponseCachedException();
    }

    private void handleResponse(HttpResponse<String> response) {
        HistoricalConversionContainerDTO jsonResponseDTO = jsonObjectMapper.mapToContainer(response.body(), HistoricalConversionContainerDTO.class);
        UnrecognizedFieldsDTO dto = jsonResponseDTO.getUnrecognizedFieldStringPair();

        String baseCurrency = jsonResponseDTO.getBaseCurrency();
        String targetCurrency = dto.getFieldName();
        Double conversionAmount = Double.parseDouble(dto.getFieldValue());

        if (!cacheMap.containsKey(baseCurrency)) {
            HashMap<String, Double> targetCurrencyMap = new HashMap<>();
            cacheMap.put(baseCurrency, targetCurrencyMap);
        }

        if (!cacheMap.get(baseCurrency).containsKey(targetCurrency)) {
            cacheMap.get(baseCurrency).put(targetCurrency, conversionAmount);
        }

        conversionContainer.calculateConversion(jsonResponseDTO, currencySet);
        saveToFile();
    }

    private void calculateCachedDataConversion() {
        String baseCurrency = conversionContainer.getBaseCurrency();
        String targetCurrency = conversionContainer.getTargetCurrency();
        Double conversionAmount = cacheMap.get(baseCurrency).get(targetCurrency);
        conversionContainer.calculateConversion(conversionAmount);

        System.out.println("Cached values: " + System.lineSeparator());
        for (Map.Entry<String, Map<String, Double>> item : cacheMap.entrySet()) {
            System.out.println("Key: "+ item.getKey() + " Value: " + item.getValue());
        }

        System.out.printf("%scalculating from cache%s",System.lineSeparator(),System.lineSeparator());
        saveToFile();
    }

    private void saveToFile() {
        String fileName = conversionContainer.getBaseCurrency().toLowerCase() + "_" + conversionContainer.getTargetCurrency().toLowerCase() + "_" + conversionContainer.getLocalDate();
        jsonFileWriter.write(jsonObjectMapper, fileName, List.of(conversionContainer));
    }

    private void printResult() {
        System.out.printf("%.2f %S is %.2f %S%s", conversionContainer.getAmount(),
                conversionContainer.getBaseCurrency(),
                conversionContainer.getConvertedAmount(),
                conversionContainer.getTargetCurrency(),
                System.lineSeparator());
    }

    private void appState(String state) {
        if (state.equalsIgnoreCase("end")) {
            cleanUpResources();
            System.exit(0);
        }
    }

    private void cleanUpResources() {
        sc.close();
    }


}
