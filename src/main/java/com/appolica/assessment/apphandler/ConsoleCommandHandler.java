package com.appolica.assessment.apphandler;

import com.appolica.assessment.io.ApiKeyReader;
import com.appolica.assessment.io.JsonFileWriter;
import com.appolica.assessment.io.Reader;
import com.appolica.assessment.io.Writer;
import com.appolica.assessment.models.dto.HistoricalConversionContainerDTO;
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

            HttpResponse<String> response = sendRequest(url);

            handleResponse(response);

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
    }

    private String buildUrl() {
        return UrlBuilder.buildHistoricalDayConversion(conversionContainer);
    }

    private HttpResponse<String> sendRequest(String url) {
        return httpClient.sendRequest(url);
    }

    private void handleResponse(HttpResponse<String> response) {
        HistoricalConversionContainerDTO jsonResponseDTO = jsonObjectMapper.mapToContainer(response.body(), HistoricalConversionContainerDTO.class);
        conversionContainer.calculateConversion(jsonResponseDTO, currencySet);
        String fileName = conversionContainer.getBaseCurrency().toLowerCase() + "_" + conversionContainer.getTargetCurrency().toLowerCase() + "_" + conversionContainer.getLocalDate();
        jsonFileWriter.write(jsonObjectMapper, fileName, List.of(conversionContainer));
    }

    private void printResult() {
        System.out.printf("%.2f %S is %.2f %S", conversionContainer.getAmount(),
                conversionContainer.getBaseCurrency(),
                conversionContainer.getConvertedAmount(),
                conversionContainer.getTargetCurrency());
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
