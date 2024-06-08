package com.appolica.assessment.apphandler;

import com.appolica.assessment.io.ApiKeyReader;
import com.appolica.assessment.io.Reader;
import com.appolica.assessment.network.CustomHttpClient;
import com.appolica.assessment.utils.AmountParser;
import com.appolica.assessment.parser.LocalDateParser;
import com.appolica.assessment.parser.Parser;
import com.appolica.assessment.utils.UrlBuilder;
import com.appolica.assessment.config.Paths;
import com.appolica.assessment.models.ConversionContainer;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Scanner;
import java.util.Set;

import static com.appolica.assessment.config.CustomURL.API_KEY;
import static com.appolica.assessment.messages.ValidationMessages.*;

public class ConsoleCommandHandler {
    public static ConsoleCommandHandler consoleCommandHandler;
    private Scanner sc;
    private ConversionContainer conversionContainer;
    private Set<Currency> currencySet;
    private CustomHttpClient httpClient;


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
    }

    private LocalDate parseArgsToLocalDate(String... args) {
        Parser<LocalDate> parser = new LocalDateParser();
        if (args.length == 1) {
            return parser.parse(args[0]);
        } else if (args.length == 0) {
            return LocalDate.of(2024, 06, 06);
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

            sendRequest();
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

    private void sendRequest() {
        String uriHistorical = UrlBuilder.buildHistoricalDayConversion(conversionContainer);
        var response = httpClient.sendRequest(uriHistorical);
        response.body();
        printResponse(response);
    }

    private void printResponse(HttpResponse<String> response) {
        System.out.println(response.body());
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
